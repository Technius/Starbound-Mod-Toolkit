package co.technius.starboundmodtoolkit.mod.assetpane;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import co.technius.starboundmodtoolkit.mod.JsonAsset;
import co.technius.starboundmodtoolkit.mod.assetpane.JsonObjectBinding.Type;

public class ModInfoAssetPane extends JsonAssetPane
{
	@JsonObjectBinding(type = Type.STRING, key = "name", required = true)
	public TextField name = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(type = Type.STRING, key = "version", required = true)
	public ComboBox<String> gameVersion = AssetPaneUtils.gameVersionComboBox();
	
	@JsonObjectBinding(type = Type.STRING, key = "path")
	public TextField path = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(type = Type.STRING, base = {"metadata"}, key = "displayName")
	public TextField metaDisplayName = new TextField();
	
	@JsonObjectBinding(type = Type.STRING, base = {"metadata"}, key = "author")
	public TextField metaAuthor = new TextField();
	
	@JsonObjectBinding(type = Type.STRING, base = {"metadata"}, key = "description")
	public TextField metaDescription = new TextField();
	
	@JsonObjectBinding(type = Type.STRING, base = {"metadata"}, key = "version")
	public TextField metaVersion = new TextField();
	
	public ModInfoAssetPane(JsonAsset asset) 
	{
		super(asset);
		form.add(new Label("General"));
		form.add(new Label("Name"), name, new Label("Required"));
		form.add(new Label("Game Version"), gameVersion, new Label("Required"));
		form.add(new Label("Asset Path"), path);
		form.add(new Label("Metadata"));
		form.add(new Label("Display Name"), metaDisplayName);
		form.add(new Label("Author"), metaAuthor);
		form.add(new Label("Description"), metaDescription);
		form.add(new Label("Version"), metaVersion);
	}
}
