package cn.blockmc.Zao_hon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Guild {
	private int guildlevel=1;
	private int membersnumber=guildlevel*33;
	private double economy=0.0;
	// private ItemStack guildicon;
	private String lore="";
	private String guildname="";
	private UUID president=null;
	private boolean canceldamage = false;
	private HashSet<UUID> members=new HashSet<UUID>();
	private HashSet<UUID> applyjoinlist= new HashSet<UUID>();

	public Guild(String guildname, UUID president) {
		this.guildname = guildname;
		this.president = president;
	}

	public Guild(File file) {
		this.Load(file);
	}
	public boolean isCancelDamage(){
		return this.canceldamage;
	}
	public boolean isFull() {
		return !(members.size() <= membersnumber);
	}
	
	public boolean hasPlayer(OfflinePlayer player) {
		UUID uuid = player.getUniqueId();
		return president.equals(uuid) || members.contains(uuid);
	}

	public boolean hasApplied(OfflinePlayer player) {
		return applyjoinlist.contains(player.getUniqueId());
	}

	public void applyJoin(OfflinePlayer player) {
		applyjoinlist.add(player.getUniqueId());
	}

	public void refuseApply(OfflinePlayer player) {
		applyjoinlist.remove(player.getUniqueId());
	}
	public void Kick(UUID uuid){
		members.remove(uuid);
	}

	public void Join(OfflinePlayer player) {
		members.add(player.getUniqueId());
		if (applyjoinlist.contains(player.getUniqueId())) {
			applyjoinlist.remove(player.getUniqueId());
		}
	}

	public void Leave(OfflinePlayer player) {
		members.remove(player.getUniqueId());
	}

	public OfflinePlayer President() {
		return Bukkit.getOfflinePlayer(this.president);
	}

	public UUID PresidentUUID() {
		return president;
	}

	public HashSet<UUID> ApplyJoinList() {
		return applyjoinlist;
	}

	public HashSet<UUID> MembersUUID() {
		return members;
	}

	public HashSet<OfflinePlayer> Members() {
		HashSet<OfflinePlayer> membersplayer = new HashSet<OfflinePlayer>();
		for (UUID uuid : this.members) {
			membersplayer.add(Bukkit.getOfflinePlayer(uuid));
		}
		return membersplayer;
	}

	public double Economy() {
		return this.economy;
	}

	public ItemStack Icon() {
		return this.Icon();
	}

	public String Lore() {
		return this.lore;
	}

	public String Name() {
		return this.guildname;
	}
	public void setCancelDamage(boolean bool){
		this.canceldamage = bool;
	}

	public void setEconomy(double economy) {
		this.economy = economy;
	}

	public void addEconomy(double economy) {
		this.economy += economy;
	}

	public void changeName(String name) {
		this.guildname = name;
	}

	public void changeLore(String lore) {
		this.lore = lore;
	}

	public void Save() {
		File guildfile = new File(GuildManager.Get().getGuildFolder(), this.guildname + ".yml");
		try {
			if (!guildfile.exists()) {
				guildfile.createNewFile();
			}
			FileConfiguration guildconfig = YamlConfiguration.loadConfiguration(guildfile);
			guildconfig.set("Level", guildlevel);
			guildconfig.set("Economy", economy);
			guildconfig.set("Lore", lore);
			guildconfig.set("President", String.valueOf(president));
			ArrayList<String> memberuuids = new ArrayList<String>();
			for (UUID uuid : members) {
				memberuuids.add(String.valueOf(uuid));
			}
			guildconfig.set("Members", memberuuids);
			ArrayList<String> applyjoinuuids = new ArrayList<String>();
			for (UUID uuid : applyjoinlist) {
				applyjoinuuids.add(String.valueOf(uuid));
			}
			guildconfig.set("ApplyJoin", applyjoinuuids);
			guildconfig.set("Settings.CancelDamage", canceldamage);
			guildconfig.save(guildfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void Load(File file){
		FileConfiguration guildconfig = YamlConfiguration.loadConfiguration(file);
		this.guildname= file.getName().replaceAll("[.][^.]+$", "");
		this.lore= guildconfig.getString("Lore");
		this.economy = guildconfig.getDouble("Economy");
		this.president= UUID.fromString(guildconfig.getString("President"));
		this.guildlevel = guildconfig.getInt("Level");
		HashSet<UUID> members = new HashSet<UUID>();
		for (String str : guildconfig.getStringList("Members")) {
			members.add(UUID.fromString(str));
		}
		this.members = members;
		HashSet<UUID> applyjoinlist = new HashSet<UUID>();
		for(String str:guildconfig.getStringList("ApplyJoin")){
			applyjoinlist.add(UUID.fromString(str));
		}
		this.applyjoinlist = applyjoinlist;
		ConfigurationSection settings = guildconfig.getConfigurationSection("Settings");
		this.canceldamage = settings.getBoolean("CancelDamage");
	}

	// public static LinkedList<Guild> Guilds = new LinkedList<Guild>();
	//
	// public static LinkedList<Guild> sort() {
	// Collections.sort(Guilds, new Comparator<Guild>() {
	//
	// @Override
	// public int compare(Guild o1, Guild o2) {
	// if (o1.Economy() > o2.Economy()) {
	// return 1;
	// }
	// if (o1.Economy() > o2.Economy()) {
	// return 0;
	// }
	// // TODO Auto-generated method stub
	// return -1;
	// }
	//
	// });
	// return Guilds;
	// }
	//
	// public static boolean InGuild(Player player) {
	// for (Guild guild : Guilds) {
	// if (guild.Members().contains(player.getUniqueId())) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public static boolean InGuild(Player player, Guild guild) {
	// return guild.Members().contains(player.getUniqueId()) ? true : false;
	// }
	//
	// public static boolean Exist(String guildname) {
	// for (Guild guild : Guilds) {
	// if (guild.Name().equals(guildname)) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public static Guild CreateNewGuild(String guildname, Player president) {
	// return Guild.CreateNewGuild(guildname, president, "", 1);
	// }
	//
	// public static Guild CreateNewGuild(String guildname, Player president,
	// String lore) {
	// return Guild.CreateNewGuild(guildname, president, lore, 1);
	// }
	//
	// public static Guild CreateNewGuild(String guildname, Player president,
	// String lore, int level) {
	// Guild guild = new Guild(guildname, president.getUniqueId(), lore, level);
	// Bukkit.broadcastMessage(president.getName());
	// guild.Save();
	// Guilds.add(guild);
	// sort();
	// return guild;
	// }
	//
	// public static Guild Get(String guildname) {
	// for (Guild guild : Guilds) {
	// if (guild.Name().equals(guildname)) {
	// return guild;
	// }
	// }
	// return null;
	// }
	//
	// public static Guild Get(Player player) {
	// for (Guild guild : Guilds) {
	// if (guild.members.contains(player.getUniqueId())) {
	// return guild;
	// }
	// }
	// return null;
	// }
	//
	// public static File getGuildFolder() {
	// return new File(Main.getInstance().getDataFolder(), "Guilds");
	// }
	//
	// public static void saveAll() {
	// for (Guild guild : Guild.Guilds) {
	// guild.Save();
	// }
	// }
	//
	// public static void loadGuilds() {
	// int number = 0;
	// for (File guildfile : (new File(Main.getInstance().getDataFolder(),
	// "Guilds")).listFiles()) {
	// FileConfiguration guildconfig =
	// YamlConfiguration.loadConfiguration(guildfile);
	// String guildname = guildfile.getName().replaceAll("[.][^.]+$", "");
	// String lore = guildconfig.getString("Lore");
	// double economy = guildconfig.getDouble("Economy");
	// UUID president = UUID.fromString(guildconfig.getString("President"));
	// int guildlevel = guildconfig.getInt("Level");
	// HashSet<UUID> members = new HashSet<UUID>();
	// for (String str : guildconfig.getStringList("Members")) {
	// members.add(UUID.fromString(str));
	// }
	// Guilds.add(new Guild(guildname, president, lore, guildlevel, members,
	// members, economy));
	//
	// Main.getInstance().getLogger().info("已查找到公会" + guildname + "...");
	// number++;
	// }
	// if (number != 0) {
	// Main.getInstance().getLogger().info("共成功加载" + number + "个社团");
	// } else {
	// Main.getInstance().getLogger().info("没有找到公会");
	// }
	// sort();
	// }

}