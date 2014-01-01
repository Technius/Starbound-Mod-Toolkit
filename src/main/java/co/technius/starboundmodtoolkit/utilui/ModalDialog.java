package co.technius.starboundmodtoolkit.utilui;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class ModalDialog extends Stage
{
	protected VBox box = new VBox();
	public ModalDialog(Window parent)
	{
		this(parent, null);
	}
	
	public ModalDialog(Window parent, String title)
	{
		initOwner(parent);
		initModality(Modality.APPLICATION_MODAL);
		setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/2);
		if(title != null)setTitle(title);
		else if(parent instanceof Stage)setTitle(((Stage)parent).getTitle());
		box.setSpacing(5);
		box.setPadding(new Insets(10));
		box.setAlignment(Pos.TOP_CENTER);
	}
	
	protected final void init()
	{
		Scene scene = new Scene(box);
		setScene(scene);
		centerOnScreen();
		sizeToScene();
	}
	
	public static MessageDialog showMessageDialog(Window parent, String message)
	{
		return showMessageDialog(parent, message, null);
	}
	
	public static MessageDialog showMessageDialog(Window parent, String message, String title)
	{
		MessageDialog m = new MessageDialog(parent, message, title);
		m.show();
		return m;
	}
	
	public static MessageDialog showErrorDialog(Window parent, String message, String title,
		Throwable e)
	{
		MessageDialog m = new MessageDialog(parent, message, title);
		StringWriter w = new StringWriter();
		e.printStackTrace(new PrintWriter(w));
		TextArea etext = new TextArea(w.toString());
		etext.setEditable(false);
		TitledPane pane = new TitledPane("View full error log", etext);
		Accordion acc = new Accordion();
		acc.getPanes().add(pane);
		AccordionSizeHack.add(acc, m);
		m.box.getChildren().add(acc);
		m.show();
		return m;
	}
}
