package co.technius.starboundmodtoolkit.utilui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

public class AccordionSizeHack implements ChangeListener<TitledPane>
{
	private Stage stage;
	public AccordionSizeHack(Stage stage)
	{
		this.stage = stage;
	}

	public void changed(ObservableValue<? extends TitledPane> v,
			TitledPane old, TitledPane n)
	{
		stage.sizeToScene();
	}
	
	public static void add(Accordion acc, Stage stage)
	{
		acc.expandedPaneProperty().addListener(new AccordionSizeHack(stage));
	}
}
