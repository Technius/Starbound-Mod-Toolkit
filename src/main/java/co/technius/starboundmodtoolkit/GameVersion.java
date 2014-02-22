package co.technius.starboundmodtoolkit;

import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum GameVersion 
{
	BETA_ANGRY_KOALA("Beta v. Angry Koala"),
	BETA_FURIOUS_KOALA("Beta v. Furious Koala"),
	BETA_ENRAGED_KOALA("Beta v. Enraged Koala");
	private static ObservableList<String> versionStrings;
	static
	{
		GameVersion[] v = values();
		String[] gv = new String[v.length];
		for(int i = 0; i < v.length; i ++)
			gv[i] = v[i].name;
		versionStrings = FXCollections.observableArrayList(gv);
		Collections.reverse(versionStrings);
		versionStrings = FXCollections.unmodifiableObservableList(versionStrings);
	}
	private String name;
	private GameVersion(String name)
	{
		this.name = name;
	
	}
	public String toString()
	{
		return name;
	}
	
	public static ObservableList<String> getVersionStrings()
	{
		return versionStrings;
	}
}
