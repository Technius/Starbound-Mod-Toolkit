package co.technius.starboundmodtoolkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import co.technius.starboundmodtoolkit.utilui.MessageDialog;

public class ModToolkit extends Application implements EventHandler<WindowEvent>
{	
	public static final Logger log = Logger.getLogger("Starbound Mod Toolkit");
	
	Stage stage;
	TabPane tabs;
	ModsPane mods;
	ModManager modman;
	ModPropertiesDialog modPropDiag;
	DirectoryChooser dirChooser;
	FileChooser fileChooser;
	DataFiles data;
	private static ModToolkit instance;
	static String version;
	ModToolkitMenu menu;
	
	public static void init(String[] args)
	{
		launch(args);
	}
	
	public void start(Stage stage) throws Exception
	{
		try
		{
			start0(stage);
		}
		catch(Throwable e)
		{
			String msg = "An error was encountered while starting the application: " + 
				Util.diagLogException(e) + Util.newLine + "Click \"Close\" to terminate the program.";
			log.log(Level.SEVERE, "Failed to start application", e);
			MessageDialog.showErrorDialog(null, msg, "Starbound Mod Toolkit - Error", e);
		}
	}
	
	public void start0(Stage stage) throws Throwable
	{
		instance = this;
		this.stage = stage;
		modPropDiag = new ModPropertiesDialog(this);
		fileChooser = new FileChooser();
		dirChooser = new DirectoryChooser();
		modman = new ModManager();
		data = new DataFiles();
		stage.setTitle("Starbound Mod Toolkit");
		BorderPane root = new BorderPane();
		
		root.setTop((menu = new ModToolkitMenu(this)));
		
		tabs = new TabPane();
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		Tab home = new Tab("Home");
		home.setContent(new HomePane());
		Tab mods = new Tab("Mods");
		this.mods = new ModsPane(this);
		mods.setContent(this.mods);
		tabs.getTabs().addAll(home, mods);
		root.setCenter(tabs);
		
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
		stage.centerOnScreen();
		
		log.info("Loading files");
		try 
		{
			data.loadFiles();
		} 
		catch (IOException e)
		{
			log.log(Level.SEVERE, "Failed to load application data", e);
			MessageDialog.showErrorDialog(stage, "An error was "
				+ "encountered while loading application data: "
				+ Util.diagLogException(e), "Starbound Mod Toolkit - Error", e);
		}
		Path r = data.getMostRecentFile();
		if(r != null)dirChooser.setInitialDirectory(r.getParent().toFile());
		File f = data.getLastExportPath();
		if(f != null)menu.exportDialog.setInitialDirectory(f.getParentFile());
		
		stage.show();
		stage.setOnCloseRequest(this);
		log.info("Starbound Mod Toolkit started");
	}
	
	public static ModToolkit getInstance()
	{
		return instance;
	}
	
	public Stage getMainWindow()
	{
		return stage;
	}
	
	public void handle(WindowEvent event)
	{
		if(!requestClose())event.consume();
	}
	
	public boolean requestClose()
	{
		log.info("Close requested");
		try 
		{
			data.saveFiles();
		} 
		catch (IOException e)
		{
			log.log(Level.SEVERE, "Failed to save application data", e);
			MessageDialog.showErrorDialog(stage, "An error was "
				+ "encountered while saving application data: "
				+ Util.diagLogException(e), "Starbound Mod Toolkit - Error", e);
			return false;
		}
		Platform.exit();
		return true;
	}
}
