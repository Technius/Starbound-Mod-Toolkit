package co.technius.starboundmodtoolkit.mod;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import co.technius.starboundmodtoolkit.ModToolkit;
import co.technius.starboundmodtoolkit.Util;

public class AssetViewer extends BorderPane
{
	Label label = new Label("Select an asset in the list");
	public AssetViewer()
	{
		setTop(label);
		label.setAlignment(Pos.TOP_CENTER);
		setPadding(new Insets(0, 0, 0 ,10));
		setDefault();
	}
	
	public void setDefault()
	{
		setCenter(null);
		label.setText("Select an asset in the list");
	}
	
	public void update(final Asset asset)
	{
		label.setText("Asset: " + asset.path.toString());
		ModToolkit.log.info(asset.getClass().getName());
		try
		{
			asset.load();
		} 
		catch (Throwable e) 
		{
			Util.handleError(e, "An error occurred while loading the asset \"" 
				+ asset.path.toString() + "\"", "Failed to load asset");
			return;
		}
		
		try
		{
			AssetPane p;
			if(asset instanceof JsonAsset)
			{
				JsonAsset ja = (JsonAsset) asset;
				ja.load();
				label.setText(ja.type.toString() + ": " + asset.path.toString());
				JsonAssetPane jap;
				switch (ja.type)
				{
				case OBJECT: jap = new ObjectAssetPane(ja); break;
				case RECIPE: jap = new RecipeAssetPane(ja); break;
				case ITEM: jap = new ItemAssetPane(ja); break;
				case CONSUMABLE: jap = new ConsumableAssetPane(ja); break;
				default:
					jap = new JsonAssetPane(ja);
				}
				jap.load();
				p = jap;
			}
			else p = new AssetPane(asset);
			p.update();
			setCenter(p);
		}
		catch(Throwable e)
		{
			Util.handleError(e, "An error occurred while creating the asset viewer for \"" 
				+ asset.path.toString() + "\"", "Failed to load asset");
		}
	}
}
