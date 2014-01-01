package co.technius.starboundmodtoolkit.mod;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import co.technius.starboundmodtoolkit.ModToolkit;
import co.technius.starboundmodtoolkit.mod.JsonObjectBinding.Type;
import co.technius.starboundmodtoolkit.utilui.FormPane;

public class RecipeAssetPane extends JsonAssetPane
{
	@SuppressWarnings("rawtypes")
	@JsonObjectBinding(key = "input", type = Type.ARRAY,
		keyBinding = {"item", "count"},
		valueBinding = {Type.STRING, Type.INTEGER})
	TableView<ObservableMap> input;
	@SuppressWarnings("rawtypes")
	ObservableList<ObservableMap> items = FXCollections.observableArrayList();
	
	@JsonObjectBinding(base = {"output"}, key = "item", type = Type.STRING, required = true)
	TextField outputItem = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(base = {"output"}, key = "count", type = Type.INTEGER, required = true)
	TextField outputAmount = new TextField();
	
	TextField addIdField = new TextField();
	TextField addAmountField = new TextField();
	Button addButton = new Button("Add Ingredient");
	Button deleteButton = new Button("Delete Ingredient");
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RecipeAssetPane(JsonAsset asset) 
	{
		super(asset);
		input = new TableView<ObservableMap>(items);
		FormPane inputForm = new FormPane();
		
		TableColumn<ObservableMap, String> idCol = new TableColumn<ObservableMap, String>("Item IDs");
		TableColumn<ObservableMap, Integer> amountCol = 
				new TableColumn<ObservableMap, Integer>("Amount");
		idCol.setCellValueFactory(new MapValueFactory("item"));
		amountCol.setCellValueFactory(new MapValueFactory("count"));
		
		input.getColumns().setAll(idCol, amountCol);
		input.setEditable(true);
		input.getSelectionModel().setCellSelectionEnabled(true);
		
		Callback<TableColumn<ObservableMap, String>, TableCell<ObservableMap, String>> idFactory =
			new Callback<TableColumn<ObservableMap, String>, TableCell<ObservableMap, String>> () {
				public TableCell<ObservableMap, String> call(TableColumn<ObservableMap, String> arg0) 
				{
					return new IdTableCell();
				}
		};
		Callback<TableColumn<ObservableMap, Integer>, TableCell<ObservableMap, Integer>> amountFactory =
			new Callback<TableColumn<ObservableMap, Integer>, TableCell<ObservableMap, Integer>> () {
				public TableCell<ObservableMap, Integer> call(TableColumn<ObservableMap, Integer> arg0) 
				{
					return new AmountTableCell();
				}
		};
		idCol.setCellFactory(idFactory);
		amountCol.setCellFactory(amountFactory);
		input.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		addIdField.setPromptText("Item ID");
		addAmountField.setPromptText("Amount");
		addButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) 
			{
				try
				{
					ObservableMap m = FXCollections.observableHashMap();
					ModToolkit.log.info(addIdField.getText() + "," + addAmountField.getText());
					m.put("item", addIdField.getText());
					m.put("count", Integer.parseInt(addAmountField.getText()));
					ModToolkit.log.info(m.get("item").toString());
					input.getItems().add(m);
					for(ObservableMap map:input.getItems())
					{
						ModToolkit.log.info(map.get("item") + "," + map.get("count"));
					}
				}
				catch(NumberFormatException nfe)
				{
					nfe.printStackTrace();
				}
			}
		});
		deleteButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event)
			{
				ObservableMap m = input.getSelectionModel().getSelectedItem();
				if(m != null)input.getItems().remove(m);
			}
		});
		
		inputForm.add(input);
		inputForm.add(addIdField, addAmountField, addButton);
		inputForm.add(deleteButton);
		
		AnchorPane inputWrapper = new AnchorPane();
		AnchorPane.setTopAnchor(input, 0d);
		AnchorPane.setLeftAnchor(input, 0d);
		AnchorPane.setRightAnchor(input, 0d);
		
		inputWrapper.getChildren().addAll(input);
		
		VBox iW2 = new VBox();
		iW2.getChildren().addAll(inputWrapper, inputForm);
		
		TitledPane inputPane = new TitledPane("Ingredients", iW2);		
		GridPane.setColumnSpan(inputPane, 4);
		GridPane.setHgrow(inputPane, Priority.ALWAYS);
		GridPane.setColumnSpan(input, 4);
		form.add(inputPane);
		inputPane.setMinSize(input.getPrefWidth(), input.getPrefHeight());
		//inputPane.autosize();
		
		form.add("Result", outputItem, new Label("Required"));
		form.add("Amount", outputAmount, new Label("Required"));
		
		Label warnLabel = new Label("");
		AssetPaneUtils.addWholeNumberListener(warnLabel, outputAmount, enable);
		form.add(warnLabel);
	}
	
	@SuppressWarnings("rawtypes")
	class IdTableCell extends TextFieldTableCell<ObservableMap, String>
	{
		public IdTableCell()
		{
			super(new StringConverter<String>() {
				public String toString(String arg0)
				{
					return arg0;
				}  
				public String fromString(String string) 
				{
					return string;
				}                       
			});
		}
		
		public void commitEdit(String val)
		{
			String nV = !val.trim().isEmpty() ? val.trim() : getItem();
			super.commitEdit(nV);
		}
	}
	
	@SuppressWarnings("rawtypes")
	class AmountTableCell extends TextFieldTableCell<ObservableMap, Integer>
	{
		public AmountTableCell()
		{
			super(new StringConverter<Integer>() {
				public String toString(Integer i)
				{
					if(i == null)return "";
					return i.toString();
				}  
				public Integer fromString(String string) 
				{
					try
					{
						return Integer.parseInt(string);
					}
					catch(NumberFormatException nfe)
					{
						return -1;
					}
				}
			});
		}
		
		public void commitEdit(Integer val)
		{
			Integer nV = val >= 0 ? val : getItem();
			super.commitEdit(nV);
		}
	}
}
