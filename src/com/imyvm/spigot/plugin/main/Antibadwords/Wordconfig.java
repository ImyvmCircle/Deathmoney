package com.imyvm.spigot.plugin.main.Antibadwords;

import cat.nyaa.nyaacore.configuration.FileConfigure;
import cat.nyaa.nyaacore.configuration.ISerializable;
import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wordconfig extends FileConfigure {

    @Serializable(name = "badwords", alias = "badwords")
    public List<String> badwords = new ArrayList<String>(Arrays.asList("fuck", "shit"));

    private PluginMain plugin;

    public Wordconfig(PluginMain pl) {
        this.plugin = pl;
    }

    @Override
    protected String getFileName() {
        return "Badwords.yml";
    }

    @Override
    protected JavaPlugin getPlugin() {
        return plugin;
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
