package cn.blockmc.Zao_hon;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Commands implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("GuildSystem")) {
			return false;
		}
		int lenth = args.length;
		if(lenth==0&&sender instanceof Player){
			Player p = (Player)sender;
			if(GuildManager.Get().inGuild(p)){
				GuildGUI.Get().openMainGUI(p);
			}else{
				p.sendMessage("你不在一个社团中");
			}
			return true;
		}
		if (lenth >= 1) {
			String cmdtype = args[0];
			//////////////////////////////////////////////////
			////////////////////// 创建社团/////////////////////
			if (cmdtype.equals("create")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("只有玩家能使用该指令");
					return true;
				}
				Player p = (Player) sender;
				if (!p.hasPermission("GuildSystem.CreateGuild")) {
					p.sendMessage("§c你没有使用该命令的权限");
					return true;
				}
				if (lenth >= 2) {
					String guildname = args[1];
					if (GuildManager.Get().Exist(guildname)) {
						p.sendMessage("§c该公会已经存在");
						return true;
					}
					if (GuildManager.Get().inGuild(p)) {
						p.sendMessage("§c你已经在一个公会中了！若要创建公会请先退出公会");
						return true;
					}
					if (lenth == 3 && args[2].equals("confirm")) {
						GuildManager.Get().CreateNewGuild(guildname, p);
						Bukkit.broadcastMessage("§a" + p.getName() + "§7刚刚创建了" + guildname + "§7公会");
						return true;
					}
					this.createGuild(p, guildname);
					return true;
				} else {
					sendCreateGuildHelp(p);
					return true;
				}
			}
			//////////////////////////////////////////////////
			////////////////////// 社团列表/////////////////////
			else if (cmdtype.equals("list")) {
				sender.sendMessage("§b资产排行：");
				for (int i = 0; i <= 10; i++) {
					if (i > GuildManager.Get().guilds.size() - 1) {
						return true;
					}
					Guild guild = GuildManager.Get().Guilds().get(i);
					sender.sendMessage("§b" + (i + 1) + ":" + guild.Name() + "    §a资产:" + guild.Economy());
					Player p = (Player) sender;
				}
				return true;
			}
			//////////////////////////////////////////////////
			/////////////////////// 离开社团/////////////////////
			else if (cmdtype.equals("leave")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("该命令只允许玩家使用");
					return true;
				}
				Player p = (Player) sender;
				if (!GuildManager.Get().inGuild(p)) {
					p.sendMessage("§c你不在一个社团中");
					return true;
				}
				Guild guild = GuildManager.Get().getGuild(p);
				if (lenth == 2 && args[1].equals("confirm")) {
					if (p.getUniqueId().equals(guild.PresidentUUID())) {
						GuildManager.Get().deleteGuild(guild);
						p.sendMessage("§b已解散社团" + guild.Name());
					} else {
						guild.Leave(p);
						p.sendMessage("§b已离开社团" + guild.Name());
					}
				} else {
					p.sendMessage("§b你确定要退出你所在的社团吗");
					TextComponent message = new TextComponent("§7   ");
					TextComponent message1 = new TextComponent("§c[确认退出]");
					if (p.getUniqueId().equals(guild.PresidentUUID())) {
						message1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("§c由于你是社长，你退出社团将意味着社团解散").create()));
					} else {
						message1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("§c该操作不可撤销！请慎重考虑").create()));
					}
					message1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gs leave confirm"));
					message.addExtra(message1);
					message.addExtra("    ");
					p.spigot().sendMessage(message);
				}
				return true;
			}
			//////////////////////////////////////////////////
			////////////////////// 申请加入/////////////////////
			else if (cmdtype.equals("join")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("该指令只允许玩家使用");
					return true;
				}
				Player p = (Player) sender;
				if (lenth >= 2) {
					String guildname = args[1];
					if (GuildManager.Get().inGuild(p)) {
						p.sendMessage("§c你已经加入了一个社团！");
						return true;
					}
					if (!GuildManager.Get().Exist(guildname)) {
						p.sendMessage("§c该社团不存在");
						return true;
					}
					Guild guild = GuildManager.Get().getGuild(guildname);
					if (guild.isFull()) {
						p.sendMessage("§c该社团已经满人了！");
						return true;
					}
					if (guild.hasApplied(p)) {
						p.sendMessage("§c你已经申请过这个社团了,请等待回复");
						return true;
					}
					guild.applyJoin(p);
					p.sendMessage("&b已申请加入" + guildname + "社团");
					return true;
				} else {
					sendJoinGuildHelp(p);
					return true;
				}
			}
			////////////////////////////////////////////////////
			/////////////////////// 重载插件///////////////////////
			else if (cmdtype.equals("reload")) {
				if (sender.hasPermission("GuildSystem.Reload")) {
					GuildManager.Get().reloadGuilds();
					sender.sendMessage("重载成功");
					return true;
				} else {
					sender.sendMessage("§c你没有权限这么做");
					return true;
				}
			}
			/////////////////////////////////////////////////////
			/////////////////////// 插件帮助////////////////////////
			else if (cmdtype.equals("help")) {
				sendPluginHelp(sender);
				return true;
			}
			//////////////////////////////////////////////////////
			else if(cmdtype.equals("save")){
				GuildManager.Get().saveGuilds();
				sender.sendMessage("it is ok");
			}
		}
		return true;
	}

	private void sendPluginHelp(CommandSender sender) {
		sender.sendMessage("社团系统插件帮助");
		sender.sendMessage("/gs list --社团列表");
		sender.sendMessage("/gs join --申请加入社团");
		sender.sendMessage("/gs create --创建社团");
		sender.sendMessage("/gs leave --离开社团");

	}

	private void sendJoinGuildHelp(Player p) {
		p.sendMessage("/gs join xxx");
	}

	private void sendCreateGuildHelp(Player p) {
		p.sendMessage("/gs create xxx");
	}

	private void createGuild(Player p, String guildname) {
		p.sendMessage("§7================");
		p.sendMessage("§7=§c社团名:§r" + guildname + "  §7=");
		TextComponent message = new TextComponent("§a[创建]");
		message.setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c这讲消耗你五百万元，确定要创建社团吗").create()));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gs create " + guildname + " confirm"));
		TextComponent message1 = new TextComponent("§7=    ");
		message1.addExtra(message);
		message1.addExtra("     §7=");
		p.spigot().sendMessage(message1);
		p.sendMessage("§7================");

	}

}
