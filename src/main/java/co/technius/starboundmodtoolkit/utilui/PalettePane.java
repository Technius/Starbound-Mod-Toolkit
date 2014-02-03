package co.technius.starboundmodtoolkit.utilui;

import java.util.concurrent.Callable;

public class PalettePane extends SwappableNodePane<Palette>
{
	public PalettePane() 
	{
		super(new Callable<Palette>(){
			public Palette call()
			{
				return new Palette();
			}
		});
		setPrefix("Palette");
	}
}
