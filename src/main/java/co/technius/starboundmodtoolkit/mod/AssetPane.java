package co.technius.starboundmodtoolkit.mod;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import co.technius.starboundmodtoolkit.Util;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AssetPane extends TabPane
{
	protected Asset asset;
	Label path = new Label("");
	Label size = new Label("");
	Label type = new Label("Unknown");
	Tab info = new Tab("Info");
	public AssetPane(Asset asset)
	{
		this.asset = asset;
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		GridPane grid = new GridPane();
		Label lPath = new Label("File Location: ");
		Label lSize = new Label("File size: ");
		Label lType = new Label("Asset type: ");
		GridPane.setConstraints(lPath, 0, 0);
		GridPane.setConstraints(lSize, 0, 1);
		GridPane.setConstraints(lType, 0, 2);
		GridPane.setConstraints(path, 1, 0);
		GridPane.setConstraints(size, 1, 1);
		GridPane.setConstraints(type, 1, 2);
		grid.getChildren().addAll(lPath, lSize, lType, path, size, type);
		info.setContent(grid);
		getTabs().add(info);
		init();
	}
	
	protected void init()
	{
		VBox v = new VBox();
		v.setAlignment(Pos.CENTER);
		Label l = new Label("Unknown asset type.");
		Button b = new Button("Edit in default program");
		b.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) 
			{
				try 
				{
					Desktop.getDesktop().edit(asset.source.resolve(asset.path).toFile());
				} 
				catch (Throwable e)
				{
					Util.handleError(e, "An error occurred while opening the asset \""
						+ asset.path.toFile() + "\" in the default editor",
						"Failed to open default editor");
				}
			}
		});
		v.getChildren().addAll(l, b);
		Tab edit = new Tab("Edit");
		edit.setContent(v);
		getTabs().add(edit);
		getSelectionModel().select(info);
	}
	
	public void update() throws IOException
	{
		Path assetPath = asset.source.resolve(asset.path);
		path.setText(assetPath.toString());
		size.setText(Files.size(assetPath) + " bytes");
		type.setText(asset.type.name());
	}
}
