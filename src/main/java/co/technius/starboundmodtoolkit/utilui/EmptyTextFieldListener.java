package co.technius.starboundmodtoolkit.utilui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import co.technius.starboundmodtoolkit.util.AndBoolean;

public class EmptyTextFieldListener extends TextFieldValidityListener
{

	public EmptyTextFieldListener(Label label, AndBoolean andBool) 
	{
		super(label, andBool);
	}
	
	@Override
	public boolean doChanged(ObservableValue<? extends String> observable,
			String oldValue, String newValue)
	{
		if(newValue == null || newValue.trim().isEmpty())
			return false;
		return true;
	}
}
