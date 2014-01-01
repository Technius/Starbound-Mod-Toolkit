package co.technius.starboundmodtoolkit.utilui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import co.technius.starboundmodtoolkit.util.AndBoolean;
import co.technius.starboundmodtoolkit.util.SimpleObservable;

public class TextFieldValidityListener implements ChangeListener<String>, SimpleObservable<Boolean>
{
	Label label;
	private AndBoolean andBool;
	private boolean valid = true;
	public TextFieldValidityListener(Label label, AndBoolean andBool)
	{
		this.label = label;
		this.andBool = andBool;
		if(label != null)
		{
			label.setTextFill(Paint.valueOf("red"));
			label.setVisible(false);
		}
	}
	
	public final void changed(ObservableValue<? extends String> observable,
			String oldValue, String newValue) 
	{
		valid = doChanged(observable, oldValue, newValue);
		if(label != null)label.setVisible(!valid);
		andBool.update();
	}
	
	public boolean doChanged(ObservableValue<? extends String> observable,
			String oldValue, String newValue)
	{
		return true;
	}

	public final Boolean getValue() 
	{
		return valid;
	}
}
