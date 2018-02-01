package com.imyvm.spigot.plugin.main;

import com.imyvm.spigot.plugin.main.deathprotect.deathloss;
import com.imyvm.spigot.plugin.main.lootprotect.LootProtectListener;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin {
    public static PluginMain instance;
    public I18n i18n;
    public static Economy econ = null;
    public Configuration cfg;
    public LootProtectListener lpListener;
    public deathloss dloss;


    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
        cfg.save();
    }

    @Override
    public void onEnable() {
        instance = this;
        cfg = new Configuration(this);
        cfg.load();
        i18n = new I18n(this, cfg.language);
        lpListener = new LootProtectListener(this);
        dloss = new deathloss(this);

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
}