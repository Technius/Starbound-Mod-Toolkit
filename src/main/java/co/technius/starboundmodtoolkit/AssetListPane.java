package co.technius.starboundmodtoolkit;

import java.nio.file.Files;
import java.nio.file.Path;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import co.technius.starboundmodtoolkit.mod.Asset;
import co.technius.starboundmodtoolkit.utilui.TextInputDialog;

public class AssetListPane extends BorderPane implements ChangeListener<Asset>, 
	EventHandler<ActionEvent>
{
	private ModPane mp;
	ListView<Asset> list = new ListView<Asset>();
	Button bNew = new Button("New");
	Button delete = new Button("Delete");
	Button rename = new Button("Rename");
	MenuItem deleteContext = new MenuItem("Delete");
	MenuItem renameContext = new MenuItem("Rename");
	public AssetListPane(ModPane mp)
	{
		this.mp = mp;
		setTop(new Label("Assets"));
		setCenter(list);
		HBox buttons = new HBox();
		buttons.getChildren().addAll(bNew, delete, rename);
		setBottom(buttons);
		list.setItems(mp.mod.getAssets());
		list.getSelectionModel().selectedItemProperty().addListener(this);
		bNew.setOnAction(this);
		delete.setOnAction(this);
		rename.setOnAction(this);
		
		final ContextMenu context = new ContextMenu();
		deleteContext.setOnAction(this);
		renameContext.setOnAction(this);
		context.getItems().addAll(deleteContext, renameContext);
		list.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) 
			{
				if(event.getButton() == MouseButton.SECONDARY &&
					!list.getSelectionModel().isEmpty())
				{
					context.show(list, event.getScreenX(), event.getScreenY());
				}
			}
		});
	}
	
	public void changed(ObservableValue<? extends Asset> v,
			Asset o, Asset n) 
	{
		if(n == null)return;
		mp.assetPane.update(n);
	}

	public void handle(ActionEvent event) 
	{
		if(event.getSource() == bNew)
		{
			new NewAssetDialog(mp, mp.mods.main.stage).show();
		}
		else if(event.getSource() == delete || event.getSource() == deleteContext)
		{
			Asset a = list.getSelectionModel().getSelectedItem();
			if(a != null)
			{
				try
				{
					Files.delete(mp.mod.getAssetFolder().resolve(a.getPath()));
					mp.mod.getAssets().remove(a);
					list.getSelectionModel().selectPrevious();
					if(list.getItems().isEmpty())
					{
						mp.assetPane.setDefault();
					}
				} 
				catch (Throwable t) 
				{
					Util.handleError(t, "An error occurred while deleting the asset", 
						"Failed to delete asset");
				}
			}
		}
		else if(event.getSource() == rename || event.getSource() == renameContext)
		{
			Asset a = list.getSelectionModel().getSelectedItem();
			if(a != null)
			{
				String r = new TextInputDialog(mp.mods.main.stage, "Rename Asset").getResponse();
				if(r != null)
				{
					try
					{
						Path af = mp.mod.getAssetFolder();
						Path old = af.resolve(a.getPath());
						Path n = af.resolve(r);
						Files.move(old, n);
						mp.mod.getAssets().remove(a);
						mp.mod.updateAsset(n);
					} 
					catch (Throwable t) 
					{
						Util.handleError(t, "An error occurred while renaming the asset", 
							"Failed to rename asset");
					}
				}
			}
		}
	}
}
