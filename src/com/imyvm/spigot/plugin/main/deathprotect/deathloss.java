package com.imyvm.spigot.plugin.main.deathprotect;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

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
        Player player = event.getEntity();
        double s = econ.getBalance(player);
        World world = player.getWorld();
        String w = world.getName();
        FileConfiguration config = plugin.getConfig();
        DecimalFormat df = new DecimalFormat( "0.00 ");
        String chargemessage = ChatColor.translateAlternateColorCodes('&', config.getString("chargemessage"));
        String nomoneymessage = ChatColor.translateAlternateColorCodes('&', config.getString("nomoneymessage"));
        String worldmessage = ChatColor.translateAlternateColorCodes('&', config.getString("disablemessage"));
        String curname = ChatColor.translateAlternateColorCodes('&', config.getString("Curname"));
        if (config.getBoolean("Enabled")) {
            if (config.getStringList("EnabledWorld").contains(w)) {
                if (player.hasPermission("deathmoney.bypass")){
                    keep(event,player,config.getBoolean("KeepInventory"),config.getBoolean("KeepExp"),
                            config.getBoolean("ANISHING_CURSE"));
                    return;
                }
                if (econ.has(player, config.getDouble("minloss"))) {
                    double loss = s * config.getDouble("losspercent") / 100. + config.getDouble(("minloss"));
                    if (econ.has(player, loss)) {
                        double lossr = min(loss, config.getDouble("maxloss"));
                        String los = df.format(lossr);
                        econ.withdrawPlayer(player, lossr);
                        player.sendMessage(chargemessage + los + curname);
                    } else {
                        String los = df.format(s);
                        econ.withdrawPlayer(player, s);
                        player.sendMessage(chargemessage + los + curname);
                    }
                    keep(event,player,config.getBoolean("KeepInventory"),config.getBoolean("KeepExp"),
                            config.getBoolean("ANISHING_CURSE"));
                } else {
                    player.sendMessage(nomoneymessage);
                }
            } else {
                player.sendMessage(worldmessage);
            }
        }
    }

    private void keep(PlayerDeathEvent event, Player player, Boolean keepinventory, Boolean keepexp, Boolean anishing_curse){
        event.setKeepInventory(keepinventory);
        event.setKeepLevel(keepexp);
        int exp = 0;
        if (keepexp){
            event.setDroppedExp(exp);
        }
        if (anishing_curse) {
            int j;
            ItemStack itemStack1 = new ItemStack(Material.AIR);
            for (j = 0; j < player.getInventory().getSize(); j++) {
                ItemStack item = player.getInventory().getItem(j);
                if(!(item == null)){
                    if (item.containsEnchantment(Enchantment.VANISHING_CURSE)) {
                        player.getInventory().setItem(j,itemStack1);
                    }
                }
            }
            player.updateInventory();
        }
    }
}
