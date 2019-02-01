package me.limeglass.reffy;

import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

public class Reffy extends JavaPlugin {

	private String packageName = "me.limeglass.reffy";
	private static Reffy instance;
	private SkriptAddon addon;
	
	public void onEnable(){
		instance = this;
		try {
			addon = Skript.registerAddon(this).loadClasses(packageName, "elements");
		} catch (IOException e) {
			Skript.exception(e, "Could not load Reffy's syntax elements");
		}
		getLogger().info("has been enabled!");
	}
	
	public SkriptAddon getAddonInstance() {
		return addon;
	}
	
	public static Reffy getInstance() {
		return instance;
	}
	
}
