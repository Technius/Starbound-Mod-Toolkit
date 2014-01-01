package co.technius.starboundmodtoolkit.util;

import java.util.logging.Level;

public class ConsoleHandler extends java.util.logging.ConsoleHandler
{
	public ConsoleHandler()
	{
		setOutputStream(System.out);
		setLevel(Level.INFO);
	}
}
