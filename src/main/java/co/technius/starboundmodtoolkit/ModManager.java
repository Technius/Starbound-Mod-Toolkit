package co.technius.starboundmodtoolkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import co.technius.starboundmodtoolkit.mod.Asset;
import co.technius.starboundmodtoolkit.mod.Mod;
import co.technius.starboundmodtoolkit.mod.ModInfo;

import com.eclipsesource.json.JsonObject;

public class ModManager 
{
	public Mod loadMod(Path path, Path modinfo) throws IOException
	{
		if(path == null || modinfo == null)return null;
		JsonObject object = Util.readJsonFromFile(modinfo);
		ModInfo info;
		try
		{
			info = new ModInfo(object);
		}
		catch(IllegalArgumentException iae)
		{
			return null;
		}
		Mod mod = new Mod(info);
		mod.setSourceFolder(path);
		ModToolkit.log.info("Mod loaded: " + path.toAbsolutePath().toString());
		return mod;
	}
	
	public void saveMod(Path path, Mod mod) throws IOException
	{
		Path dir = path == null ? mod.getSourceFolder() : path;
		if(dir == null)
			throw new IllegalArgumentException("No path was provided");
		if(Files.notExists(path))
			Files.createDirectory(dir);
		ModInfo info = mod.getInfo();
		Util.writeJsonToFile(info.toJson(), dir.resolve(info.getName() + ".modinfo"));
		Path assetPath = mod.getAssetFolder();
		for(Asset a: mod.getAssets())
		{
			if(a.getClass() != Asset.class)
			{
				if(!a.isLoaded())a.load();
				a.saveWithSource(assetPath);
			}
			else if(a.getSource() != null)
			{
				Path n = assetPath.resolve(a.getPath());
				Files.createDirectories(n.getParent());
				Files.copy(a.getSource(), n);
			}
		}
		ModToolkit.log.info("Mod saved: " + path.toAbsolutePath().toString());
	}
}
