package co.technius.starboundmodtoolkit.mod;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import co.technius.starboundmodtoolkit.JsonConstants.Rarity;
import co.technius.starboundmodtoolkit.util.AndBoolean;
import co.technius.starboundmodtoolkit.utilui.EmptyTextFieldListener;
import co.technius.starboundmodtoolkit.utilui.IntegerTextFieldListener;

public class AssetPaneUtils 
{
	public static ComboBox<Rarity> rarityBox()
	{
		ComboBox<Rarity> rarity = 
				new ComboBox<Rarity>(FXCollections.observableArrayList(Rarity.values()));
		rarity.getSelectionModel().selectFirst();
		return rarity;
	}
	
	public static TextArea noLinesTextArea()
	{
		TextArea t = new TextArea();
		t.setWrapText(true);
		return t;
	}
	
	public static TextField noEmptyTextField(AndBoolean bool)
	{
		TextField f = new TextField();
		EmptyTextFieldListener l = new EmptyTextFieldListener(null, bool);
		f.textProperty().addListener(l);
		bool.register(l);
		return f;
	}
	
	public static IntegerTextFieldListener addWholeNumberListener(
		Label vLabel, TextInputControl c, AndBoolean bool)
	{
		IntegerTextFieldListener iV = new IntegerTextFieldListener(vLabel, bool);
		if(bool != null)bool.o.add(iV);
		iV.acceptNegative = false;
		bool.register(iV);
		c.textProperty().addListener(iV);
		return iV;
	}
}
