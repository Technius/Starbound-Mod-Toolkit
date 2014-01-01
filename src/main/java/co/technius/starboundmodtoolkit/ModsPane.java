package co.technius.starboundmodtoolkit;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import co.technius.starboundmodtoolkit.mod.Mod;

public class ModsPane extends TabPane
{
	ModToolkit main;
	public ModsPane(ModToolkit main)
	{
		this.main = main;
		setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
	}
	
	public ModPane addModPane(Mod mod)
	{
		Tab tab = new Tab(mod.getName());
		ModPane p = new ModPane(mod, this);
		tab.setContent(p);
		getTabs().add(tab);
		p.updateAssets();
		return p;
	}
	
	public ModPane addModPaneAndFocus(Mod mod)
	{
		ModPane p = addModPane(mod);
		main.tabs.getSelectionModel().select(1);
		getSelectionModel().selectLast();
		p.requestFocus();
		return p;
	}
}
