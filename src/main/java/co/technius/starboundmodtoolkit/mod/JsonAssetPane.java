package co.technius.starboundmodtoolkit.mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import co.technius.starboundmodtoolkit.JsonPane;
import co.technius.starboundmodtoolkit.ModToolkit;
import co.technius.starboundmodtoolkit.Util;
import co.technius.starboundmodtoolkit.mod.JsonObjectBinding.Type;
import co.technius.starboundmodtoolkit.util.AndBoolean;
import co.technius.starboundmodtoolkit.utilui.FormPane;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class JsonAssetPane extends AssetPane
{
	protected JsonAsset asset;
	
	protected FormPane form = new FormPane();
	protected Button saveChanges = new Button("Save Changes");
	protected Button cancel = new Button("Cancel");
	protected AndBoolean enable = new AndBoolean();
	protected JsonPane jsonPane;
	
	public JsonAssetPane(JsonAsset asset) 
	{
		super(asset);
		Tab edit = new Tab("Edit");
		BorderPane pane = new BorderPane();
		edit.setContent(pane);
		getTabs().add(1, edit);
		getSelectionModel().select(edit);
		if(this.getClass() == JsonAssetPane.class)
		{
			pane.setCenter(new Label("This object type's editor hasn't been added yet." + Util.newLine
				+ "If you know what you are doing, you can use the JSON panel instead."));
			return;
		}
		pane.setCenter(form);
		pane.setPadding(new Insets(0, 0, 0, 0));
		enable.addListener(new ChangeListener<Boolean>(){
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if(arg2 != null)
				{
					saveChanges.disableProperty().set(!arg2);
				}
			}
		});
		HBox buttons = new HBox();
		buttons.getChildren().addAll(saveChanges, cancel);
		pane.setBottom(buttons);
		buttons.setAlignment(Pos.BASELINE_RIGHT);
		buttons.setSpacing(5);
		buttons.setPadding(new Insets(5));
		
		saveChanges.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				save();
			}
		});
		cancel.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				load();
			}
		});
	}
	
	public void init()
	{
		jsonPane = new JsonPane();
		asset = (JsonAsset) super.asset;
		Tab json = new Tab("Raw JSON");
		jsonPane.loadJson(asset.object);
		json.setContent(jsonPane);
		getTabs().add(json);
	}
	
	public final void load()
	{
		try
		{
			Class<?> clazz = getClass();
			while(clazz != JsonAssetPane.class)
			{
				loadForClass(clazz);
				clazz = clazz.getSuperclass();
			}
			loadCustom();
		}
		catch(Throwable t)
		{
			Util.handleError(t, "An error occurred while loading asset data", 
				"Failed to load JsonObject");
		}
		jsonPane.loadJson(asset.object);
	}
	
	public final void save()
	{
		try
		{
			Class<?> clazz = getClass();
			while(clazz != JsonAssetPane.class)
			{
				saveForClass(clazz);
				clazz = clazz.getSuperclass();
			}
			saveCustom();
			asset.save();
		}
		catch(Throwable t)
		{
			Util.handleError(t, "An error occurred while editing asset data", 
				"Failed to modify JsonObject");
		}
		jsonPane.loadJson(asset.object);
	}
	
	protected void loadCustom()
	{
		
	}
	
	protected void saveCustom()
	{
		
	}
	
	@SuppressWarnings("unchecked")
	private void loadForClass(Class<?> clazz) throws Throwable
	{
		for(Field f: clazz.getDeclaredFields())
		{
			f.setAccessible(true);
			Annotation a = f.getAnnotation(JsonObjectBinding.class);
			if(a == null)continue;
			JsonObjectBinding b = (JsonObjectBinding) a;
			String key = b.key();
			JsonObject base = getJsonFromBinding(b, false);
			if(base == null)
			{
				if(b.required())throw new IllegalArgumentException("Invalid " 
					+ asset.type.toString() + " file");
				continue;
			}
			JsonValue val = base.get(key);
			if(val == null)
			{
				if(b.required())
					throw new IllegalArgumentException(asset.type.toString() + " file missing "
						+ "required key: " + key);
				continue;
			}
			Object obj = f.get(this);
			if(obj instanceof TextInputControl)
			{
				String s;
				switch(b.type())
				{
				case STRING: s = val.asString(); break;
				case INTEGER:
					if(val.isNumber())
						s = Integer.toString(val.asInt()); 
					else if(val.isString())
						Integer.parseInt((s = val.asString()));
					else throw new UnsupportedOperationException(
						"Not an integer: " + val.toString());
					break;
				case DOUBLE: s = Double.toString(val.asDouble()); break;
				default: throw new UnsupportedOperationException(
					"TextInput does not support type " + b.type().name());
				}
				((TextInputControl)obj).setText(s);
			}
			else if(obj instanceof CheckBox)
			{
				validateType(key, val, Type.BOOLEAN);
				((CheckBox)obj).setSelected(base.get(key).asBoolean());
			}
			else if(obj instanceof ComboBox)
			{
				validateType(key, val, Type.STRING);
				ComboBox<?> cbox = (ComboBox<?>) obj;
				try
				{
					ObservableList<?> items = cbox.getItems();
					for(int i = 0; i < items.size(); i ++)
					{
						Object o = items.get(i);
						if(o.toString().equals(val))
							cbox.getSelectionModel().select(i);
					}
				}
				catch(IllegalArgumentException iae)
				{
					cbox.getSelectionModel().selectFirst();
				}
			}
			else if(obj instanceof TableView)
			{
				@SuppressWarnings("rawtypes")
				TableView v = (TableView) obj;
				v.getItems().clear();
				validateType(key, val, Type.ARRAY);
				for(JsonValue m : ((JsonArray)val))
				{
					if(m.isObject())
					{
						Map<String, Object> row = FXCollections.observableHashMap();
						JsonObject mo = m.asObject();
						for(String k: b.keyBinding())
						{
							row.put(k, Util.getValue(mo.get(k)));
						}
						v.getItems().add(row);
					}
				}
				@SuppressWarnings("rawtypes")
				ObservableList<TableColumn> columns = FXCollections.observableArrayList();
				columns.addAll(v.getColumns());
				v.getColumns().clear();
				v.getColumns().setAll(columns);
				v.requestLayout();
			}
		}
	}
	
	private void saveForClass(Class<?> clazz) throws Throwable
	{
		for(Field f: clazz.getDeclaredFields())
		{
			f.setAccessible(true);
			Annotation a = f.getAnnotation(JsonObjectBinding.class);
			if(a == null)continue;
			JsonObjectBinding b = (JsonObjectBinding) a;
			String key = b.key();
			JsonObject base = getJsonFromBinding(b, true);
			assert base != null;
			Object obj = f.get(this);
			if(obj instanceof TextInputControl)
			{
				String data = ((TextInputControl) obj).getText();
				if(!b.required() && data.trim().isEmpty())
				{
					base.remove(key);
					continue;
				}
				switch(b.type())
				{
				case STRING: base.set(key, data); break;
				case INTEGER: base.set(key, Integer.parseInt(data)); break;
				case DOUBLE: base.set(key, Double.parseDouble(data)); break;
				case BOOLEAN: base.set(key, Boolean.parseBoolean(data)); break;
				default: throw new UnsupportedOperationException(
					"TextInput does not support type " + b.type().name());
				}
			}
			else if(obj instanceof CheckBox)
			{
				checkType(Type.BOOLEAN, b.type(), CheckBox.class);
				base.set(key, ((CheckBox)obj).isSelected());
			}
			else if(obj instanceof ComboBox)
			{
				checkType(Type.STRING, b.type(), ComboBox.class);
				base.set(key, ((ComboBox<?>)obj).getSelectionModel()
					.getSelectedItem().toString());
			}
			else if(obj instanceof TableView)
			{
				JsonArray ar = new JsonArray();
				@SuppressWarnings({ "rawtypes", "unchecked" })
				ObservableList<Map> ob = ((TableView)obj).getItems();
				for(@SuppressWarnings("rawtypes") Map m: ob)
				{
					JsonObject no = new JsonObject();
					for(String k: b.keyBinding())
					{
						JsonValue v = Util.getValue(m.get(k));
						ModToolkit.log.info(v.toString());
						no.set(k, v);
					}
					ar.add(no);
				}
				base.set(key, ar);
			}
		}
	}
	
	private void checkType(Type wanted, Type given, Class<?> c)
	{
		if(given != wanted)
			throw new UnsupportedOperationException(c.getSimpleName() +
				"does not support type " + given.name());
	}
	
	private void validateType(String key, JsonValue val, Type wanted)
	{
		boolean valid = true;
		if(val.isString() && wanted != Type.STRING)valid = false;
		else if(val.isNumber() && (wanted != Type.INTEGER || wanted != Type.DOUBLE))valid = false;
		else if(val.isObject() && wanted != Type.OBJECT)valid = false;
		else if(val.isArray() && wanted != Type.ARRAY)valid = false;
		if(!valid)
			throw new IllegalArgumentException("Key" + (key == null ? "" : " \"" + key + "\"") 
				+ " is not of type " + wanted.name());
	}
	
	private JsonObject getJsonFromBinding(JsonObjectBinding a, boolean create)
	{
		JsonObject base = asset.object;
		String[] keys = a.base();
		for(String s: keys)
		{
			JsonValue val = base.get(s);
			if(val == null)
			{
				if(create)base = base.set(s, new JsonObject());
				else return null;
			}
			else if(!val.isObject())
				throw new IllegalArgumentException("Expected JsonObject; got " + val.getClass().getSimpleName());
			base = (JsonObject)val;
		}
		return base;
	}
}
