package com.imyvm.spigot.plugin.main.Hooks;

import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.LocationUtil;
import com.earth2me.essentials.utils.NumberUtil;
import com.imyvm.spigot.plugin.main.PluginMain;
import net.ess3.api.IEssentials;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.imyvm.spigot.plugin.main.PluginMain.econ;


public class Essentials implements Listener {
    private final PluginMain plugin;
    private IEssentials ess;


    public Essentials(PluginMain pl) {
        plugin = pl;
        this.ess = (IEssentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
        plugin.getServer().getPluginManager().registerEvents(this, pl);
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) throws InvalidWorldException {
        String cmd = e.getMessage().toLowerCase().trim();
        Player p = e.getPlayer();
        User iu = ess.getUser(p);

        if (cmd.equals("/tpa")) {
            e.setCancelled(true);
            String inv_name = "§cTpa";
            OpenTPGUI(p, inv_name);
        } else if (cmd.startsWith("/tpa ") && cmd.length() > 5) {
            String to = cmd.substring(5).trim();
            for (Player a : Bukkit.getOnlinePlayers()) {
                if (!(a.equals(p)) && a.getDisplayName().equalsIgnoreCase(to)) {
                    e.setCancelled(true);
                    doTpa(p, a);
                    return;
                }
            }
        } else if (cmd.equals("/tpahere")) {
            e.setCancelled(true);
            String inv_name = "§cTpahere";
            OpenTPGUI(p, inv_name);
        } else if (cmd.startsWith("/tpahere ") && cmd.length() > 9) {
            String to = cmd.substring(9).trim();
            for (Player a : Bukkit.getOnlinePlayers()) {
                if (!(a.equals(p)) && a.getDisplayName().equalsIgnoreCase(to)) {
                    e.setCancelled(true);
                    doTpahere(p, a);
                    return;
                }
            }
        } else if (cmd.equals("/home") || cmd.startsWith("/home ") || cmd.equals("/homes")) {
            List<String> homes = iu.getHomes();
            if (cmd.equals("/home bed") || ((cmd.equals("/home") || cmd.equals("/homes")) && homes.size() < 1)) {
                e.setCancelled(true);
                Location bedLoc = p.getBedSpawnLocation();
                if (bedLoc == null) {
                    p.sendMessage("§c错误:§4你的床已丢失或阻挡，因此你的出生点重置为主城!");
                    return;
                }
                doHome(p, iu, bedLoc);
                return;
            }
            if (cmd.equals("/home") || cmd.equals("/homes")) {
                e.setCancelled(true);
                OpenHomeGUI(p, homes);
                return;
            }
            String to = cmd.substring(6).trim();
            for (String a : homes) {
                if (a.equals(to)) {
                    e.setCancelled(true);
                    Location homeLoc;
                    try {
                        homeLoc = iu.getHome(to);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                    doHome(p, iu, homeLoc);
                    return;
                }
            }
        } else if (cmd.equals("/sethome") || cmd.startsWith("/sethome ")) {
            e.setCancelled(true);
            Location curLoc = p.getLocation();
            String name = cmd.replace("/sethome", "").trim();
            if (name.equals("")) {
                name = "home";
            }
            doSetHome(p, iu, curLoc, name);
        } else if (cmd.equals("/back") || cmd.startsWith("/back ")) {
            e.setCancelled(true);
            Location lastLoc = iu.getLastLocation();
            if (lastLoc == null) {
                p.sendMessage("§c错误:§4没有有效的返回地点");
                return;
            }
            doBack(p, iu, lastLoc);
        }
    }

    private void OpenHomeGUI(Player player, List<String> homes) {
        Inventory inv = Bukkit.createInventory(null, 9, "§cHomes");
        for (String a : homes) {
            ItemStack item = new ItemStack(Material.GREEN_BED, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(a);
            item.setItemMeta(meta);
            inv.addItem(item);
        }
        player.openInventory(inv);
        player.updateInventory();
    }

    private void OpenTPGUI(Player player, String inv_name) {
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
        if (event.getInventory().getTitle().equalsIgnoreCase("§cTpa")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                //User sent = ess.getUser(p);    //sent
                if (event.getCurrentItem().hasItemMeta()) {
                    SkullMeta meta = (SkullMeta) event.getCurrentItem().getItemMeta();
                    Player receiver = (Player) meta.getOwningPlayer();    //receive
                    //User receive = ess.getUser(receiver);
                    p.closeInventory();
                    doTpa(p, receiver);
                }
            }
        } else if (event.getInventory().getTitle().equalsIgnoreCase("§cTpahere")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                //User sent = ess.getUser(p);    //sent
                if (event.getCurrentItem().hasItemMeta()) {
                    SkullMeta meta = (SkullMeta) event.getCurrentItem().getItemMeta();
                    Player receiver = (Player) meta.getOwningPlayer();    //receive
                    //User receive = ess.getUser(receiver);
                    p.closeInventory();
                    doTpahere(p, receiver);
                }
            }
        } else if (event.getInventory().getTitle().equalsIgnoreCase("§cHomes")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                User sent = ess.getUser(p);    //sent
                if (event.getCurrentItem().hasItemMeta()) {
                    ItemMeta meta = event.getCurrentItem().getItemMeta();
                    String home = meta.getDisplayName();
                    Location homeLoc;
                    try {
                        homeLoc = sent.getHome(home);
                    } catch (InvalidWorldException ex) {
                        return;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                    p.closeInventory();
                    doHome(p, sent, homeLoc);
                }
            }
        }
    }

    private void doTpa(Player p, Player receiver) {
        User sent = ess.getUser(p);
        User receive = ess.getUser(receiver);
        if (sent.getConfigUUID().equals(receive.getTeleportRequest()) &&
                receive.hasOutstandingTeleportRequest() && !receive.isTpRequestHere()) {
            sent.sendMessage("§4你已经发送了" + receive.getDisplayName() + " §4一个传送请求");
            return;
        }
        receive.requestTeleport(sent, false);
        sent.sendMessage("§6请求已发送给 " + receive.getDisplayName());
        JSONMessage.create("想要取消这个传送请求，请点击")
                .color(ChatColor.GOLD)
                .then("取消").color(ChatColor.RED).runCommand("/tpacancel")
                .send(p);
        econ.withdrawPlayer(p, plugin.cfg.tpfee);
        p.sendMessage("§a从你的账户中扣除了 " + plugin.cfg.tpfee + " §6D");
        econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getmoneyuuid)), plugin.cfg.tpfee);
        JSONMessage.create(sent.getDisplayName())
                .then(" 请求传送到你这里：").color(ChatColor.GOLD)
                .newline().then("若想接受传送，请点击").color(ChatColor.GOLD)
                .then("接受").color(ChatColor.DARK_RED).runCommand("/tpaccept")
                .newline().then("若想拒绝传送，请点击").color(ChatColor.GOLD)
                .then("拒绝").color(ChatColor.DARK_RED).runCommand("/tpdeny").send(receiver);
    }

    private void doTpahere(Player p, Player receiver) {
        User sent = ess.getUser(p);
        User receive = ess.getUser(receiver);
        if (sent.getConfigUUID().equals(receive.getTeleportRequest()) &&
                receive.hasOutstandingTeleportRequest() && !receive.isTpRequestHere()) {
            sent.sendMessage("§4你已经发送了" + receive.getDisplayName() + " §4一个传送请求");
            return;
        }
        receive.requestTeleport(sent, true);
        sent.sendMessage("§6请求已发送给 " + receive.getDisplayName());
        JSONMessage.create("想要取消这个传送请求，请点击")
                .color(ChatColor.GOLD)
                .then("取消").color(ChatColor.RED).runCommand("/tpacancel")
                .send(p);
        econ.withdrawPlayer(p, plugin.cfg.tpfee);
        p.sendMessage("§a从你的账户中扣除了 " + plugin.cfg.tpfee + " §6D");
        econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getmoneyuuid)), plugin.cfg.tpfee);
        JSONMessage.create(sent.getDisplayName())
                .then(" 请求传送到他那里：").color(ChatColor.GOLD)
                .newline().then("若想接受传送，请点击").color(ChatColor.GOLD)
                .then("接受").color(ChatColor.DARK_RED).runCommand("/tpaccept")
                .newline().then("若想拒绝传送，请点击").color(ChatColor.GOLD)
                .then("拒绝").color(ChatColor.DARK_RED).runCommand("/tpdeny").send(receiver);
    }

    private void doHome(Player p, User iu, Location homeLoc) {
        if (iu.getWorld() != homeLoc.getWorld() && ess.getSettings().isWorldHomePermissions() &&
                !iu.isAuthorized("essentials.worlds." + homeLoc.getWorld().getName())) {
            p.sendMessage("§4你没有使用该命令的权限");
            return;
        }
        double dd = plugin.cfg.homefee;
        BigDecimal fee = new BigDecimal(dd);
        try {
            iu.getTeleport().teleport(homeLoc, new Trade(fee, ess), PlayerTeleportEvent.TeleportCause.PLUGIN);
            econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getmoneyuuid)), fee.doubleValue());
        } catch (Exception e) {
            p.sendMessage(e.getMessage());
        }
    }

    private void doSetHome(Player p, User iu, Location curLoc, String name) {
        int limit = ess.getSettings().getHomeLimit(iu);
        if (limit <= iu.getHomes().size()) {
            p.sendMessage("§c错误:§4你无法设置超过§4" + limit + "§4个家.");
            return;
        }
        if ("bed".equals(name) || NumberUtil.isInt(name)) {
            p.sendMessage("§c错误:§4无效的家名称.");
            return;
        }
        if (!ess.getSettings().isTeleportSafetyEnabled() &&
                LocationUtil.isBlockUnsafeForUser(iu, curLoc.getWorld(), curLoc.getBlockX(),
                        curLoc.getBlockY(), curLoc.getBlockZ())) {
            p.sendMessage("§c错误:§4传送的目标地点不安全");
            return;
        }
        iu.setHome(name, curLoc);
        econ.withdrawPlayer(p, plugin.cfg.sethomefee);
        p.sendMessage("§a从你的账户中扣除了 " + plugin.cfg.sethomefee + " §6D");
        econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getmoneyuuid)), plugin.cfg.sethomefee);
    }

    private void doBack(Player p, User iu, Location lastLoc) {
        if (iu.getWorld() != lastLoc.getWorld() && ess.getSettings().isWorldTeleportPermissions() &&
                !iu.isAuthorized("essentials.worlds." + lastLoc.getWorld().getName())) {
            p.sendMessage("§4你没有使用该命令的权限");
            return;
        }
        double dd = plugin.cfg.backfee;
        BigDecimal fee = new BigDecimal(dd);
        try {
            iu.getTeleport().back(new Trade(fee, ess));
            econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getmoneyuuid)), fee.doubleValue());
        } catch (Exception e) {
            p.sendMessage(e.getMessage());
        }
    }


}
