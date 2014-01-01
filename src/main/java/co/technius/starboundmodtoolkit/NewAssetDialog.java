package co.technius.starboundmodtoolkit;

import java.nio.file.Path;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import co.technius.starboundmodtoolkit.mod.Asset;
import co.technius.starboundmodtoolkit.mod.AssetType;
import co.technius.starboundmodtoolkit.mod.JsonAsset;
import co.technius.starboundmodtoolkit.utilui.ModalDialog;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

public class NewAssetDialog extends ModalDialog implements EventHandler<ActionEvent>
{
	ModPane mp;
	ModToolkit main;
	ComboBox<AssetType> type = new ComboBox<AssetType>();
	TextField name = new TextField();
	Button create = new Button("Create");
	Button cancel = new Button("Cancel");
	public NewAssetDialog(ModPane mp, Stage mainStage)
	{
		super(mainStage, "New Asset");
		this.mp = mp;
		main = mp.mods.main;
		type.setItems(FXCollections.observableArrayList(AssetType.getJsonAssets()));
		type.getSelectionModel().selectFirst();
		GridPane pane = new GridPane();
		Label nameLabel = new Label("Name");
		name.setPromptText("Location of the asset");
		GridPane.setConstraints(nameLabel, 0, 0);
		GridPane.setConstraints(name, 1, 0);
		Label typeLabel = new Label("Type");
		GridPane.setConstraints(typeLabel, 0, 1);
		GridPane.setConstraints(type, 1, 1);
		pane.getChildren().addAll(nameLabel, name, typeLabel, type);
		HBox buttons = new HBox();
		buttons.getChildren().addAll(create, cancel);
		box.getChildren().addAll(pane, buttons);
		init();
		
		create.setOnAction(this);
		cancel.setOnAction(this);
	}
	
	public void handle(ActionEvent event)
	{
		if(event.getSource() == create)
		{
			try
			{
				AssetType type = this.type.getSelectionModel().getSelectedItem();
				Path af = mp.mod.getAssetFolder();
				Path p = af.resolve(name.getText() + "." + type.getFileExtension());
				p = af.relativize(p);
				ModToolkit.log.info(p.toString());
				Asset a = Asset.createAsset(p, type, af);
				if(a instanceof JsonAsset)
				{
					JsonObject templ = main.data.getJsonTemplate(type);
					if(templ != null)
					{
						JsonAsset ja = (JsonAsset) a;
						JsonObject jo = ja.getObject();
						for(Member m: templ)
						{
							jo.add(m.getName(), m.getValue());
						}
					}
				}
				a.save();
				mp.mod.getAssets().add(a);
				mp.assets.list.getSelectionModel().select(a);
			}
			catch(Throwable t)
			{
				Util.handleError(t, "An error occurred while creating an asset", 
					"Failed to create asset");
			}
			close();
		}
		else
		{
			close();
		}
	}
}
