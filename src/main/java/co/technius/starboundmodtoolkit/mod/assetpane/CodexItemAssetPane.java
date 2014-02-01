package co.technius.starboundmodtoolkit.mod.assetpane;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import co.technius.starboundmodtoolkit.JsonConstants.Rarity;
import co.technius.starboundmodtoolkit.mod.JsonAsset;
import co.technius.starboundmodtoolkit.mod.assetpane.JsonObjectBinding.Type;

public class CodexItemAssetPane extends JsonAssetPane
{
	@JsonObjectBinding(key = "itemName", type = Type.STRING, required = true)
	public TextField itemId = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "shortdescription", type = Type.STRING, required = true)
	public TextField itemName = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "codexId", type = Type.STRING, required = true)
	public TextField codexId = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "title", type = Type.STRING, required = true)
	public TextField codexTitle = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "rarity", type = Type.STRING, required = true)
	public ComboBox<Rarity> rarity = AssetPaneUtils.rarityBox();
	
	@JsonObjectBinding(key = "description", type = Type.STRING)
	public TextArea description = AssetPaneUtils.noLinesTextArea();
	
	@JsonObjectBinding(key = "inventoryIcon", type = Type.STRING, required = true)
	public TextField inventoryIcon = AssetPaneUtils.noEmptyTextField(enable);
	
	public CodexItemAssetPane(JsonAsset asset)
	{
		super(asset);
		form.add("Item ID", itemId, new Label("Required"));
		form.add("Item Name", itemName, new Label("Required"));
		form.add("Rarity", rarity, new Label("Required"));
		form.add("Codex ID", codexId, new Label("Required"));
		form.add("Codex Display Title", codexTitle, new Label("Required"));
		form.add("Description", description);
		form.add("Inventory Icon", inventoryIcon, new Label("Required"));
	}
}
