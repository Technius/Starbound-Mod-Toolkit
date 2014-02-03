package co.technius.starboundmodtoolkit.mod.assetpane;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import co.technius.starboundmodtoolkit.GameVersion;
import co.technius.starboundmodtoolkit.JsonConstants.Race;
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
	
	public static ComboBox<Race> raceBox()
	{
		ComboBox<Race> race = 
				new ComboBox<Race>(FXCollections.observableArrayList(Race.values()));
		race.getSelectionModel().selectFirst();
		return race;
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
	
	public static ComboBox<String> gameVersionComboBox()
	{
		ComboBox<String> gameVersions = new ComboBox<String>(GameVersion.getVersionStrings());
		gameVersions.setValue(gameVersions.getItems().get(0));
		return gameVersions;
	}
	
	public static void updateSliderSize(int size, Slider slider)
	{
		slider.setMax(size);
		if(size <= 10)
		{
			if(size > 1)slider.setMajorTickUnit(size - 1);
			else slider.setMajorTickUnit(size);
			if(size > 2)slider.setMinorTickCount(size - 2);
			else slider.setMinorTickCount(0);
		}
		else
		{
			slider.setMajorTickUnit(10);
			slider.setMinorTickCount(9);
		}
	}
	
	public static void setupOneIndexSlider(Slider slider)
	{
		slider.setBlockIncrement(1);
		slider.setSnapToTicks(true);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
	}
}
