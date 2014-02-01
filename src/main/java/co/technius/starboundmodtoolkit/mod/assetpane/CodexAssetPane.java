package co.technius.starboundmodtoolkit.mod.assetpane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import co.technius.starboundmodtoolkit.mod.JsonAsset;
import co.technius.starboundmodtoolkit.mod.assetpane.JsonObjectBinding.Type;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class CodexAssetPane extends JsonAssetPane implements EventHandler<ActionEvent>,
	ChangeListener<Number>
{
	@JsonObjectBinding(key = "id", type = Type.STRING, required = true)
	public TextField codexId = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "title", type = Type.STRING, required = true)
	public TextField title = AssetPaneUtils.noEmptyTextField(enable);
	
	BorderPane pagesPane = new BorderPane();
	StackPane pages = new StackPane();
	Slider pageSlider = new Slider(1, 1, 1);
	Label pageLabel = new Label("Page 1 of 1");
	Button next = new Button("Next");
	Button prev = new Button("Prev");
	Button first = new Button("First");
	Button last = new Button("Last");
	Button delete = new Button("Delete");
	Button newButton = new Button("New");
	
	public CodexAssetPane(JsonAsset asset)
	{
		super(asset);
		pageSlider.setBlockIncrement(1);
		pageSlider.setSnapToTicks(true);
		pageSlider.setShowTickMarks(true);
		pageSlider.setShowTickLabels(true);
		form.add(new Label("Codex ID"), codexId, new Label("Required"));
		form.add(new Label("Title"), title, new Label("Required"));
		form.add(pageLabel, pagesPane);
		pageLabel.setAlignment(Pos.TOP_LEFT);
		
		pages.getChildren().add(AssetPaneUtils.noLinesTextArea());
		pagesPane.setCenter(pages);
		HBox buttons = new HBox();
		buttons.setSpacing(5);
		buttons.getChildren().addAll(first, next, pageSlider, prev, last, newButton, delete);
		pagesPane.setBottom(buttons);
		
		first.setOnAction(this);
		next.setOnAction(this);
		prev.setOnAction(this);
		last.setOnAction(this);
		newButton.setOnAction(this);
		delete.setOnAction(this);
	}
	
	@Override
	public void loadCustom()
	{
		pages.getChildren().clear();
		JsonArray cpages = asset.getObject().get("contentPages").asArray();
		if(cpages.size() == 0)
			pages.getChildren().add(AssetPaneUtils.noLinesTextArea());
		else
		{
			boolean f = true;
			for(JsonValue v: cpages)
			{
				TextArea a = AssetPaneUtils.noLinesTextArea();
				a.setText(v.asString());
				pages.getChildren().add(a);
				if(f)f = false;
				else a.setVisible(false);
			}
		}
		pageSlider.valueProperty().addListener(this);
		pageSlider.setValue(1);
		updateSliderSize();
		selectPage(1);
	}
	
	@Override
	public void saveCustom()
	{
		JsonArray a = new JsonArray();
		for(Node n: pages.getChildren())
			a.add(((TextArea)n).getText());
		asset.getObject().set("contentPages", a);
	}
	
	private void selectPage(int pagenum)
	{
		int psize = pages.getChildren().size();
		if(pagenum < 1 || pagenum > psize)
			return;
		int i = pagenum - 1;
		for(Node n: pages.getChildren())n.setVisible(false);
		pages.getChildren().get(i).setVisible(true);
		pageLabel.setText("Page " + pagenum + " of " + psize);
		pageSlider.setValue(pagenum);
	}

	public void handle(ActionEvent e)
	{
		int p = (int) pageSlider.getValue();
		if(e.getSource() == first)selectPage(1);
		else if(e.getSource() == next)
		{
			if(p != pages.getChildren().size())selectPage(p + 1);
		}
		else if(e.getSource() == prev)
		{
			if(p != 1)selectPage(p - 1);
		}
		else if(e.getSource() == last)
			selectPage(pages.getChildren().size());
		else if(e.getSource() == newButton)
		{
			pages.getChildren().add(p, AssetPaneUtils.noLinesTextArea());
			updateSliderSize();
			selectPage(p + 1);
		}
		else if(e.getSource() == delete && pages.getChildren().size() != 1)
		{
			pages.getChildren().remove(p - 1);
			updateSliderSize();
			if(p == 1)selectPage(1);
			else if(p == pages.getChildren().size())selectPage(p - 1);
			else selectPage(p);
		}
	}
	
	private void updateSliderSize()
	{
		int size = pages.getChildren().size();
		pageSlider.setMax(size);
		if(size <= 10)
		{
			if(size > 1)pageSlider.setMajorTickUnit(size - 1);
			else pageSlider.setMajorTickUnit(size);
			if(size > 2)pageSlider.setMinorTickCount(size - 1);
			else pageSlider.setMinorTickCount(0);
		}
		else
		{
			pageSlider.setMajorTickUnit(10);
			pageSlider.setMinorTickCount(9);
		}
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable,
			Number oldValue, Number newValue) 
	{
		selectPage(newValue.intValue());
	}
}
