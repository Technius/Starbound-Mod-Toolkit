package co.technius.starboundmodtoolkit.utilui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MessageDialog extends ModalDialog implements EventHandler<ActionEvent>
{
	public MessageDialog(Window parent, String message)
	{
		this(parent, message, null);
	}
	
	public MessageDialog(Window parent, String message, String title)
	{
		super(parent, title);
		if(title == null && parent instanceof Stage)
			setTitle(((Stage)parent).getTitle());
		Label l = new Label(message);
		l.setWrapText(true);
		Button button = new Button("Close");
		button.setAlignment(Pos.BOTTOM_CENTER);
		box.getChildren().addAll(l, button);
		button.setOnAction(this);
		init();
	}

	public void handle(ActionEvent event)
	{
		close();
	}
}
