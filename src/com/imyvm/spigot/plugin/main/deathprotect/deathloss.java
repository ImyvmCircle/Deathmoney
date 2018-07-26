package com.imyvm.spigot.plugin.main.deathprotect;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.UUID;

import static java.lang.Math.*;

import static com.imyvm.spigot.plugin.main.PluginMain.econ;

public class deathloss implements Listener {
    final private PluginMain plugin;


    public deathloss(PluginMain pl) {
        plugin = pl;
        plugin.getServer().getPluginManager().registerEvents(this, pl);
    }

    @EventHandler
    public void onEntityDeath(PlayerDeathEvent event) {
        Player player = (Player) event.getEntity();
        String a = player.getName();
        double s = econ.getBalance(player);
        World world = player.getWorld();
        String w = world.getName();
        BigDecimal balance = new BigDecimal(s);
        String chargemessage = ChatColor.translateAlternateColorCodes('&',plugin.cfg.chargedmessage);
        String nomoneymessage = ChatColor.translateAlternateColorCodes('&',plugin.cfg.nomoneymessage);
        String worldmessage = ChatColor.translateAlternateColorCodes('&',plugin.cfg.disabledmessage);
        String curname = ChatColor.translateAlternateColorCodes('&',plugin.cfg.Curname);
        if (plugin.cfg.deathloss_enable_world.contains(w)) {
            if(player.isOp()){
                event.setKeepInventory(plugin.cfg.KeepInventory);
            }else {
                if (s >= plugin.cfg.miniloss) {
                    event.setKeepInventory(plugin.cfg.KeepInventory); /*Toggle KeepInventory*/
                    double dd = s * plugin.cfg.losspercent / 100.00 + plugin.cfg.miniloss;
                    BigDecimal charge = new BigDecimal(dd);
                    DecimalFormat df = new DecimalFormat( "0.00 ");
                    if (balance.compareTo(charge) == -1.0) {
                        //double lossdouble = s;
                        String loss = df.format(s);
                        econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getdeathmoneyuuid)),s);
                        econ.withdrawPlayer(player, s);
                        player.sendMessage(chargemessage + loss + curname);
                    } else {
                        double d = min(dd, plugin.cfg.maxloss);
                        String loss = df.format(d);
                        econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getdeathmoneyuuid)),d);
                        econ.withdrawPlayer(player, d);
                        player.sendMessage(chargemessage + loss + curname);
                    }
                } else {
                    player.sendMessage(nomoneymessage);
                }
            }
        } else {
            player.sendMessage(worldmessage);
        }
    }
}
