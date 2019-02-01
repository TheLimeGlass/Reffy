package me.limeglass.reffy;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

public class Reffy extends JavaPlugin {

	private Map<String, FileConfiguration> files = new HashMap<>();
	private String packageName = "me.limeglass.reffy";
	private static Reffy instance;
	private SkriptAddon addon;
	
	public void onEnable(){
		instance = this;
		File configFile = new File(getDataFolder(), "config.yml");
		//If newer version was found, update configuration.
		if (!getDescription().getVersion().equals(getConfig().getString("version"))) {
			if (configFile.exists()) configFile.delete();
		}
		//Create all the default files.
		for (String name : Arrays.asList("config")) {
			File file = new File(getDataFolder(), name + ".yml");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				saveResource(file.getName(), false);
			}
			FileConfiguration configuration = new YamlConfiguration();
			try {
				configuration.load(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			files.put(name, configuration);
		}
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
