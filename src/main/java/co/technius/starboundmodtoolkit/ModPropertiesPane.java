package co.technius.starboundmodtoolkit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class ModPropertiesPane extends GridPane
{
	TextField name = new TextField();
	ComboBox<String> gameVersions = new ComboBox<String>(GameVersion.getVersionStrings());
	TextField path = new TextField(".");
	Accordion acc = new Accordion();
	public ModPropertiesPane()
	{
		setPadding(new Insets(10, 10, 10, 10));
		setVgap(5);
		setHgap(5);
		setAlignment(Pos.TOP_LEFT);
		Label lname = new Label("Mod Name");
		GridPane.setConstraints(lname, 0, 0);
		getChildren().add(lname);
		name.setPromptText("Name of the mod");
		GridPane.setConstraints(name, 1, 0);
		getChildren().add(name);
		Label lversions = new Label("Game Version");
		GridPane.setConstraints(lversions, 0, 1);
		getChildren().add(lversions);
		gameVersions.setValue(gameVersions.getItems().get(0));
		GridPane.setConstraints(gameVersions, 1, 1);
		getChildren().add(gameVersions);
		GridPane advp = new GridPane();
		advp.setPadding(new Insets(10, 10, 10, 10));
		advp.setVgap(5);
		advp.setHgap(5);
		Label lpath = new Label("Path");
		GridPane.setConstraints(lpath, 0, 0);
		path.setPromptText("Path to assets");
		GridPane.setConstraints(path, 1, 0);
		advp.getChildren().addAll(lpath, path);
		TitledPane accpane = new TitledPane("Advanced", advp);
		acc.getPanes().add(accpane);
		GridPane.setConstraints(acc, 0, 2, 2, 1);
		getChildren().add(acc);
	}
}
