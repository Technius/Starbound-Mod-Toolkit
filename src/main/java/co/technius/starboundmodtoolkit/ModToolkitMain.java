package co.technius.starboundmodtoolkit;

import javax.swing.JOptionPane;

public class ModToolkitMain 
{
	public static void main(String[] args)
	{
		try
		{
			Class.forName("javafx.application.Application");
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
				"JavaFX cannot be found in your Java installation!" +
					System.getProperty("line.separator") + "Please make sure you" +
					" are you Java 7 Update 6 or higher.", 
				"Starbound Mod Toolkit - Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		ModToolkit.init(args);
	}
}
