package co.technius.starboundmodtoolkit;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomePane extends VBox
{
	public HomePane()
	{
		getChildren().add(new Label("Welcome to Starbound Mod Toolkit!"));
		getChildren().add(new Label("To get started, either create a new mod"
			+ " or load an existing mod."));
	}
}
