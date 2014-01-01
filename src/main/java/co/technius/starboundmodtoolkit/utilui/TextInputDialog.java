package co.technius.starboundmodtoolkit.utilui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

public class TextInputDialog extends ModalDialog implements EventHandler<ActionEvent>
{
	String query = null;
	boolean accept = false;
	TextField field = new TextField();
	Button confirm = new Button("Confirm");
	Button cancel = new Button("Cancel");
	public TextInputDialog(Window parent, String title) 
	{
		super(parent, title);
		HBox btn = new HBox();
		btn.setSpacing(5);
		btn.getChildren().addAll(confirm, cancel);
		box.getChildren().addAll(field, btn);
		confirm.setOnAction(this);
		cancel.setOnAction(this);
		init();
	}
	
	public String getResponse()
	{
		showAndWait();
		String query = this.query;
		this.query = null;
		return query;
	}
	
	public void close()
	{
		super.close();
		if(!accept)query = null;
	}

	public void handle(ActionEvent event)
	{
		if(event.getSource() == confirm)
		{
			query = field.getText();
			accept = true;
		}
		close();
	}
}
