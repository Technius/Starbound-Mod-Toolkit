package co.technius.starboundmodtoolkit.mod.assetpane;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import co.technius.starboundmodtoolkit.mod.JsonAsset;
import co.technius.starboundmodtoolkit.mod.assetpane.JsonObjectBinding.Type;

public class CelestialAssetPane extends ItemAssetPane
{
	@JsonObjectBinding(key = "image", type = Type.STRING, required = true)
	TextField image = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "sectorUnlock", type = Type.STRING, required = true)
	TextField sectorUnlock = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "tierRecipesUnlock", type = Type.STRING, required = true)
	TextField tierRecipesUnlock = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "unlockMessage", type = Type.STRING, required = true)
	TextField unlockMessage = AssetPaneUtils.noEmptyTextField(enable);
	
	public CelestialAssetPane(JsonAsset asset) 
	{
		super(asset);
		form.add("Image", image, new Label("Required"));
		form.add("Unlocked Sector", sectorUnlock, new Label("Required"));
		form.add("Unlocked Recipe Tier", tierRecipesUnlock, new Label("Required"));
		form.add("Message", unlockMessage, new Label("Required"));
	}
}
