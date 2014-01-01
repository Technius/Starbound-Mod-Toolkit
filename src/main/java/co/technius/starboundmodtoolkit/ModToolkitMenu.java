package co.technius.starboundmodtoolkit;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import co.technius.starboundmodtoolkit.mod.Mod;
import co.technius.starboundmodtoolkit.utilui.MessageDialog;

public class ModToolkitMenu extends MenuBar implements EventHandler<ActionEvent>
{
	ModToolkit main;
	MenuItem newm = new MenuItem("New");
	MenuItem open = new MenuItem("Open");
	MenuItem copyto = new MenuItem("Copy To");
	MenuItem exit = new MenuItem("Exit");
	MenuItem about = new MenuItem("About");
	public ModToolkitMenu(ModToolkit main)
	{
		this.main = main;
		Menu file = new Menu("File");
		file.getItems().addAll(newm, open, copyto, exit);
		
		Menu help = new Menu("Help");
		help.getItems().addAll(about);
		
		getMenus().addAll(file, help);
		
		newm.setOnAction(this);
		open.setOnAction(this);
		copyto.setOnAction(this);
		exit.setOnAction(this);
		
		about.setOnAction(this);
	}
	
	public void handle(ActionEvent event) 
	{
		if(event.getSource() == newm)
		{
			main.modPropDiag.loadAndShow(null);
		}
		else if(event.getSource() == open)
		{
			main.dirChooser.setTitle("Open Mod Folder");
			File selected = main.dirChooser.showDialog(main.stage);
			if(selected != null)
			{
				Path path = selected.toPath();
				try 
				{
					Path modinfo = Util.findModInfo(path);
					if(modinfo == null)
					{
						MessageDialog.showMessageDialog(main.stage, "\"" + path.toAbsolutePath()
							+ "\" does not contain a .modinfo file.", "Starbound Mod Toolkit - Error");
						return;
					}
					Mod mod = main.modman.loadMod(path, modinfo);
					if(mod != null)
						main.mods.addModPaneAndFocus(mod);
					else
					{
						MessageDialog.showMessageDialog(main.stage, "\"" + modinfo.toAbsolutePath()
								+ "\" is not a valid .modinfo file.", "Starbound Mod Toolkit - Error");
							return;
					}
				} 
				catch (Throwable e) 
				{
					ModToolkit.log.log(Level.SEVERE, "Failed to load mod data", e);
					MessageDialog.showErrorDialog(main.stage, "An error was "
						+ "encountered while loading \"" + path.toAbsolutePath() + "\": "
						+ Util.diagLogException(e), "Starbound Mod Toolkit - Error", e);
				}
				main.data.addRecentFile(path);
				main.dirChooser.setInitialDirectory(selected.getParentFile());
			}
		}
		else if(event.getSource() == copyto)
		{
			Tab selection = main.mods.getSelectionModel().getSelectedItem();
			if(selection != null)
			{
				ModPane mp = (ModPane) selection.getContent();
				Path dir = mp.mod.getSourceFolder();
				main.dirChooser.setTitle("Copy Mod Folder To");
				File folder = main.dirChooser.showDialog(main.stage);
				if(folder == null)
					return;
				dir = folder.toPath();
				main.data.addRecentFile(dir);
				main.dirChooser.setInitialDirectory(folder.getParentFile());
				
				try 
				{
					main.modman.saveMod(dir, mp.mod);
					MessageDialog.showMessageDialog(main.stage, "Mod folder copied!");
				}
				catch (Throwable e)
				{
					ModToolkit.log.log(Level.SEVERE, "Failed to save mod data", e);
					MessageDialog.showErrorDialog(main.stage, "An error was "
						+ "encountered while saving \"" + dir.toAbsolutePath() + "\": "
						+ Util.diagLogException(e), "Starbound Mod Toolkit - Error", e);
				}
			}
			else MessageDialog.showMessageDialog(main.stage, "Please select a mod.");
		}
		else if(event.getSource() == exit)
			main.requestClose();
		else if(event.getSource() == about)
		{
			String message = "Starbound Mod Toolkit version " + ModToolkit.version + Util.newLine +
				"Starbound is a trademark of Chucklefish LTD.";
			MessageDialog.showMessageDialog(main.stage, message);
		}
	}
}
