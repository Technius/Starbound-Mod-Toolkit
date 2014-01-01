package co.technius.starboundmodtoolkit;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import co.technius.starboundmodtoolkit.mod.AssetViewer;
import co.technius.starboundmodtoolkit.mod.Mod;

public class ModPane extends TabPane implements EventHandler<ActionEvent>
{
	Mod mod;
	ModsPane mods;
	Button prop = new Button("Edit Mod Info");
	Label assetC = new Label("Number of assets: Unknown");
	AssetListPane assets;
	AssetViewer assetPane = new AssetViewer();
	public ModPane(Mod mod, ModsPane mods)
	{
		this.mod = mod;
		this.mods = mods;
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		GridPane grid = new GridPane();
		Insets padding = new Insets(10);
		grid.setPadding(padding);
		GridPane.setConstraints(prop, 0, 0, 2, 1);
		GridPane.setConstraints(assetC, 0, 1, 2, 1);
		grid.getChildren().addAll(prop, assetC);
		Tab main = new Tab("Mod Summary");
		main.setContent(grid);
		BorderPane assetp = new BorderPane();
		assetp.setPadding(padding);
		assets = new AssetListPane(this);
		assetp.setCenter(assetPane);
		assetp.setLeft(assets);
		Tab assetTab = new Tab("Assets");
		assetTab.setContent(assetp);
		getTabs().addAll(main, assetTab);
		prop.setOnAction(this);
	}
	
	public void handle(ActionEvent event)
	{
		if(event.getSource() == prop)
		{
			mods.main.modPropDiag.loadAndShow(mod);
		}
	}
	
	public void updateAssets()
	{
		if(mod.updateAssets())
		{
			assetC.setText("Total number of assets: " + mod.getAssetCount());
		}
	}
}
