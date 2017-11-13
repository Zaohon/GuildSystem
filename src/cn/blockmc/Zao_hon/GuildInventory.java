package cn.blockmc.Zao_hon;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class GuildInventory implements Listener {
//	private static GuildInventory instance;
//	private ItemStack returnitem;
//	private ItemStack createguilditem;
//
//	public GuildInventory() {
//		instance = this;
//		initializeItems();
//	}
//
//	public void openMainGuildInventory(Player p) {
//		Inventory inv = Bukkit.createInventory(null, 54, "§a§l            社团系统");
//		inv.setItem(45, createguilditem);
//		p.openInventory(inv);
//	}
//
//	public void openCreateGuilInventory(Player p) {
//		 Inventory inv =Bukkit.createInventory(p, InventoryType.ANVIL);
//	}
//
//	public void Update() {
//		instance = new GuildInventory();
//	}
//
//	public static GuildInventory Instance() {
//		return instance;
//	}
//
//	@EventHandler
//	public void interactInventory(InventoryClickEvent e) {
//		Inventory inv = e.getClickedInventory();
//		if (inv != null) {
//			Player p = (Player) e.getWhoClicked();
//			//////////////社团系统主菜单///////////////
//			if (inv.getTitle().equals("§a§l            社团系统")) {
//				ItemStack item = e.getCurrentItem();
//				if(item.equals(createguilditem)){
//					openCreateGuilInventory(p);
//				}
//			}
//			/////////////////////////////////////////
//		}
//	}
//
//	@EventHandler
//	public void dropItemEvent(PlayerDropItemEvent e) {
//		Bukkit.broadcastMessage("XF");
//		openMainGuildInventory(e.getPlayer());
//	}
//
//	// Nothing important under
//	private void initializeItems() {
//		returnitem = new ItemStack(Material.FEATHER);
//		ItemMeta returnitemmeta = returnitem.getItemMeta();
//		returnitemmeta.setDisplayName("§b返回");
//		returnitem.setItemMeta(returnitemmeta);
//		createguilditem = new ItemStack(Material.BANNER);
//		BannerMeta createguilditemdata = (BannerMeta) createguilditem.getItemMeta();
//		createguilditemdata.setDisplayName("§cCreate Guild");
//		createguilditem.setItemMeta(createguilditemdata);
//	}

}
