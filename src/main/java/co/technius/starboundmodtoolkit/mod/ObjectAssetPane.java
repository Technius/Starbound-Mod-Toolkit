package co.technius.starboundmodtoolkit.mod;

import javafx.geometry.VPos;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import co.technius.starboundmodtoolkit.JsonConstants.Rarity;
import co.technius.starboundmodtoolkit.mod.JsonObjectBinding.Type;

public class ObjectAssetPane extends JsonAssetPane
{
	@JsonObjectBinding(key = "objectName", type = Type.STRING, required = true)
	public TextField objectId = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "shortdescription", type = Type.STRING, required = true)
	public TextField objectName = AssetPaneUtils.noEmptyTextField(enable);
	
	@JsonObjectBinding(key = "description", type = Type.STRING)
	public TextArea description = AssetPaneUtils.noLinesTextArea();
	
	@JsonObjectBinding(key = "rarity", type = Type.STRING, required = true)
	public ComboBox<Rarity> rarity = AssetPaneUtils.rarityBox();
	
	@JsonObjectBinding(key = "printable", type = Type.BOOLEAN)
	public CheckBox printable = new CheckBox();
	
	@JsonObjectBinding(key = "price", type = Type.INTEGER)
	public TextField price = new TextField("0");
	
	public Accordion speciesDescriptions = new Accordion();
	
	@JsonObjectBinding(key = "apexDescription", type = Type.STRING)
	public TextArea apexDescription = AssetPaneUtils.noLinesTextArea();
	
	@JsonObjectBinding(key = "avianDescription", type = Type.STRING)
	public TextArea avianDescription = AssetPaneUtils.noLinesTextArea();
	
	@JsonObjectBinding(key = "floranDescription", type = Type.STRING)
	public TextArea floranDescription = AssetPaneUtils.noLinesTextArea();
	
	@JsonObjectBinding(key = "glitchDescription", type = Type.STRING)
	public TextArea glitchDescription = AssetPaneUtils.noLinesTextArea();
	
	@JsonObjectBinding(key = "humanDescription", type = Type.STRING)
	public TextArea humanDescription = AssetPaneUtils.noLinesTextArea();
	
	@JsonObjectBinding(key = "hyotlDescription", type = Type.STRING)
	public TextArea hylotlDescription = AssetPaneUtils.noLinesTextArea();
	
	public ObjectAssetPane(JsonAsset asset)
	{
		super(asset);
		form.add("Object ID", objectId, new Label("Required"));
		form.add("Object Name", objectName, new Label("Required"));
		form.add("Rarity", rarity, new Label("Required"));
		form.add("Description", description);
		form.add("Printable", printable);
		Label priceValidity = new Label("Must be a number");
		AssetPaneUtils.addWholeNumberListener(priceValidity, price, enable).acceptNegative = false;
		form.add("Price", price);
		form.add(priceValidity);
		TitledPane[] speciesDescPanes = {
			new TitledPane("Apex Description", apexDescription),
			new TitledPane("Avian Description", avianDescription),
			new TitledPane("Floran Description", floranDescription),
			new TitledPane("Glitch Description", glitchDescription),
			new TitledPane("Human Description", humanDescription),
			new TitledPane("Hylotl Description", hylotlDescription)
		};
		speciesDescriptions.getPanes().addAll(speciesDescPanes);
		Label d = new Label("Inspection Descriptions");
		form.add(d, speciesDescriptions);
		GridPane.setValignment(d, VPos.TOP);
	}
}
