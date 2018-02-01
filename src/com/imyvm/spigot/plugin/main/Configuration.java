package com.imyvm.spigot.plugin.main;

import cat.nyaa.nyaacore.configuration.PluginConfigure;
import cat.nyaa.nyaacore.configuration.ISerializable;
import com.imyvm.spigot.plugin.main.lootprotect.LootProtectMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static com.imyvm.spigot.plugin.main.lootprotect.LootProtectMode.OFF;

public class Configuration extends PluginConfigure {
    @Serializable
    public String language = "en_US";
    /* Enchantment Configurations */
    /* Loot Protect */
    @Serializable
    public LootProtectMode lootProtectMode = OFF;
    @Serializable(name = "bypassVanilla")
    public boolean bypassVanilla = true;
    @Serializable(name = "Exp")
    public boolean Exp = true;

    /* Death Money */
    @Serializable(name = "deathloss.enable_worlds", alias = "deathloss_enabled_world")
    public List<String> deathloss_enable_world = new ArrayList<String>(Arrays.asList("world1", "world2"));
    @Serializable(name = "miniloss")
    public double miniloss = 30.00;
    @Serializable(name = "maxloss")
    public double maxloss = 10000.00;
    @Serializable(name = "losspercent")
    public double losspercent = 8.00;
    @Serializable(name = "disabledmessage")
    public String disabledmessage = "Keepinventory is disabled in this world";
    @Serializable(name = "nomoneymessage")
    public String nomoneymessage = "You can't afford it";
    @Serializable(name = "chargedmessage")
    public String chargedmessage = "You Charged";
    @Serializable(name = "Curname")
    public String Curname = "D";
    @Serializable(name = "KeepInventory")
    public boolean KeepInventory = true;

    private final PluginMain plugin;

    @Override
    protected JavaPlugin getPlugin() {
        return plugin;
    }

    public Configuration(PluginMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public void deserialize(ConfigurationSection config) {
        // general values load & standalone config load
        ISerializable.deserialize(config, this);
    }

    @Override
    public void serialize(ConfigurationSection config) {
        // general values save & standalone config save
        ISerializable.serialize(config, this);
    }
}
