package co.technius.starboundmodtoolkit.mod;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import co.technius.starboundmodtoolkit.Util;

public class AssetIndexer extends SimpleFileVisitor<Path>
{
	public Path src;
	public ObservableList<Path> paths = FXCollections.observableArrayList();
	public ObservableList<Asset> assets = FXCollections.observableArrayList();
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
	{
		if(file.getFileName().toString().endsWith(".modinfo"))
			return FileVisitResult.CONTINUE;
		try
		{
			if(paths.contains(file))
				return FileVisitResult.CONTINUE;
			if(src != null)
				file = src.relativize(file);
			indexAsset(file).source = src;
			return super.visitFile(file, attrs);
		}
		catch (Throwable t) 
		{
			Util.handleError(t, "An error occurred while indexing assets", "Failed to index assets");
		}
		return FileVisitResult.CONTINUE;
	}
	
	public Asset indexAsset(Path file) throws IOException
	{
		Asset asset;
		asset = getAsset(file);
		assert asset != null;
		assets.add(asset);
		paths.add(file);
		return asset;
	}
	
	private Asset getAsset(Path file) throws IOException
	{
		String fname = file.getFileName().toString();
		for(AssetType t: AssetType.values())
		{
			if(t.isOfType(fname))
			{
				if(t.isJsonAsset())return new JsonAsset(file, t);
				else return new Asset(file, t);
			}
		}
		return new Asset(file, AssetType.OTHER);
	}
}