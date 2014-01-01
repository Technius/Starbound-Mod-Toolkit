package co.technius.starboundmodtoolkit.util;

import java.util.ArrayList;

import javafx.beans.property.SimpleBooleanProperty;

public class AndBoolean extends SimpleBooleanProperty
{
	public ArrayList<SimpleObservable<Boolean>> o = new ArrayList<SimpleObservable<Boolean>>();
	public void update()
	{
		for(SimpleObservable<Boolean> s: o)
		{
			if(!s.getValue())
			{
				set(false);
				return;
			}
		}
		set(true);
	}
	
	@SafeVarargs
	public final void registerAll(SimpleObservable<Boolean>... s)
	{
		for(SimpleObservable<Boolean> o: s)
		{
			this.o.add(o);
		}
	}
	
	public void register(SimpleObservable<Boolean> s)
	{
		this.o.add(s);
	}
}
