package co.technius.starboundmodtoolkit.mod;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ModInfo extends JsonAsset
{
	public ModInfo(String name, String version)
	{
		super(Paths.get(name + ".modinfo"), AssetType.JSON);
		object = new JsonObject();
		object.set("name", name);
		object.set("version", version);
		object.set("path", ".");
		object.set("dependencies", new JsonArray());
	}
	
	public ModInfo(Path path, JsonObject object)
	{
		super(path, AssetType.JSON);
		this.object = object;
		boolean pass = true;
		try
		{
			object.get("name").asString();
			object.get("version").asString();
			if(object.get("path") == null)object.set("path", ".");
			object.get("path").asString();
			if(object.get("dependencies") == null)
				object.set("dependencies", new JsonArray());
			JsonArray a = object.get("dependencies").asArray();
			for(JsonValue v: a)
			{
				if(!v.isString())
				{
					pass = false;
					break;
				}
			}
		}
		catch(NullPointerException | UnsupportedOperationException e)
		{
			pass = false;
		}
		if(!pass)
			throw new IllegalArgumentException("Illegal modinfo JSON object");
	}
	
	public ArrayList<String> getDependencies()
	{
		ArrayList<String> dep = new ArrayList<String>();
		for(JsonValue v: object.get("dependencies").asArray().values())dep.add(v.asString());
		return dep;
	}
	
	public String getName()
	{
		return object.get("name").asString();
	}
	
	public String getGameVersion()
	{
		return object.get("version").asString();
	}
	
	public Path getAssetPath()
	{
		String path = object.get("path").asString();
		path = path.replaceAll("[/\\\\\\.]", Matcher.quoteReplacement(File.separator));
		return FileSystems.getDefault().getPath(path);
	}
	
	public void setName(String name)
	{
		object.set("name", name);
	}
	
	public void setGameVersion(String version)
	{
		object.set("version", version);
	}

	public void setAssetPath(String path)
	{
		object.set("path", path);
	}
	
	public void setSource(Path path)
	{
		source = path;
	}
}
