package co.technius.starboundmodtoolkit;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import com.eclipsesource.json.JsonObject;

public class JsonPane extends BorderPane
{
	TextArea text = new TextArea();
	public JsonPane()
	{
		setTop(new Label("Warning: This is raw JSON. Edit at your own risk."));
		setCenter(text);
		text.setStyle("-fx-font-family: \'Consolas\', monospace;");
	}
	
	public void loadJson(JsonObject object)
	{
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
}
