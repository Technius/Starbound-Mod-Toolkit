package co.technius.starboundmodtoolkit.mod;

import java.util.ArrayList;

public enum AssetType
{
	OBJECT("object", true),
	ITEM("item", true),
	MATERIAL_ITEM("matitem", true),
	MATERIAL("material", true),
	MATERIAL_MODIFIER("matmod", true),
	RECIPE("recipe", true),
	ANIMATION("animation", true),
	FRAMES("frames", true),
	CONFIGURATION("config", true),
	SWORD("sword", true),
	GENERATED_SWORD("generatedsword", true),
	GUN("gun", true),
	GENERATED_GUN("generatedgun", true),
	NPC_TYPE("npctype", true),
	INSTRUMENT("instrument", true),
	MONSTER_TYPE("monstertype", true),
	MONSTER_PART("monsterpart", true),
	PARTICLE("particle", true),
	PROJECTILE("projectile", true),
	GRASS("grass", true),
	STEM_MODULAR("modularstem", true),
	FOILAGE_MODULAR("modularfoilage", true),
	STATUS_EFFECT("statuseffect", true),
	AUGMENT("augment", true),
	CODEX("codex", true),
	TOOL_MINING("miningtool", true),
	TOOL_HARVESTING("harvestingtool", true),
	TOOL_WIRE("wiretool", true),
	TOOL_TILING("tilingtool", true),
	TOOL_PAINT("painttool", true),
	FLASHLIGHT("flashlight", true),
	CHEST("chest", true),
	LEGS("legs", true),
	HEAD("head", true),
	BACK("back", true),
	GENERATED_SHIELD("generatedshield", true),
	CONSUMABLE("consumable", true),
	BIOME_SURFACE("surfacebiome", true),
	BIOME_CORE("corebiome", true),
	BIOME_UNDERGROUND("undergroundbiome", true),
	PARALLAX("parallax", true),
	PARALLAX_UNDERGROUND("undergroundparallax", true),
	DUNGEON("dungeon", true),
	EFFECTSOURCE("effectsource", true),
	DAMAGE("damage", true),
	CINEMATIC("cinematic", true),
	BEAMAXE("beamaxe", true),
	GRAPPLINGHOOK("grapplinghook", true),
	ITEM_THROWN("thrownitem", true),
	ITEM_TECH("techitem", true),
	TECH("tech", true),
	TERRAIN("terrain", true),
	RIDGE_CAVE("ridgecave", true),
	RIDGE_BLOCKS("ridgeblocks", true),
	CANYON("canyon", true),
	CHASM("chasm", true),
	DUNES("dunes", true),
	HILLS("hills", true),
	TREASUREPOOLS("treasurepools", true),
	QUESTTEMPLATE("questtemplate", true),
	STRUCTURE("structure", true),
	CELESTIAL("celestial", true),
	SPECIES("species", true),
	WEATHER("weather", true),
	SAPLING("sapling", true),
	FUNCTIONS("functions", true),
	FUNCTIONS_2("2functions", true),
	JSON("json", true),
	PNG("png"),
	LUA("lua"),
	OTHER;
	
	static final String[] jsonExt;
	static final AssetType[] jsonTypes;
	static
	{
		AssetType[] types = values();
		ArrayList<AssetType> jt = new ArrayList<AssetType>();
		for(AssetType t:types)
		{
			if(t.isJson)jt.add(t);
		}
		jsonExt = new String[jt.size()];
		jsonTypes = new AssetType[jt.size()];
		for(int i = 0; i < jt.size(); i ++)
		{
			jsonTypes[i] = jt.get(i);
			jsonExt[i] = jsonTypes[i].fileExtension;
		}
	}
	
	private String fileExtension;
	private boolean isJson = false;
	
	private AssetType(String ext, boolean isJson)
	{
		fileExtension = ext;
		this.isJson = isJson;
	}
	
	private AssetType(String ext)
	{
		fileExtension = ext;
	}
	
	private AssetType()
	{
		
	}
	
	public static AssetType getByFileExtension(String ext)
	{
		for(AssetType t: values())
		{
			if(t.fileExtension.equals(ext))return t;
		}
		return null;
	}
	
	public String getFileExtension()
	{
		return fileExtension;
	}
	
	public static AssetType[] getJsonAssets()
	{
		return jsonTypes;
	}
	
	public boolean isJsonAsset()
	{
		return isJson;
	}
	
	public boolean isOfType(String fname)
	{
		if(fileExtension == null)return false;
		return fname.endsWith(fileExtension);
	}
}
