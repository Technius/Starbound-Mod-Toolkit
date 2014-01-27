package co.technius.starboundmodtoolkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import co.technius.starboundmodtoolkit.mod.AssetType;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class DataFiles 
{
	private static final FileSystem fsys = FileSystems.getDefault();
	JsonObject baseData = new JsonObject();
	JsonArray recentFiles;
	HashMap<AssetType, JsonObject> jsonTemplates = new HashMap<AssetType, JsonObject>();
	public DataFiles()
	{
		try
		{
			JsonObject o = JsonObject.readFrom(new InputStreamReader(
				getClass().getResourceAsStream("/metadata.json")));
			ModToolkit.version = o.get("version").asString();
			assert ModToolkit.version != null;
		}
		catch(Throwable t)
		{
			throw new RuntimeException("Failed to determine version", t);
		}
		validateDataTree();
		for(AssetType a: AssetType.getJsonAssets())
		{
			InputStream s = getClass().getResourceAsStream("/assettemplates/" + a.name() + ".json");
			if(s != null)
			{
				try 
				{
					JsonObject o = JsonObject.readFrom(new InputStreamReader(s));
					jsonTemplates.put(a, JsonObject.unmodifiableObject(o));
				} 
				catch (IOException e)
				{
					throw new RuntimeException("Failed to read template for asset type "
						+ a.name(),e);
				}
			}
		}
	}
	
	public void loadFiles() throws IOException
	{
		Path savePath = getSavePath();
		if(Files.notExists(savePath))return;
		Path dataPath = savePath.resolve("data.json");
		if(Files.exists(dataPath))baseData = Util.readJsonFromFile(dataPath);
		validateDataTree();
	}
	
	public void saveFiles() throws IOException
	{
		Path savePath = getSavePath();
		Files.createDirectories(savePath);
		Path dataPath = savePath.resolve("data.json");
		Util.writeJsonToFile(baseData, dataPath);
	}
	
	public void addRecentFile(Path path)
	{
		String s = path.toAbsolutePath().toString();
		for(JsonValue v: recentFiles)
		{
			if(v.asString().equals(s))return;
		}
		JsonValue val = JsonValue.valueOf(s);
		if(recentFiles.size() == 10)
		{
			for(int i = 0; i < 9; i ++)
				recentFiles.set(i + 1, recentFiles.get(i));
			recentFiles.set(0, val);
		}
		else recentFiles.add(val);
	}
	
	public File getLastExportPath()
	{
		JsonValue v = baseData.get("lastExport");
		if(v == null)return null;
		return new File(v.asString());
	}
	
	public void setLastExportPath(Path path)
	{
		baseData.set("lastExport", path.toAbsolutePath().toString());
	}
	
	public Path getMostRecentFile()
	{
		return recentFiles.size() > 0 ? fsys.getPath(recentFiles.get(0).asString()) : null;
	}
	
	public static Path getSavePath()
	{
		String osname = System.getProperty("os.name").toLowerCase();
		//String fs = File.separator;
		String userHome = System.getProperty("user.home");
		String fname = ".StarboundModificationToolkit";
		String loc;
		if(osname.startsWith("windows"))
			loc = System.getenv("APPDATA");
		else if(osname.startsWith("mac"))
			loc = "/Library/Application Support";
		else loc = userHome;
		return fsys.getPath(loc, fname);
	}
	
	private void validateDataTree()
	{
		if(baseData.get("recentFiles") == null)
			baseData.set("recentFiles", new JsonArray());
		updateDataTree();
	}
	
	private void updateDataTree()
	{
		recentFiles = baseData.get("recentFiles").asArray();
	}
	
	public JsonObject getJsonTemplate(AssetType type)
	{
		return jsonTemplates.get(type);
	}
}
