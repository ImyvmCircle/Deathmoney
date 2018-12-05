package com.imyvm.spigot.plugin.main.Hooks;

import com.earth2me.essentials.User;
import com.imyvm.spigot.plugin.main.PluginMain;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


public class Essentials implements Listener {
    private final PluginMain plugin;
    private IEssentials ess;


    public Essentials(PluginMain pl) {
        plugin = pl;
        this.ess = (IEssentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
        plugin.getServer().getPluginManager().registerEvents(this, pl);
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) {
        //if (!plugin.cfg.teleportEnable) return;
        String cmd = e.getMessage().toLowerCase().trim();
        Player p = e.getPlayer();
        User iu = ess.getUser(p);
        //Location curLoc = p.getLocation();

        if (cmd.equals("/tpa")) {
            e.setCancelled(true);
            String inv_name = "§cTpa";
            OpenGUI(p, inv_name);
        } else if (cmd.startsWith("/tpa ") && cmd.length() > 5) {
            p.sendMessage("//TODO");
        } else if (cmd.equals("/tpahere")) {
            e.setCancelled(true);
            String inv_name = "§cTpahere";
            OpenGUI(p, inv_name);
        } else if (cmd.startsWith("/tpahere ") && cmd.length() > 9) {
            p.sendMessage("//TODO");
        }

    }

    private void OpenGUI(Player player, String inv_name) {
        Inventory inv = Bukkit.createInventory(null, 27, inv_name);
        for (Player a : Bukkit.getOnlinePlayers()) {
            if (a != player) {
                String name = a.getDisplayName();
                ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setDisplayName(name);
                meta.setOwningPlayer(a);
                item.setItemMeta(meta);
                inv.addItem(item);
            }
        }
        player.openInventory(inv);
        player.updateInventory();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        event.setCancelled(true);
        if (event.getInventory().getTitle().equalsIgnoreCase("§cTpa")) {
            if (event.getCurrentItem() != null) {
                User sent = ess.getUser(p);    //sent
                if (event.getCurrentItem().hasItemMeta()) {
                    SkullMeta meta = (SkullMeta) event.getCurrentItem().getItemMeta();
                    Player receiver = (Player) meta.getOwningPlayer();    //receive
                    User receive = ess.getUser(receiver);
                    if (sent.getConfigUUID().equals(receive.getTeleportRequest()) &&
                            receive.hasOutstandingTeleportRequest() && !receive.isTpRequestHere()) {
                        sent.sendMessage("§4你已经发送了" + receive.getDisplayName() + " §4一个传送请求");
                        return;
                    }
                    receive.requestTeleport(sent, false);
                    sent.sendMessage("§6请求已发送给 " + receive.getDisplayName());
                    p.closeInventory();
                    //TODO
                    JSONMessage.create(sent.getDisplayName())
                            .then(" 请求传送到你这里：").color(ChatColor.GOLD)
                            .newline().then("若想接受传送，请点击").color(ChatColor.GOLD)
                            .then("接受").color(ChatColor.DARK_RED).runCommand("/tpaccept")
                            .newline().then("若想拒绝传送，请点击").color(ChatColor.GOLD)
                            .then("拒绝").color(ChatColor.DARK_RED).runCommand("/tpdeny").send(receiver);
                }
            }
        } else if (event.getInventory().getTitle().equalsIgnoreCase("§cTpahere")) {
            if (event.getCurrentItem() != null) {
                User sent = ess.getUser(p);    //sent
                if (event.getCurrentItem().hasItemMeta()) {
                    SkullMeta meta = (SkullMeta) event.getCurrentItem().getItemMeta();
                    Player receiver = (Player) meta.getOwningPlayer();    //receive
                    User receive = ess.getUser(receiver);
                    if (sent.getConfigUUID().equals(receive.getTeleportRequest()) &&
                            receive.hasOutstandingTeleportRequest() && !receive.isTpRequestHere()) {
                        sent.sendMessage("§4你已经发送了" + receive.getDisplayName() + " §4一个传送请求");
                        JSONMessage.create("想要取消这个传送请求，请的点击")
                                .color(ChatColor.GOLD)
                                .then("取消").color(ChatColor.RED).runCommand("/tpacancel")
                                .send(p);
                        return;
                    }
                    receive.requestTeleport(sent, true);
                    sent.sendMessage("§6请求已发送给 " + receive.getDisplayName());
                    p.closeInventory();
                    //TODO
                    JSONMessage.create(sent.getDisplayName())
                            .then(" 请求传送到他那里：").color(ChatColor.GOLD)
                            .newline().then("若想接受传送，请点击").color(ChatColor.GOLD)
                            .then("接受").color(ChatColor.DARK_RED).runCommand("/tpaccept")
                            .newline().then("若想拒绝传送，请点击").color(ChatColor.GOLD)
                            .then("拒绝").color(ChatColor.DARK_RED).runCommand("/tpdeny").send(receiver);
                }
            }
        }
    }


}
