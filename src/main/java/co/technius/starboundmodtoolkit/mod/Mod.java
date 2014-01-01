package co.technius.starboundmodtoolkit.mod;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import javafx.collections.ObservableList;
import co.technius.starboundmodtoolkit.Util;

public class Mod 
{
	private ModInfo info;
	private Path sourceFolder;
	private AssetIndexer assetIndexer = new AssetIndexer();
	
	public Mod(ModInfo info)
	{
		this.info = info;
	}
	
	public Mod(String name, String version)
	{
		info = new ModInfo(name, version);
	}
	
	public String getName()
	{
		return info.getName();
	}
	
	public ModInfo getInfo()
	{
		return info;
	}
	
	public Path getSourceFolder()
	{
		return sourceFolder;
	}
	
	public void setSourceFolder(Path src)
	{
		sourceFolder = src;
	}
	
	public boolean updateAssets()
	{
		if(sourceFolder == null)return false;
		try 
		{
			Path assetFolder = getAssetFolder();
			Util.findAssets(assetFolder, assetIndexer);
		}
		catch (Throwable t) 
		{
			Util.handleError(t, "An error ocurred while indexing the assets for " + getName()
				, "Failed to index assets");
			return false;
		}
		return true;
	}
	
	public boolean updateAsset(Path p)
	{
		if(sourceFolder == null)return false;
		try 
		{
			Path assetFolder = getAssetFolder();
			Path path = assetFolder.resolve(p);
			assetIndexer.visitFile(p, Files.readAttributes(path, BasicFileAttributes.class));
		}
		catch (Throwable t) 
		{
			Util.handleError(t, "An error ocurred while indexing an asset for " + getName()
				, "Failed to index asset");
			return false;
		}
		return true;
	}
	
	public ObservableList<Asset> getAssets()
	{
		return assetIndexer.assets;
	}
	
	public Asset getAsset(String name) throws IOException
	{
		Path p = getAssetFolder().resolve(name);
		for(Asset a: assetIndexer.assets)
		{
			if(Files.isSameFile(p, a.path))
				return a;
		}
		return null;
	}
	
	public int getAssetCount()
	{
		return assetIndexer.assets.size();
	}

	public Path getAssetFolder()
	{
		return FileSystems.getDefault().getPath(sourceFolder.toAbsolutePath().toString(), 
			info.getAssetPath().toString());
	}
}
