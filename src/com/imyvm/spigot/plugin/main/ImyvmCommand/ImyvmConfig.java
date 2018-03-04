package com.imyvm.spigot.plugin.main.ImyvmCommand;

import cat.nyaa.nyaacore.configuration.FileConfigure;
import cat.nyaa.nyaacore.configuration.ISerializable;
import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ImyvmConfig extends FileConfigure {

    public HashMap<String, ImyvmData> ImyvmDataList = new HashMap<>();


    private PluginMain plugin;

    public ImyvmConfig(PluginMain pl) {
        this.plugin = pl;
    }

    @Override
    protected String getFileName() {
        return "Imyvm.yml";
    }

    @Override
    protected JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void deserialize(ConfigurationSection config) {
        ImyvmDataList.clear();
        ISerializable.deserialize(config, this);
        if (config.isConfigurationSection("Imyvm")) {
            ConfigurationSection Imyvm = config.getConfigurationSection("Imyvm");
            for (String idx : Imyvm.getKeys(false)) {
                ImyvmData tmp = new ImyvmData();
                tmp.deserialize(Imyvm.getConfigurationSection(idx));
                ImyvmDataList.put(tmp.name, tmp);
            }
        }
    }

    @Override
    public void serialize(ConfigurationSection config) {
        for (String k : config.getKeys(false)) {
            config.set(k, null);
        }
        ISerializable.serialize(config, this);
        config.set("Imyvm", null);
        ConfigurationSection ads = config.createSection("Imyvm");
        for (ImyvmData imyvm : ImyvmDataList.values()) {
            imyvm.serialize(ads.createSection(imyvm.name));
        }
    }
}
