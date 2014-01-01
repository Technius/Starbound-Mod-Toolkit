package co.technius.starboundmodtoolkit.utilui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import co.technius.starboundmodtoolkit.util.AndBoolean;

public class IntegerTextFieldListener extends TextFieldValidityListener
{
	public boolean acceptNegative = true;
	public boolean acceptPositive = true;
	public IntegerTextFieldListener(Label label, AndBoolean andBool)
	{
		super(label, andBool);
		if(label != null)label.setText("Must be an integer");
	}
	
	@Override
	public boolean doChanged(ObservableValue<? extends String> observable,
			String oldValue, String newValue) 
	{
		int i = 0;
		try
		{
			i = Integer.parseInt(newValue);
		}
		catch(NumberFormatException nfe)
		{
			label.setText("Must be an integer");
			return false;
		}
		
		if(!acceptNegative && i < 0)
		{
			if(label != null)label.setText("Cannot be negative");
			return false;
		}
		if(!acceptPositive && i > 0)
		{
			if(label != null)label.setText("Cannot be positive");
			return false;
		}
		
		return true;
	}
}
