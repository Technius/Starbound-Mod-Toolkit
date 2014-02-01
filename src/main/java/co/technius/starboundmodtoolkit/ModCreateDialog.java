package co.technius.starboundmodtoolkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import co.technius.starboundmodtoolkit.mod.Mod;
import co.technius.starboundmodtoolkit.mod.ModInfo;
import co.technius.starboundmodtoolkit.mod.assetpane.AssetPaneUtils;

public class ModCreateDialog extends Stage implements EventHandler<ActionEvent>
{
	TextField name = new TextField();
	ComboBox<String> gameVersions = AssetPaneUtils.gameVersionComboBox();
	
	Button save = new Button("Create");
	Button cancel = new Button("Cancel");
	private ModToolkit main;
	
	public ModCreateDialog(ModToolkit main)
	{
		this.main = main;
		initOwner(main.stage);
		initModality(Modality.APPLICATION_MODAL);
		setTitle("New mod");
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setAlignment(Pos.TOP_LEFT);
		Label lname = new Label("Mod Name");
		GridPane.setConstraints(lname, 0, 0);
		grid.getChildren().add(lname);
		name.setPromptText("Name of the mod");
		GridPane.setConstraints(name, 1, 0);
		grid.getChildren().add(name);
		Label lversions = new Label("Game Version");
		GridPane.setConstraints(lversions, 0, 1);
		grid.getChildren().add(lversions);
		GridPane.setConstraints(gameVersions, 1, 1);
		grid.getChildren().add(gameVersions);
		GridPane advp = new GridPane();
		advp.setPadding(new Insets(10, 10, 10, 10));
		advp.setVgap(5);
		advp.setHgap(5);
		
		HBox btns = new HBox();
		btns.setSpacing(5);
		GridPane.setConstraints(btns, 0, 2, 2, 1);
		btns.setAlignment(Pos.BASELINE_RIGHT);
		save.disableProperty().set(true);
		btns.getChildren().addAll(save, cancel);
		grid.getChildren().add(btns);
		Scene scene = new Scene(grid);
		setScene(scene);
		centerOnScreen();
		sizeToScene();
		
		name.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> v,
					String o, String n) {
				save.disableProperty().set(n.isEmpty());
			}
		});
		
		save.setOnAction(this);
		cancel.setOnAction(this);
	}

	public void handle(ActionEvent event)
	{
		if(event.getSource() == save)
		{
			main.dirChooser.setTitle("Choose Mod Save Location");
			File f = main.dirChooser.showDialog(main.stage);
			if(f == null)
			{
				close();
				return;
			}
			Path p = f.toPath();
			main.dirChooser.setInitialDirectory(f.getParentFile());
			Mod mod = new Mod(name.getText(), gameVersions.getValue());
			mod.setSourceFolder(p);
			ModInfo info = mod.getInfo();
			info.setSource(p);
			main.mods.addModPaneAndFocus(mod);
			
			try 
			{
				Util.writeJsonToFile(info.getObject(), mod.getSourceFolder().resolve(
					info.getName() + ".modinfo"));
			} 
			catch (IOException e)
			{
				Util.handleError(e, "An error occurred while saving the .modinfo file", 
					"Failed to save .modinfo");
			}
			
			close();
		}
		else if(event.getSource() == cancel)
			close();
	}
	
	public void showDialog()
	{
		name.setText("");
		show();
	}
}
