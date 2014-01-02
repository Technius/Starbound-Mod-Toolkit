package co.technius.starboundmodtoolkit;

public class JsonConstants 
{
	public enum Rarity
	{
		COMMON("Common"),
		UNCOMMON("Uncommon"),
		RARE("Rare"),
		LEGENDARY("Legendary");
		
		private String name;
		private Rarity(String name)
		{
			this.name = name;
		}
		
		public String toString()
		{
			return name;
		}
	}
	
	public enum Emote
	{
		NONE,
		HAPPY,
		SAD,
		NEUTRAL,
		LAUGH,
		ANGRY,
		OH,
		OOOH,
		WINK;
		
		public String toString()
		{
			if(this == NONE)return "";
			return name().toLowerCase();
		}
	}
	
	public enum CraftingGroups
	{
		ALL,
		ANVIL,
		ARMORS,
		CHEST,
		COMPRESSOR,
		CONSUMABLE,
		COOKING,
		CRAFTINGTABLE,
		DESSERT,
		DOOR,
		ENTREE,
		FROGMERCHANT,
		FURNITURE,
		HEAD,
		KITCHEN,
		LEGS,
		LIGHT,
		MATERIALS,
		METALWORKSTATION,
		MOD,
		OBJECTS,
		OTHER,
		PANTS,
		PLAIN,
		ROBOTICCRAFTINGTABLE,
		SIDE,
		SPINNINGWHEEL,
		STONEFURNACE,
		TOOLS,
		WEAPONS,
		WIRE;
		
		public String toString()
		{
			return name().toLowerCase();
		}
	}
}
