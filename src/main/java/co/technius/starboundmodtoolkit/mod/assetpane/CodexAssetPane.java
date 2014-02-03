package co.technius.starboundmodtoolkit.mod.assetpane;

import java.util.concurrent.Callable;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import co.technius.starboundmodtoolkit.mod.JsonAsset;
import co.technius.starboundmodtoolkit.mod.assetpane.JsonObjectBinding.Type;
import co.technius.starboundmodtoolkit.utilui.SwappableNodePane;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class CodexAssetPane extends JsonAssetPane
{
	@JsonObjectBinding(key = "id", type = Type.STRING, required = true)
	public TextField codexId = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "title", type = Type.STRING, required = true)
	public TextField title = AssetPaneUtils.noEmptyTextField(enable);
	
	SwappableNodePane<TextArea> pages;
	
	public CodexAssetPane(JsonAsset asset)
	{
		super(asset);
		form.add(new Label("Codex ID"), codexId, new Label("Required"));
		form.add(new Label("Title"), title, new Label("Required"));
		pages = new SwappableNodePane<TextArea>(new Callable<TextArea>(){
			public TextArea call() throws Exception
			{
				return AssetPaneUtils.noLinesTextArea();
			}
		});
		pages.setPrefix("Page");
		form.add(pages.getLabel(), pages);
	}
	
	@Override
	public void loadCustom()
	{
		pages.getPages().clear();
		JsonArray cpages = asset.getObject().get("contentPages").asArray();
		if(cpages.size() == 0)
			pages.getPages().add(AssetPaneUtils.noLinesTextArea());
		else
		{
			boolean f = true;
			for(JsonValue v: cpages)
			{
				TextArea a = AssetPaneUtils.noLinesTextArea();
				a.setText(v.asString());
				pages.getPages().add(a);
				if(f)f = false;
				else a.setVisible(false);
			}
		}
		pages.reset();
	}
	
	@Override
	public void saveCustom()
	{
		JsonArray a = new JsonArray();
		for(Node n: pages.getPages())
			a.add(((TextArea)n).getText());
		asset.getObject().set("contentPages", a);
	}
}
