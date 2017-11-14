package cn.blockmc.Zao_hon.GuildSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GuildGUI implements Listener {

	private static final String MAINGUINAME = "      ��b������Ϣ";
	private static final String APPLYGUINAME = "��b�����б�";
	private static final String SETTINGSGUINAME = "��b��������";
	private static final String MEMBERSGUINAME = "��b��Ա�б�";
	private static GuildGUI instance;
	private ItemStack openvappliersguiitem = new ItemStack(Material.YELLOW_FLOWER);
	private ItemStack opensettingguiitem = new ItemStack(Material.APPLE);
	private ItemStack openmembersguiitem = new ItemStack(Material.CAKE);
	private ItemStack leaveguilditem = new ItemStack(Material.REDSTONE);
	private ItemStack settingnodamageitem = new ItemStack(Material.ARMOR_STAND);
	private ItemStack returnmainguiitem = new ItemStack(Material.ARROW);

	public GuildGUI() {
		this.inistizeItem();
	}

	public static GuildGUI Get() {
		if (instance == null) {
			instance = new GuildGUI();
		}
		return instance;
	}

	public void openMembersGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, MEMBERSGUINAME);
		Guild guild = GuildManager.Get().getGuild(p);
		boolean ispresident = guild.PresidentUUID().equals(p.getUniqueId());
		ItemStack presidentskull = this.getSkullOfPlayer(guild.President());
		this.itemSetLore(presidentskull, Arrays.asList("��c�᳤"));
		inv.addItem(presidentskull);
		for (OfflinePlayer offp : guild.Members()) {
			ItemStack skull = this.getSkullOfPlayer(offp);
			ArrayList<String> lores = new ArrayList<String>();
			lores.add("��8[���]�鿴��Ϣ   ��8[�Ҽ�]����");
			if(ispresident){
				lores.add("��c[Shift+�Ҽ�]�߳�����");
			}
			this.itemSetLore(skull, lores);
			inv.addItem(skull);
		}
		inv.setItem(53,returnmainguiitem);
		p.openInventory(inv);
	}

	public void openSettingGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, SETTINGSGUINAME);
		Guild guild = GuildManager.Get().getGuild(p);
		inv.setItem(0, new ItemStack(settingnodamageitem) {
			{
				if (guild.isCancelDamage()) {
					addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
			}
		});
		inv.setItem(8, returnmainguiitem);
		p.openInventory(inv);
	}

	public void openApplyListGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, APPLYGUINAME);
		Guild guild = GuildManager.Get().getGuild(p);
		for (UUID uuid : guild.ApplyJoinList()) {
			OfflinePlayer offp = Bukkit.getOfflinePlayer(uuid);
			ItemStack skull = this.getSkullOfPlayer(offp);
			this.itemSetLore(skull, new ArrayList<String>(Arrays.asList("��7[���]ͬ�����,[�һ�]�ܾ�����")));
			inv.addItem(skull);
		}
		inv.setItem(53, returnmainguiitem);
		p.openInventory(inv);
	}

	public void openMainGUI(Player p) {
		Guild guild = GuildManager.Get().getGuild(p);
		Inventory inv = Bukkit.createInventory(null, 54, guild.Name());
		if (guild.PresidentUUID().equals(p.getUniqueId())) {
			inv.setItem(12, openvappliersguiitem);
			inv.setItem(26, opensettingguiitem);
			inv.setItem(17, openmembersguiitem);
			inv.setItem(45, leaveguilditem);
			p.openInventory(inv);
		} else {
			inv.setItem(45, leaveguilditem);
			inv.setItem(17, openmembersguiitem);
			p.openInventory(inv);
		}
	}

	@EventHandler
	public void onInventoryInteract(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if (e.getCurrentItem() == null || !GuildManager.Get().inGuild(p)) {
			return;
		}
		ItemStack clicked = e.getCurrentItem();
		ItemStack cursor = e.getCursor();
		///////////////////////////////////////////////////////////
		//////////////////////// ���˵�//////////////////////////////
		if (clicked.equals(returnmainguiitem)) {
			this.openMainGUI(p);
			return;
		}
		if (GuildManager.Get().Exist(inv.getName())) {
			e.setCancelled(true);
			// Guild guild = GuildManager.Get().getGuild(p);
			if (clicked.equals(this.openvappliersguiitem)) {
				this.openApplyListGUI(p);
			} else if (clicked.equals(this.leaveguilditem)) {
				p.closeInventory();
				p.performCommand("gs leave");
			} else if (clicked.equals(this.opensettingguiitem)) {
				this.openSettingGUI(p);
			}else if(clicked.equals(this.openmembersguiitem)){
				this.openMembersGUI(p);
			}
			return;
		}
		////////////////////////////////////////////////////////////
		//////////////////////// �����б�/////////////////////////////
		else if (inv.getName().equals(APPLYGUINAME)) {
			e.setCancelled(true);
			if (clicked.getType() != Material.AIR && e.getRawSlot() <= 53) {
				if (e.getClick() == ClickType.LEFT) {
					Guild guild = GuildManager.Get().getGuild(p);
					guild.Join(Bukkit.getOfflinePlayer(UUID.fromString(clicked.getItemMeta().getLocalizedName())));
					p.sendMessage("��ͬ��" + clicked.getItemMeta().getDisplayName() + "��ҽ�����");
				} else if (e.getClick() == ClickType.RIGHT) {
					Guild guild = GuildManager.Get().getGuild(p);
					guild.refuseApply(
							Bukkit.getOfflinePlayer(UUID.fromString(clicked.getItemMeta().getLocalizedName())));
					p.sendMessage("�Ѿܾ�" + clicked.getItemMeta().getDisplayName() + "��ҵ�����");
				}
				this.openApplyListGUI(p);
				return;
			}
		}
		//////////////////////////////////////////////////////////////////
		////////////////////////// ��������/////////////////////////////////
		else if (inv.getName().equals(SETTINGSGUINAME)) {
			e.setCancelled(true);
			if (clicked.getType() == Material.AIR
					|| e.getClick() != ClickType.LEFT && e.getClick() != ClickType.RIGHT||e.getSlot()>53) {
				return;
			}
			boolean b = e.getClick() == ClickType.LEFT ? true : false;
			Guild guild = GuildManager.Get().getGuild(p);
			if (clicked.getItemMeta().getDisplayName().equals(settingnodamageitem.getItemMeta().getDisplayName())) {
				guild.setCancelDamage(b);
				openSettingGUI(p);
			}
			return;
		}
		///////////////////////////////////////////////////////////////////
		///////////////////////////��Ա�б�////////////////////////////////
		else if(inv.getName().equals(MEMBERSGUINAME)){
			e.setCancelled(true);
			if(clicked.getType()==Material.AIR||e.getSlot()>53){
				return;
			}
			Guild guild = GuildManager.Get().getGuild(p);
			if(e.getClick()==ClickType.SHIFT_RIGHT){
				if(guild.PresidentUUID().equals(p.getUniqueId())){
					if(clicked.getItemMeta().getDisplayName().equals(guild.President().getName())){
						p.sendMessage("��m�㲻���������Լ�");
						return;
					}
					guild.Kick(UUID.fromString(clicked.getItemMeta().getLocalizedName()));
					p.sendMessage("��c���߳�"+clicked.getItemMeta().getDisplayName()+"���");
					
				}
			}
			
			return;
		}
	}

	private void inistizeItem() {
		this.itemSetDisplayName(returnmainguiitem, "��e�������˵�");
		this.itemSetDisplayName(openvappliersguiitem, "��b�鿴�����б�");
		this.itemSetDisplayName(opensettingguiitem, "��b��������");
		this.itemSetDisplayName(leaveguilditem, "��c�뿪����");
		this.itemSetDisplayName(openmembersguiitem, "��b��Ա�б�");
		this.itemSetDisplayName(settingnodamageitem, "��c��Ա����");
		this.itemSetLore(settingnodamageitem, Arrays.asList("��8[���]����   ��8[�Ҽ�]����"));
		this.itemHideEnchantMent(settingnodamageitem);
	}

	private ItemStack itemHideEnchantMent(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack itemSetLocalizeName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setLocalizedName(name);
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack itemSetDisplayName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack itemSetLore(ItemStack item, List<String> lores) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lores);
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack getSkullOfPlayer(OfflinePlayer p) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setDisplayName(p.getName());
		meta.setOwner(p.getName());
		meta.setLocalizedName(p.getName());
		item.setItemMeta(meta);
		return item;

	}
}
