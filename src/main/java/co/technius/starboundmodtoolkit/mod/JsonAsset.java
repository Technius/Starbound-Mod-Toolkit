package co.technius.starboundmodtoolkit.mod;

import java.io.IOException;
import java.nio.file.Path;

import co.technius.starboundmodtoolkit.Util;

import com.eclipsesource.json.JsonObject;

public class JsonAsset extends Asset
{
	public JsonAsset(Path path) 
	{
		super(path);
	}
	
	JsonAsset(Path path, AssetType type)
	{
		super(path, type);
	}
	
	JsonObject object;
	
	@Override
	public void load(Path path) throws IOException
	{
		assert source != null;
		object = Util.readJsonFromFile(path);
		assert object != null;
	}
	
	@Override
	public void save(Path path) throws IOException
	{
		checkAsset();
		assert source != null;
		Util.writeJsonToFile(object,path);
	}
	
	public JsonObject getObject()
	{
		return object;
	}
	
	protected void checkAsset()
	{
		if(object == null)
			throw new IllegalStateException("Asset is not loaded");
	}
	
	@Override
	public boolean isLoaded()
	{
		return object != null;
	}
}
