package co.technius.starboundmodtoolkit.utilui;

import java.util.concurrent.Callable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import co.technius.starboundmodtoolkit.mod.assetpane.AssetPaneUtils;

public class SwappableNodePane<T extends Node> extends BorderPane implements EventHandler<ActionEvent>,
	ChangeListener<Number>
{
	StackPane panels = new StackPane();
	Slider slider = new Slider(1, 1, 1);
	Label label = new Label("Panel 1 of 1");
	Button next = new Button("Next");
	Button prev = new Button("Prev");
	Button first = new Button("First");
	Button last = new Button("Last");
	Button delete = new Button("Delete");
	Button newButton = new Button("New");
	String prefix = "Panel";
	Callable<T> factory;
	
	public SwappableNodePane(Callable<T> factory)
	{
		this.factory = factory;
		AssetPaneUtils.setupOneIndexSlider(slider);
		panels.getChildren().add(createNew());
		setCenter(panels);
		HBox buttons = new HBox();
		buttons.setSpacing(5);
		buttons.getChildren().addAll(first, next, slider, prev, last, newButton, delete);
		setBottom(buttons);
		
		first.setOnAction(this);
		next.setOnAction(this);
		prev.setOnAction(this);
		last.setOnAction(this);
		newButton.setOnAction(this);
		delete.setOnAction(this);
		slider.valueProperty().addListener(this);
	}
	
	public void reset()
	{
		slider.setValue(1);
		updateSliderSize();
		selectPage(1);
	}
	
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
	
	public String getPrefix()
	{
		return prefix;
	}
	
	private T createNew()
	{
		try 
		{
			return factory.call();
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T getPage(int page)
	{
		return (T) panels.getChildren().get(page - 1);
	}
	
	public ObservableList<Node> getPages()
	{
		return panels.getChildren();
	}
	
	public Label getLabel()
	{
		return label;
	}
	
	public void selectPage(int pagenum)
	{
		int psize = panels.getChildren().size();
		if(pagenum < 1 || pagenum > psize)
			return;
		int i = pagenum - 1;
		for(Node n: panels.getChildren())n.setVisible(false);
		panels.getChildren().get(i).setVisible(true);
		label.setText(prefix + " " + pagenum + " of " + psize);
		slider.setValue(pagenum);
	}
	
	public void handle(ActionEvent e)
	{
		int p = (int) slider.getValue();
		if(e.getSource() == first)selectPage(1);
		else if(e.getSource() == next)
		{
			if(p != panels.getChildren().size())selectPage(p + 1);
		}
		else if(e.getSource() == prev)
		{
			if(p != 1)selectPage(p - 1);
		}
		else if(e.getSource() == last)
			selectPage(panels.getChildren().size());
		else if(e.getSource() == newButton)
		{
			panels.getChildren().add(p, createNew());
			updateSliderSize();
			selectPage(p + 1);
		}
		else if(e.getSource() == delete && panels.getChildren().size() != 1)
		{
			panels.getChildren().remove(p - 1);
			updateSliderSize();
			if(p == 1)selectPage(1);
			else if(p == panels.getChildren().size())selectPage(p - 1);
			else selectPage(p);
		}
	}
	
	private void updateSliderSize()
	{
		AssetPaneUtils.updateSliderSize(panels.getChildren().size(), slider);
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable,
			Number oldValue, Number newValue) 
	{
		selectPage(newValue.intValue());
	}
}
