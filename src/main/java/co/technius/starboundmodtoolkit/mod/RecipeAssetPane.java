package co.technius.starboundmodtoolkit.mod;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.util.StringConverter;
import co.technius.starboundmodtoolkit.JsonConstants.CraftingGroups;
import co.technius.starboundmodtoolkit.ModToolkit;
import co.technius.starboundmodtoolkit.mod.JsonObjectBinding.Type;
import co.technius.starboundmodtoolkit.utilui.FormPane;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class RecipeAssetPane extends JsonAssetPane implements EventHandler<ActionEvent>
{
	@SuppressWarnings("rawtypes")
	@JsonObjectBinding(key = "input", type = Type.ARRAY,
		keyBinding = {"item", "count"},
		valueBinding = {Type.STRING, Type.INTEGER})
	TableView<ObservableMap> input;
	@SuppressWarnings("rawtypes")
	ObservableList<ObservableMap> items = FXCollections.observableArrayList();
	
	@JsonObjectBinding(base = {"output"}, key = "item", type = Type.STRING, required = true)
	TextField outputItem = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(base = {"output"}, key = "count", type = Type.INTEGER, required = true)
	TextField outputAmount = new TextField();
	
	ListView<CraftingGroups> groups = new ListView<CraftingGroups>();
	
	TextField addIdField = new TextField();
	TextField addAmountField = new TextField();
	Button addButton = new Button("Add Ingredient");
	Button deleteButton = new Button("Delete Ingredient");
	
	ComboBox<CraftingGroups> addGroupsBox = new ComboBox<CraftingGroups>();
	Button addGroupButton = new Button("Add Group");
	Button removeGroupButton = new Button("Remove Selected Group");
	MenuItem removeGroupContext = new MenuItem("Remove");
	ObservableList<CraftingGroups> unusedGroups = 
			FXCollections.observableArrayList(CraftingGroups.values());
	ObservableList<CraftingGroups> usedGroups = FXCollections.observableArrayList();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RecipeAssetPane(JsonAsset asset) 
	{
		super(asset);
		input = new TableView<ObservableMap>(items);
		FormPane inputForm = new FormPane();
		
		TableColumn<ObservableMap, String> idCol = new TableColumn<ObservableMap, String>("Item IDs");
		TableColumn<ObservableMap, Integer> amountCol = 
				new TableColumn<ObservableMap, Integer>("Amount");
		idCol.setCellValueFactory(new MapValueFactory("item"));
		amountCol.setCellValueFactory(new MapValueFactory("count"));
		
		input.getColumns().setAll(idCol, amountCol);
		input.setEditable(true);
		input.getSelectionModel().setCellSelectionEnabled(true);
		
		Callback<TableColumn<ObservableMap, String>, TableCell<ObservableMap, String>> idFactory =
			new Callback<TableColumn<ObservableMap, String>, TableCell<ObservableMap, String>> () {
				public TableCell<ObservableMap, String> call(TableColumn<ObservableMap, String> arg0) 
				{
					return new IdTableCell();
				}
		};
		Callback<TableColumn<ObservableMap, Integer>, TableCell<ObservableMap, Integer>> amountFactory =
			new Callback<TableColumn<ObservableMap, Integer>, TableCell<ObservableMap, Integer>> () {
				public TableCell<ObservableMap, Integer> call(TableColumn<ObservableMap, Integer> arg0) 
				{
					return new AmountTableCell();
				}
		};
		idCol.setCellFactory(idFactory);
		amountCol.setCellFactory(amountFactory);
		input.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		addIdField.setPromptText("Item ID");
		addAmountField.setPromptText("Amount");
		addButton.setOnAction(this);
		deleteButton.setOnAction(this);
		
		inputForm.add(addIdField, addAmountField, addButton);
		inputForm.add(deleteButton);
		
		BorderPane inputWrapper = new BorderPane();
		inputWrapper.setCenter(input);
		inputWrapper.setBottom(inputForm);
		
		TitledPane inputPane = new TitledPane("Ingredients", inputWrapper);		
		GridPane.setColumnSpan(inputPane, 4);
		GridPane.setHgrow(inputPane, Priority.ALWAYS);
		GridPane.setColumnSpan(input, 4);
		form.add(inputPane);
		inputPane.setMinSize(input.getPrefWidth(), input.getPrefHeight());
		//inputPane.autosize();
		
		form.add("Result", outputItem, new Label("Required"));
		form.add("Amount", outputAmount, new Label("Required"));
		
		Label warnLabel = new Label("");
		AssetPaneUtils.addWholeNumberListener(warnLabel, outputAmount, enable);
		form.add(warnLabel);
		
		addGroupsBox.setItems(unusedGroups);
		groups.setItems(usedGroups);
		addGroupButton.setOnAction(this);
		removeGroupButton.setOnAction(this);
		
		BorderPane groupForm = new BorderPane();
		HBox groupButtons = new HBox();
		TitledPane groupsPane = new TitledPane("Crafting Groups", groupForm);
		
		groupForm.setCenter(groups);
		groupButtons.setSpacing(5);
		groupButtons.getChildren().addAll(addGroupsBox, addGroupButton, removeGroupButton);
		groupForm.setBottom(groupButtons);
		
		form.add(groupsPane);
		GridPane.setHgrow(groupsPane, Priority.ALWAYS);
		GridPane.setColumnSpan(groupsPane, 4);
		GridPane.setColumnSpan(groups, 4);
		
		final ContextMenu context = new ContextMenu();
		removeGroupContext.setOnAction(this);
		context.getItems().add(removeGroupContext);
		groups.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) 
			{
				if(event.getButton() == MouseButton.SECONDARY &&
					!groups.getSelectionModel().isEmpty())
				{
					context.show(groups, event.getScreenX(), event.getScreenY());
				}
			}
		});
	}
	
	@Override
	public void loadCustom()
	{
		usedGroups.clear();
		unusedGroups.setAll(CraftingGroups.values());
		JsonValue jGroups = asset.object.get("groups");
		if(jGroups != null && jGroups.isArray())
		{
			for(JsonValue v: (JsonArray) jGroups)
			{
				if(v.isString())
				{
					try
					{
						CraftingGroups g = CraftingGroups.valueOf(v.asString().toUpperCase());
						usedGroups.add(g);
						unusedGroups.remove(g);
					}
					catch(IllegalArgumentException iae)
					{
						ModToolkit.log.info("Invalid/unknown group: " + v.asString());
					}
				}
			}
		}
		addGroupsBox.getSelectionModel().selectFirst();
	}
	
	@Override
	public void saveCustom()
	{
		JsonArray jGroups = new JsonArray();
		for(CraftingGroups g: usedGroups)
		{
			ModToolkit.log.info(g.toString());
			jGroups.add(g.toString());
		}
		asset.object.set("groups", jGroups);
	}
	
	@SuppressWarnings("rawtypes")
	class IdTableCell extends TextFieldTableCell<ObservableMap, String>
	{
		public IdTableCell()
		{
			super(new StringConverter<String>() {
				public String toString(String arg0)
				{
					return arg0;
				}  
				public String fromString(String string) 
				{
					return string;
				}                       
			});
		}
		
		public void commitEdit(String val)
		{
			String nV = !val.trim().isEmpty() ? val.trim() : getItem();
			super.commitEdit(nV);
		}
	}
	
	@SuppressWarnings("rawtypes")
	class AmountTableCell extends TextFieldTableCell<ObservableMap, Integer>
	{
		public AmountTableCell()
		{
			super(new StringConverter<Integer>() {
				public String toString(Integer i)
				{
					if(i == null)return "";
					return i.toString();
				}  
				public Integer fromString(String string) 
				{
					try
					{
						return Integer.parseInt(string);
					}
					catch(NumberFormatException nfe)
					{
						return -1;
					}
				}
			});
		}
		
		public void commitEdit(Integer val)
		{
			Integer nV = val >= 0 ? val : getItem();
			super.commitEdit(nV);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void handle(ActionEvent event)
	{
		if(event.getSource() == addButton)
		{
			try
			{
				ObservableMap m = FXCollections.observableHashMap();
				ModToolkit.log.info(addIdField.getText() + "," + addAmountField.getText());
				m.put("item", addIdField.getText());
				m.put("count", Integer.parseInt(addAmountField.getText()));
				ModToolkit.log.info(m.get("item").toString());
				input.getItems().add(m);
			}
			catch(NumberFormatException nfe)
			{
				nfe.printStackTrace();
			}
		}
		else if(event.getSource() == deleteButton)
		{
			ObservableMap m = input.getSelectionModel().getSelectedItem();
			if(m != null)input.getItems().remove(m);
		}
		else if(event.getSource() == addGroupButton)
		{
			CraftingGroups g = addGroupsBox.getSelectionModel().getSelectedItem();
			if(g != null)
			{
				unusedGroups.remove(g);
				usedGroups.add(g);
			}
		}
		else if(event.getSource() == removeGroupButton || event.getSource() == removeGroupContext)
		{
			CraftingGroups g = groups.getSelectionModel().getSelectedItem();
			if(g != null && usedGroups.size() > 1)
			{
				usedGroups.remove(g);
				unusedGroups.add(0, g);
				addGroupsBox.getSelectionModel().selectFirst();
			}
		}
	}
}
