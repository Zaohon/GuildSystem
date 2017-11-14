package cn.blockmc.Zao_hon.GuildSystem;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class GuildManager {
	private Main plugin;
	public LinkedList<Guild> guilds = new LinkedList<Guild>();

	public GuildManager() {
		this.plugin = Main.getInstance();
		loadGuilds();
	}

	public LinkedList<Guild> sort() {
		Collections.sort(guilds, new Comparator<Guild>() {

			@Override
			public int compare(Guild o1, Guild o2) {
				if (o1.Economy() > o2.Economy()) {
					return 1;
				}
				if (o1.Economy() == o2.Economy()) {
					return 0;
				}
				return -1;
			}

		});
		return guilds;
	}

	public LinkedList<Guild> Guilds() {
		return guilds;
	}

	public boolean inGuild(Player player) {
		for (Guild guild : guilds) {
			if (guild.hasPlayer(player)) {
				return true;
			}
		}
		return false;
	}

	public boolean inGuild(Player player, Guild guild) {
		return guild.hasPlayer(player);
	}

	public boolean Exist(String guildname) {
		for (Guild guild : guilds) {
			if (guild.Name().equals(guildname)) {
				return true;
			}
		}
		return false;
	}

	public Guild CreateNewGuild(String guildname, Player president) {
		Guild guild = new Guild(guildname, president.getUniqueId());
		guild.Save();
		guilds.add(guild);
		sort();
		return guild;
	}

	public Guild getGuild(String guildname) {
		for (Guild guild : guilds) {
			if (guild.Name().equals(guildname)) {
				return guild;
			}
		}
		return null;
	}

	public Guild getGuild(Player player) {
		for (Guild guild : guilds) {
			if (guild.MembersUUID().contains(player.getUniqueId())
					|| guild.PresidentUUID().equals(player.getUniqueId())) {
				return guild;
			}
		}
		return null;
	}

	public File getGuildFolder() {
		return new File(Main.getInstance().getDataFolder(), "Guilds");
	}

	public void deleteGuild(Guild guild) {
		guilds.remove(guild);
		File file = new File(getGuildFolder(), guild.Name() + ".yml");
		file.delete();
	}

	public void reloadGuilds() {
		instance = new GuildManager();
	}

	public void saveGuilds() {
		for (Guild guild : guilds) {
			guild.Save();
		}
	}

	public void loadGuilds() {
		int number = 0;
		for (File guildfile : getGuildFolder().listFiles()) {
			String guildname = guildfile.getName().replaceAll("[.][^.]+$", "");
			Guild guild = new Guild(guildfile);
			guilds.add(guild);
			// FileConfiguration guildconfig =
			// YamlConfiguration.loadConfiguration(guildfile);
			// String guildname = guildfile.getName().replaceAll("[.][^.]+$",
			// "");
			// String lore = guildconfig.getString("Lore");
			// double economy = guildconfig.getDouble("Economy");
			// UUID president =
			// UUID.fromString(guildconfig.getString("President"));
			// int guildlevel = guildconfig.getInt("Level");
			// HashSet<UUID> members = new HashSet<UUID>();
			// for (String str : guildconfig.getStringList("Members")) {
			// members.add(UUID.fromString(str));
			// }
			// HashSet<UUID> applyjoinlist = new HashSet<UUID>();
			// for(String str:guildconfig.getStringList("ApplyJoin")){
			// applyjoinlist.add(UUID.fromString(str));
			// }
			// guilds.add(new Guild(guildname, president, lore, guildlevel,
			// members,applyjoinlist, economy));

			Main.getInstance().getLogger().info("已查找到公会" + guildname + "...");
			number++;
		}
		if (number != 0) {
			Main.getInstance().getLogger().info("共成功加载" + number + "个社团");
		} else {
			Main.getInstance().getLogger().info("没有找到公会");
		}
	}

	private static GuildManager instance;

	public static GuildManager Get() {
		if(instance==null){
			instance = new GuildManager();
		}
		return instance;
	}
}
