package co.technius.starboundmodtoolkit.mod.assetpane;

import co.technius.starboundmodtoolkit.mod.JsonAsset;
import co.technius.starboundmodtoolkit.utilui.PalettePane;

public class BackAssetPane extends ItemAssetPane
{
	PalettePane colorPalette = new PalettePane();
	public BackAssetPane(JsonAsset asset) 
	{
		super(asset);
		form.add(colorPalette.getLabel(), colorPalette);
	}
}
