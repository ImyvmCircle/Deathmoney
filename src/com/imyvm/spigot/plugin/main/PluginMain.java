package com.imyvm.spigot.plugin.main;

import com.imyvm.spigot.plugin.main.deathprotect.deathloss;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class PluginMain extends JavaPlugin implements CommandExecutor{
    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    private FileConfiguration config = getConfig();
    private List<String> world = Arrays.asList("world", "fun");
    public deathloss deathloss;

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onEnable() {
        log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));

        config.addDefault("Enabled",true);
        config.addDefault("minloss", 30);
        config.addDefault("maxloss", 10000);
        config.addDefault("losspercent", 8);
        config.addDefault("chargemessage", "You Charged");
        config.addDefault("Curname","dollars");
        config.addDefault("disablemessage", "Keepinventory is disabled in this world");
        config.addDefault("nomoneymessage", "You can't afford it");
        config.addDefault("EnabledWorld", world);
        config.addDefault("KeepInventory",true);
        config.addDefault("KeepExp",false);
        config.addDefault("ANISHING_CURSE",false);
        config.options().copyDefaults(true);
        saveConfig();

        deathloss = new deathloss(this);
        //this.getCommand("deathmoney").setExecutor(this::onCommand);

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

    public static Economy getEcononomy() {
        return econ;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("deathmoney.reload")) {
            if (args.length==0){
                sender.sendMessage(ChatColor.GREEN + "---- Deathmoney ----");
                sender.sendMessage(ChatColor.GREEN + "- /deathmoney reload    - reload this plugin");
            }
            else if (args[0].equalsIgnoreCase("reload") && args.length==1) {
                reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Configuration Reloaded!");
            } else {
                sender.sendMessage(ChatColor.GREEN + "---- Deathmoney ----");
                sender.sendMessage(ChatColor.GREEN + "- /deathmoney reload    - reload this plugin");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have the permission!");
        }
        return true;
    }
}