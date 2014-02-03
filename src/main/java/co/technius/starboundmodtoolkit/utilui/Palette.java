package co.technius.starboundmodtoolkit.utilui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class Palette extends BorderPane implements EventHandler<ActionEvent>
{
	Button add = new Button("Add color");
	Button remove = new Button("Remove selected");
	TableView<Colors> colors = new TableView<Colors>();
	ObservableList<Colors> pairs = FXCollections.observableArrayList();
	@SuppressWarnings("unchecked")
	public Palette()
	{
		colors.setItems(pairs);
		TableColumn<Colors, Color> oCol =
			new TableColumn<Colors, Color>("Original colors");
		TableColumn<Colors, Color> rCol = 
				new TableColumn<Colors, Color>("Replacement colors");
		Callback<TableColumn<Colors, Color>, TableCell<Colors, Color>> factory = 
			new Callback<TableColumn<Colors, Color>, TableCell<Colors, Color>>() {
				public TableCell<Colors, Color> call(TableColumn<Colors, Color> column) 
				{
					return new ColorTableCell();
				}
		};
		oCol.setCellFactory(factory);
		rCol.setCellFactory(factory);
		oCol.setCellValueFactory(new PropertyValueFactory<Colors, Color>("orig"));
		rCol.setCellValueFactory(new PropertyValueFactory<Colors, Color>("repl"));
		colors.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		colors.setEditable(true);
		colors.getColumns().addAll(oCol, rCol);
		setCenter(colors);
		HBox buttons = new HBox();
		buttons.getChildren().addAll(add, remove);
		setBottom(buttons);
		add.setOnAction(this);
		remove.setOnAction(this);
	}
	
	public void handle(ActionEvent e)
	{
		if(e.getSource() == add)
		{
			pairs.add(new Colors(Color.WHITE, Color.WHITE));
		}
		else if(e.getSource() == remove)
		{
			pairs.removeAll(colors.getSelectionModel().getSelectedItems());
			colors.getSelectionModel().clearSelection();
		}
	}
	
	static class ColorTableCell extends TableCell<Colors, Color>
	{
		ColorPicker c = new ColorPicker();
		public ColorTableCell()
		{
			c.getStyleClass().addAll("button");
			c.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent ev) 
				{
					getTableView().edit(getTableRow().getIndex(), getTableColumn());
					commitEdit(c.getValue());
				}
			});
		}
		public void updateItem(Color color, boolean empty)
		{
			if(color == null)return;
			setGraphic(c);
			c.setValue(color);
		}
	}
	
	public static class Colors
	{
		SimpleObjectProperty<Color> orig = new SimpleObjectProperty<Color>();
		SimpleObjectProperty<Color> repl = new SimpleObjectProperty<Color>();
		public Colors(){}
		public Colors(Color orig, Color repl)
		{
			this.orig.setValue(orig);
			this.repl.setValue(repl);
		}
		
		public Color getOrig()
		{
			return orig.get();
		}
		
		public void setOrig(Color val)
		{
			orig.set(val);
		}
		
		public Color getRepl()
		{
			return repl.get();
		}
		
		public void setRepl(Color val)
		{
			repl.set(val);
		}
	}
}
