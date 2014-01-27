package co.technius.starboundmodtoolkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import co.technius.starboundmodtoolkit.mod.Mod;
import co.technius.starboundmodtoolkit.utilui.MessageDialog;

public class ModToolkitMenu extends MenuBar implements EventHandler<ActionEvent>
{
	ModToolkit main;
	MenuItem newm = new MenuItem("New");
	MenuItem open = new MenuItem("Open");
	MenuItem copyto = new MenuItem("Copy To");
	MenuItem export = new MenuItem("Export");
	MenuItem exit = new MenuItem("Exit");
	MenuItem about = new MenuItem("About");
	
	FileChooser exportDialog = new FileChooser();
	public ModToolkitMenu(ModToolkit main)
	{
		this.main = main;
		Menu file = new Menu("File");
		file.getItems().addAll(newm, open, copyto, export, exit);
		
		Menu help = new Menu("Help");
		help.getItems().addAll(about);
		
		getMenus().addAll(file, help);
		
		newm.setOnAction(this);
		open.setOnAction(this);
		copyto.setOnAction(this);
		export.setOnAction(this);
		exit.setOnAction(this);
		
		about.setOnAction(this);
		
		exportDialog.setTitle("Select mod export destination");
		exportDialog.getExtensionFilters().add(new ExtensionFilter("ZIP archive", "*.zip"));
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
		else if(event.getSource() == export)
		{
			Tab selection = main.mods.getSelectionModel().getSelectedItem();
			if(selection != null)
			{
				File selected = exportDialog.showSaveDialog(main.stage);
				if(selected != null)
				{
					final Path path = selected.toPath();
					main.data.setLastExportPath(path);
					ModPane mp = (ModPane) selection.getContent();
					final Path dir = mp.mod.getSourceFolder();
					try
					{
						final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(selected));
						Files.walkFileTree(dir, new SimpleFileVisitor<Path>(){
							public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
									throws IOException
							{
								String r = dir.relativize(file).toString();
								ZipEntry ent = new ZipEntry(r);
								zos.putNextEntry(ent);
								Files.copy(file, zos);
								zos.closeEntry();
								return FileVisitResult.CONTINUE;
							}
						});
						zos.flush();
						zos.close();
						ModToolkit.log.info("Exported \"" + mp.mod.getName() + " to " + path.toString());
						MessageDialog.showMessageDialog(main.stage, 
							"Export complete!", "Starbound Mod Toolkit");
					}
					catch(Throwable t)
					{
						Util.handleError(t, "An error was encountered while exporting \"" 
							+ mp.mod.getName()+ "\" to " + selected.getAbsolutePath(), 
							"Failed to export mod");
					}
				}
			}
		}
		else if(event.getSource() == exit)
			main.requestClose();
		else if(event.getSource() == about)
		{
			String message = "Starbound Mod Toolkit version " + ModToolkit.version + Util.newLine +
				"Mod Toolkit created by Technius" + Util.newLine + Util.newLine +
				"Starbound is a trademark of Chucklefish LTD.";
			MessageDialog.showMessageDialog(main.stage, message);
		}
	}
}
