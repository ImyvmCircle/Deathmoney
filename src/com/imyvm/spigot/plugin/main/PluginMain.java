package com.imyvm.spigot.plugin.main;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import static java.lang.Math.*;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

public class PluginMain extends JavaPlugin implements Listener {
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    FileConfiguration config = getConfig();
    List<String> world = Arrays.asList("world", "fun");

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onEnable() {
        log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));

        config.addDefault("minloss", 30);
        config.addDefault("maxloss", 10000);
        config.addDefault("maxloss", 10000);
        config.addDefault("percent", 8);
        config.addDefault("chargemessage", "You Charged");
        config.addDefault("worldmessage", "Keepinventory is disabled in this world");
        config.addDefault("nomoneymessage", "You can't afford it");
        config.addDefault("EnabledWorld", world);
        config.options().copyDefaults(true);
        saveConfig();


        Bukkit.getPluginManager().registerEvents(this, this);
        RegisteredServiceProvider<Economy> economyP = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyP != null)
            econ = economyP.getProvider();
        else
            Bukkit.getLogger().info( "Unable to initialize Economy Interface with Vault!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @EventHandler
    public void onEntityDeath(PlayerDeathEvent event) {
        Player player = (Player) event.getEntity();
        String a = player.getName();
        double s = econ.getBalance(player);
        World world = player.getWorld();
        String w = world.getName();
        BigDecimal balance = new BigDecimal(s);
        String chargemessage = ChatColor.translateAlternateColorCodes('&',config.getString("chargemessage"));
        String nomoneymessage = ChatColor.translateAlternateColorCodes('&',config.getString("nomoneymessage"));
        String worldmessage = ChatColor.translateAlternateColorCodes('&',config.getString("worldmessage"));
            if (config.getStringList("EnabledWorld").contains(w)) {
                if (s >= config.getDouble("minloss")) {
                    event.setKeepInventory(true);
                    double dd = s * config.getDouble("percent") / 100 + config.getDouble("minloss");
                    BigDecimal charge = new BigDecimal(dd);
                    if (balance.compareTo(charge) == -1) {
                        double d = s;
                        EconomyResponse t = econ.withdrawPlayer(player, d);
                        player.sendMessage(chargemessage + econ.format(d));
                    } else {
                        double d = min(dd, config.getDouble("maxloss"));
                        EconomyResponse t = econ.withdrawPlayer(player, d);
                        player.sendMessage(chargemessage + econ.format(d));
                    }
                } else {
                    player.sendMessage(nomoneymessage);
                }
            } else {
                player.sendMessage(worldmessage);
            }
        }
    public static Economy getEcononomy() {
        return econ;
    }
}