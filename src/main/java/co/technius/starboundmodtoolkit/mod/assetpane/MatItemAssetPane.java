package co.technius.starboundmodtoolkit.mod.assetpane;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import co.technius.starboundmodtoolkit.mod.JsonAsset;
import co.technius.starboundmodtoolkit.mod.assetpane.JsonObjectBinding.Type;

public class MatItemAssetPane extends ItemAssetPane
{
	@JsonObjectBinding(key = "materialId", type = Type.INTEGER, required = true)
	private TextField materialId = new TextField();

	public MatItemAssetPane(JsonAsset asset)
	{
		super(asset);
		AssetPaneUtils.addWholeNumberListener(null, materialId, enable);
		form.add("Block ID", materialId, new Label("Required"));
	}
}
