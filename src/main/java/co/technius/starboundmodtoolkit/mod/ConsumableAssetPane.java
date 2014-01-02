package co.technius.starboundmodtoolkit.mod;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import co.technius.starboundmodtoolkit.JsonConstants.Emote;
import co.technius.starboundmodtoolkit.mod.JsonObjectBinding.Type;

import com.eclipsesource.json.JsonValue;

public class ConsumableAssetPane extends ItemAssetPane
{
	@JsonObjectBinding(key = "emote", type = Type.STRING)
	private ComboBox<Emote> emote = new ComboBox<Emote>(FXCollections.observableArrayList(
		Emote.values()));
	
	public ConsumableAssetPane(JsonAsset asset) 
	{
		super(asset);
		form.add("Emote", emote);
	}
	
	@Override
	public void loadCustom()
	{
		super.loadCustom();
		JsonValue emoteVal = asset.object.get("emote");
		if(emoteVal != null && emoteVal.isString())
		{
			String emote = emoteVal.asString();
			for(Emote e: Emote.values())
			{
				if(e.toString().equals(emote))
					this.emote.getSelectionModel().select(e);
			}
		}
		else emote.getSelectionModel().clearSelection();
	}
}
