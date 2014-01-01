package co.technius.starboundmodtoolkit.mod;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import co.technius.starboundmodtoolkit.JsonConstants.Rarity;
import co.technius.starboundmodtoolkit.mod.JsonObjectBinding.Type;

public class ItemAssetPane extends JsonAssetPane
{
	@JsonObjectBinding(key = "itemName", type = Type.STRING, required = true)
	public TextField itemId = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "shortdescription", type = Type.STRING, required = true)
	public TextField itemName = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "description", type = Type.STRING)
	public TextArea description = AssetPaneUtils.noLinesTextArea();
	
	@JsonObjectBinding(key = "rarity", type = Type.STRING, required = true)
	public ComboBox<Rarity> rarity = AssetPaneUtils.rarityBox();
	
	public ItemAssetPane(JsonAsset asset)
	{
		super(asset);
		form.add("Item ID", itemId, new Label("Required"));
		form.add("Item Name", itemName, new Label("Required"));
		form.add("Rarity", rarity, new Label("Required"));
		form.add("Description", description);
	}
}
