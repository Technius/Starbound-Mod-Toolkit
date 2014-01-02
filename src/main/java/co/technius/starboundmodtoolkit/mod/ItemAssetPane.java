package co.technius.starboundmodtoolkit.mod;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import co.technius.starboundmodtoolkit.JsonConstants.Rarity;
import co.technius.starboundmodtoolkit.mod.JsonObjectBinding.Type;

import com.eclipsesource.json.JsonValue;

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
	
	@JsonObjectBinding(key = "inventoryIcon", type = Type.STRING, required = true)
	public TextField inventoryIcon = AssetPaneUtils.noEmptyTextField(enable);
	
	public ItemAssetPane(JsonAsset asset)
	{
		super(asset);
		form.add("Item ID", itemId, new Label("Required"));
		form.add("Item Name", itemName, new Label("Required"));
		form.add("Rarity", rarity, new Label("Required"));
		form.add("Description", description);
		form.add("Inventory Icon", inventoryIcon, new Label("Required"));
	}
	
	public void loadCustom()
	{
		JsonValue rarityVal = asset.object.get("rarity");
		if(rarityVal != null && rarityVal.isString())
		{
			String rarity = rarityVal.asString();
			for(Rarity r: Rarity.values())
			{
				if(r.toString().equals(rarity))
					this.rarity.getSelectionModel().select(r);
			}
		}
		else rarity.getSelectionModel().clearSelection();
	}
}
