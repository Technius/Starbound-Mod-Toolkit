package co.technius.starboundmodtoolkit.mod;

import java.io.IOException;
import java.nio.file.Path;

import com.eclipsesource.json.JsonObject;

public class Asset 
{
	protected Path path;
	protected Path source;
	AssetType type;
	public Asset(Path path)
	{
		this.path = path;
		String fname = path.getFileName().toString();
		if(fname.contains("."))
		{
			type = AssetType.getByFileExtension(
					fname.substring(fname.lastIndexOf(".")));
			if(type == null)type = AssetType.OTHER;
		}
	}
	
	public Asset(Path path, AssetType type)
	{
		this.path = path;
		this.type = type;
	}
	
	public AssetType getType()
	{
		return type;
	}
	
	public String toString()
	{
		return path.toString();
	}
	
	public final void load() throws IOException
	{
		load(source.resolve(path));
	}
	
	public final void save() throws IOException
	{
		save(source.resolve(path));
	}
	
	public void saveWithSource(Path path) throws IOException
	{
		save(path.resolve(this.path));
	}
	
	public void loadWithSource(Path path) throws IOException
	{
		load(path.resolve(this.path));
	}
	
	public void save(Path path) throws IOException
	{
		
	}
	
	public void load(Path path) throws IOException
	{
		
	}
	
	public boolean isLoaded()
	{
		return true;
	}
	
	public Path getPath()
	{
		return path;
	}
	
	public Path getSource()
	{
		return source;
	}
	
	public static Asset createAsset(Path path, AssetType type, Path source)
	{
		Asset a;
		if(type.isJsonAsset())
		{
			JsonAsset b = new JsonAsset(path, type);
			b.object = new JsonObject();
			a = b;
		}
		else a = new Asset(path, type);
		a.source = source;
		return a;
	}
}
