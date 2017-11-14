package cn.blockmc.Zao_hon.GuildSystem;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin{
	private static Main instance;
	@Override
	public void onEnable(){
		instance = this;
		this.saveDefaultConfig();
		this.createGuildDataFolder();
		GuildManager.Get().sort();
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		this.getServer().getPluginManager().registerEvents(GuildGUI.Get(), this);
		this.getCommand("GuildSystem").setExecutor(new Commands());
	}
	@Override
	public void onDisable(){
		GuildManager.Get().saveGuilds();
	}
	
	private void createGuildDataFolder(){
		File folder = new File(this.getDataFolder(), "Guilds");
		if(!folder.exists()){
			folder.mkdir();
		}
	}

	public static Main getInstance(){
		return instance;
	}

}
