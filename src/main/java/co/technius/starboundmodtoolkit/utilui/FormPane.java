package co.technius.starboundmodtoolkit.utilui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class FormPane extends ScrollPane
{
	private GridPane grid = new GridPane();
	public FormPane()
	{
		setContent(grid);
		vbarPolicyProperty().set(ScrollBarPolicy.AS_NEEDED);
		setStyle("-fx-background-color:inherit;");
		grid.setHgap(5);
		grid.setVgap(5);
	}
	int rowc = 0;
	
	public void add(String label, Node... nodes)
	{
		if(label == null)
			throw new IllegalArgumentException("label is null");
		if(nodes == null)
			throw new IllegalArgumentException("node is null");
		Label l = new Label(label);
		GridPane.setConstraints(l, 0, rowc);
		for(int i = 0; i < nodes.length; i ++)
			GridPane.setConstraints(nodes[i], i + 1, rowc);
		GridPane.setHgrow(nodes[nodes.length - 1], Priority.SOMETIMES);
		grid.getChildren().add(l);
		grid.getChildren().addAll(nodes);
		rowc ++;
	}
	
	public void add(String label)
	{
		Label l = new Label(label);
		GridPane.setConstraints(l, 0, rowc);
		grid.getChildren().add(l);
		rowc ++;
	}
	
	public void add(Node... nodes)
	{
		for(int i = 0; i < nodes.length; i ++)
			GridPane.setConstraints(nodes[i], i, rowc);
		GridPane.setHgrow(nodes[nodes.length - 1], Priority.SOMETIMES);
		grid.getChildren().addAll(nodes);
		rowc ++;
	}
	
	public void clear()
	{
		grid.getChildren().clear();
	}
	
	public GridPane getGrid()
	{
		return grid;
	}
}
