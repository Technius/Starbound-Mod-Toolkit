package co.technius.starboundmodtoolkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import co.technius.starboundmodtoolkit.mod.Mod;
import co.technius.starboundmodtoolkit.mod.ModInfo;
import co.technius.starboundmodtoolkit.utilui.AccordionSizeHack;

public class ModPropertiesDialog extends Stage implements EventHandler<ActionEvent>
{
	Button save = new Button("Create");
	Button cancel = new Button("Cancel");
	ModPropertiesPane mp = new ModPropertiesPane();
	public static final int CREATE = 0;
	public static final int EDIT = 1;
	private int mode = CREATE;
	Mod loaded = null;
	private ModToolkit main;
	
	public ModPropertiesDialog(ModToolkit main)
	{
		this.main = main;
		initOwner(main.stage);
		initModality(Modality.APPLICATION_MODAL);
		setTitle("New mod");
		GridPane.setConstraints(mp.acc, 0, 3, 2, 1);
		HBox btns = new HBox();
		btns.setSpacing(5);
		GridPane.setConstraints(btns, 0, 2, 2, 1);
		btns.setAlignment(Pos.BASELINE_RIGHT);
		save.disableProperty().set(true);
		btns.getChildren().addAll(save, cancel);
		mp.getChildren().add(btns);
		Scene scene = new Scene(mp);
		setScene(scene);
		centerOnScreen();
		sizeToScene();
		
		mp.name.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> v,
					String o, String n) {
				save.disableProperty().set(n.isEmpty());
			}
		});
		
		AccordionSizeHack.add(mp.acc, this);
		save.setOnAction(this);
		cancel.setOnAction(this);
	}

	public void handle(ActionEvent event)
	{
		if(event.getSource() == save)
		{
			Mod mod;
			ModInfo info;
			String oldname = null;
			if(loaded != null)
			{
				String name = mp.name.getText().trim();
				if(!name.equals(loaded.getName()))
					oldname = loaded.getName();
				info = loaded.getInfo();
				info.setName(name);
				info.setGameVersion(mp.gameVersions.getSelectionModel().getSelectedItem());
				info.setAssetPath(mp.path.getText());
				main.mods.getSelectionModel().getSelectedItem().setText(name);
				mod = loaded;
			}
			else
			{
				main.dirChooser.setTitle("Choose Mod Save Location");
				File f = main.dirChooser.showDialog(main.stage);
				if(f != null)
				{
					Path p = f.toPath();
					main.dirChooser.setInitialDirectory(f.getParentFile());
					mod = new Mod(mp.name.getText(), mp.gameVersions.getSelectionModel().getSelectedItem());
					mod.setSourceFolder(p);
					info = mod.getInfo();
					info.setAssetPath(mp.path.getText());
					main.mods.addModPaneAndFocus(mod);
				}
				else 
				{
					close();
					return;
				}
			}
			
			try 
			{
				if(oldname != null)
					Files.deleteIfExists(loaded.getSourceFolder().resolve(
							oldname + ".modinfo"));
				Util.writeJsonToFile(info.toJson(), mod.getSourceFolder().resolve(
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
	
	public int getMode()
	{
		return mode;
	}
	
	public void setMode(int mode)
	{
		this.mode = mode;
		if(mode == CREATE)
		{
			save.setText("Create");
			setTitle("New Mod");
		}
		else if(mode == EDIT)
		{
			save.setText("Save");
			setTitle("Edit .modinfo");
		}
	}
	
	public void loadAndShow(Mod mod)
	{
		loaded = mod;
		if(mod == null)
		{
			setMode(CREATE);
			mp.name.setText("");
			mp.path.setText(".");
		}
		else
		{
			setMode(EDIT);
			ModInfo info = mod.getInfo();
			mp.name.setText(info.getName());
			mp.gameVersions.getSelectionModel().select(info.getGameVersion());
			mp.path.setText(info.getAssetPath().toString().replace(File.separatorChar, '/'));
		}
		show();
	}
}
