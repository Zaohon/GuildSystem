package cn.blockmc.Zao_hon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Events implements Listener {
	@EventHandler
	public void cancelDamageInGuild(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player p1 = (Player) e.getDamager();
			Player p2 = (Player) e.getEntity();
			if (GuildManager.Get().inGuild(p1) && GuildManager.Get().inGuild(p2, GuildManager.Get().getGuild(p1))
					&& GuildManager.Get().getGuild(p1).isCancelDamage()) {
				e.setCancelled(true);
			}
		}
	}
}
