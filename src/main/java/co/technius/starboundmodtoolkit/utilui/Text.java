package co.technius.starboundmodtoolkit.utilui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Text
{
	public static TextField coloredTextField(String color, String text)
	{
		TextField f = new TextField(text);
		f.setStyle("-fx-color:" + color + ";");
		return f;
	}
	
	public static TextField coloredTextField(String color)
	{
		return coloredTextField(color, "");
	}
	
	public static Label coloredLabel(String color, String text)
	{
		Label l = new Label(text);
		l.setStyle("-fx-color:" + color + ";");
		return l;
	}
	
	public static Label coloredLabel(String color)
	{
		return coloredLabel(color, "");
	}
}
