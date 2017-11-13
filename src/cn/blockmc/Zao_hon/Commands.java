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
				p.sendMessage("�㲻��һ��������");
			}
			return true;
		}
		if (lenth >= 1) {
			String cmdtype = args[0];
			//////////////////////////////////////////////////
			////////////////////// ��������/////////////////////
			if (cmdtype.equals("create")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("ֻ�������ʹ�ø�ָ��");
					return true;
				}
				Player p = (Player) sender;
				if (!p.hasPermission("GuildSystem.CreateGuild")) {
					p.sendMessage("��c��û��ʹ�ø������Ȩ��");
					return true;
				}
				if (lenth >= 2) {
					String guildname = args[1];
					if (GuildManager.Get().Exist(guildname)) {
						p.sendMessage("��c�ù����Ѿ�����");
						return true;
					}
					if (GuildManager.Get().inGuild(p)) {
						p.sendMessage("��c���Ѿ���һ���������ˣ���Ҫ�������������˳�����");
						return true;
					}
					if (lenth == 3 && args[2].equals("confirm")) {
						GuildManager.Get().CreateNewGuild(guildname, p);
						Bukkit.broadcastMessage("��a" + p.getName() + "��7�ոմ�����" + guildname + "��7����");
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
			////////////////////// �����б�/////////////////////
			else if (cmdtype.equals("list")) {
				sender.sendMessage("��b�ʲ����У�");
				for (int i = 0; i <= 10; i++) {
					if (i > GuildManager.Get().guilds.size() - 1) {
						return true;
					}
					Guild guild = GuildManager.Get().Guilds().get(i);
					sender.sendMessage("��b" + (i + 1) + ":" + guild.Name() + "    ��a�ʲ�:" + guild.Economy());
					Player p = (Player) sender;
				}
				return true;
			}
			//////////////////////////////////////////////////
			/////////////////////// �뿪����/////////////////////
			else if (cmdtype.equals("leave")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("������ֻ�������ʹ��");
					return true;
				}
				Player p = (Player) sender;
				if (!GuildManager.Get().inGuild(p)) {
					p.sendMessage("��c�㲻��һ��������");
					return true;
				}
				Guild guild = GuildManager.Get().getGuild(p);
				if (lenth == 2 && args[1].equals("confirm")) {
					if (p.getUniqueId().equals(guild.PresidentUUID())) {
						GuildManager.Get().deleteGuild(guild);
						p.sendMessage("��b�ѽ�ɢ����" + guild.Name());
					} else {
						guild.Leave(p);
						p.sendMessage("��b���뿪����" + guild.Name());
					}
				} else {
					p.sendMessage("��b��ȷ��Ҫ�˳������ڵ�������");
					TextComponent message = new TextComponent("��7   ");
					TextComponent message1 = new TextComponent("��c[ȷ���˳�]");
					if (p.getUniqueId().equals(guild.PresidentUUID())) {
						message1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("��c���������糤�����˳����Ž���ζ�����Ž�ɢ").create()));
					} else {
						message1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("��c�ò������ɳ����������ؿ���").create()));
					}
					message1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gs leave confirm"));
					message.addExtra(message1);
					message.addExtra("    ");
					p.spigot().sendMessage(message);
				}
				return true;
			}
			//////////////////////////////////////////////////
			////////////////////// �������/////////////////////
			else if (cmdtype.equals("join")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("��ָ��ֻ�������ʹ��");
					return true;
				}
				Player p = (Player) sender;
				if (lenth >= 2) {
					String guildname = args[1];
					if (GuildManager.Get().inGuild(p)) {
						p.sendMessage("��c���Ѿ�������һ�����ţ�");
						return true;
					}
					if (!GuildManager.Get().Exist(guildname)) {
						p.sendMessage("��c�����Ų�����");
						return true;
					}
					Guild guild = GuildManager.Get().getGuild(guildname);
					if (guild.isFull()) {
						p.sendMessage("��c�������Ѿ������ˣ�");
						return true;
					}
					if (guild.hasApplied(p)) {
						p.sendMessage("��c���Ѿ���������������,��ȴ��ظ�");
						return true;
					}
					guild.applyJoin(p);
					p.sendMessage("&b���������" + guildname + "����");
					return true;
				} else {
					sendJoinGuildHelp(p);
					return true;
				}
			}
			////////////////////////////////////////////////////
			/////////////////////// ���ز��///////////////////////
			else if (cmdtype.equals("reload")) {
				if (sender.hasPermission("GuildSystem.Reload")) {
					GuildManager.Get().reloadGuilds();
					sender.sendMessage("���سɹ�");
					return true;
				} else {
					sender.sendMessage("��c��û��Ȩ����ô��");
					return true;
				}
			}
			/////////////////////////////////////////////////////
			/////////////////////// �������////////////////////////
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
		sender.sendMessage("����ϵͳ�������");
		sender.sendMessage("/gs list --�����б�");
		sender.sendMessage("/gs join --�����������");
		sender.sendMessage("/gs create --��������");
		sender.sendMessage("/gs leave --�뿪����");

	}

	private void sendJoinGuildHelp(Player p) {
		p.sendMessage("/gs join xxx");
	}

	private void sendCreateGuildHelp(Player p) {
		p.sendMessage("/gs create xxx");
	}

	private void createGuild(Player p, String guildname) {
		p.sendMessage("��7================");
		p.sendMessage("��7=��c������:��r" + guildname + "  ��7=");
		TextComponent message = new TextComponent("��a[����]");
		message.setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("��c�⽲�����������Ԫ��ȷ��Ҫ����������").create()));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gs create " + guildname + " confirm"));
		TextComponent message1 = new TextComponent("��7=    ");
		message1.addExtra(message);
		message1.addExtra("     ��7=");
		p.spigot().sendMessage(message1);
		p.sendMessage("��7================");

	}

}
