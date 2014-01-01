package co.technius.starboundmodtoolkit.mod;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ModInfo 
{
	protected JsonObject json = new JsonObject();
	
	public ModInfo(String name, String version)
	{
		json.set("name", name);
		json.set("version", version);
		json.set("path", ".");
		json.set("dependencies", new JsonArray());
	}
	
	public ModInfo(JsonObject object)
	{
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
		json = object;
	}
	
	public ArrayList<String> getDependencies()
	{
		ArrayList<String> dep = new ArrayList<String>();
		for(JsonValue v: json.get("dependencies").asArray().values())dep.add(v.asString());
		return dep;
	}
	
	public JsonObject toJson()
	{
		return json;
	}
	
	public String getName()
	{
		return json.get("name").asString();
	}
	
	public String getGameVersion()
	{
		return json.get("version").asString();
	}
	
	public Path getAssetPath()
	{
		String path = json.get("path").asString();
		path = path.replaceAll("[/\\\\\\.]", Matcher.quoteReplacement(File.separator));
		return FileSystems.getDefault().getPath(path);
	}
	
	public void setName(String name)
	{
		json.set("name", name);
	}
	
	public void setGameVersion(String version)
	{
		json.set("version", version);
	}

	public void setAssetPath(String path)
	{
		json.set("path", path);
	}
}
