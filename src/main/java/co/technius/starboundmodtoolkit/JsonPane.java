package co.technius.starboundmodtoolkit;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import co.technius.starboundmodtoolkit.mod.JsonAssetPane;
import co.technius.starboundmodtoolkit.utilui.MessageDialog;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.ParseException;

public class JsonPane extends BorderPane implements EventHandler<ActionEvent>
{
	private TextArea text = new TextArea();
	private JsonObject loaded = new JsonObject();
	private Button save = new Button("Save");
	private Button cancel = new Button("Cancel");
	public JsonAssetPane jap;
	public JsonPane()
	{
		setTop(new Label("Warning: This is raw JSON. Edit at your own risk."));
		setCenter(text);
		text.setStyle("-fx-font-family: \'Consolas\', monospace;");
		HBox buttons = new HBox();
		buttons.getChildren().addAll(save, cancel);
		buttons.setAlignment(Pos.BASELINE_RIGHT);
		buttons.setSpacing(5);
		setBottom(buttons);
		save.setOnAction(this);
		cancel.setOnAction(this);
	}
	
	public void loadJson(JsonObject object)
	{
		loaded = object;
		String t = Util.toPrettyJson(null, object);
		text.setText(t);
		try
		{
			JsonObject.readFrom(t);
		}
		catch(Throwable e)
		{
			Util.handleError(e, "An error occurred while formatting the JSON",
					"Failed to format JSON correctly");
		}
	}
	
	public JsonObject save()
	{
		try
		{
			JsonValue v = JsonValue.readFrom(text.getText());
			if(v.isObject())
			{
				JsonObject o = JsonObject.readFrom(text.getText());
				ArrayList<String> names = new ArrayList<String>();
				names.addAll(loaded.names());
				for(String s: names)loaded.remove(s);
				for(Member m: o)
					loaded.add(m.getName(), m.getValue());
			}
			else MessageDialog.showMessageDialog(ModToolkit.getInstance().stage,
					"The root JSON value must be a JSON object!");

		}
		catch(ParseException e)
		{
			MessageDialog.showMessageDialog(ModToolkit.getInstance().stage,
				"Error parsing JSON: " + Util.newLine + e.getMessage());
		}
		catch(Throwable e)
		{
			Util.handleError(e, "An error occurred while saving the JSON",
					"Failed to format JSON correctly");
		}
		if(jap != null)
			jap.load();
		return loaded;
	}

	public void handle(ActionEvent event)
	{
		if(event.getSource() == save)
			save();
		else if(event.getSource() == cancel)
			loadJson(loaded);
	}
}
