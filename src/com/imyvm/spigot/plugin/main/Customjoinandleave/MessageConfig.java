package com.imyvm.spigot.plugin.main.Customjoinandleave;

import com.imyvm.spigot.plugin.main.PluginMain;
import cat.nyaa.nyaacore.configuration.FileConfigure;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MessageConfig extends FileConfigure {
    public HashMap<UUID, MessageData> MessageDataList = new HashMap<>();


//    @Serializable
//    public int pos = 0;


    private PluginMain plugin;

    public MessageConfig(PluginMain pl) {
        this.plugin = pl;
    }

    @Override
    protected String getFileName() {
        return "Message.yml";
    }

    @Override
    protected JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void deserialize(ConfigurationSection config) {
        MessageDataList.clear();
        ISerializable.deserialize(config, this);
        if (config.isConfigurationSection("Message")) {
            ConfigurationSection Message = config.getConfigurationSection("Message");
            for (String idx : Message.getKeys(false)) {
                MessageData tmp = new MessageData();
                tmp.deserialize(Message.getConfigurationSection(idx));
                MessageDataList.put(UUID.fromString(tmp.playerUUID), tmp);
            }
        }
    }

    @Override
    public void serialize(ConfigurationSection config) {
        for (String k : config.getKeys(false)) {
            config.set(k, null);
        }
        ISerializable.serialize(config, this);
        config.set("Message", null);
        ConfigurationSection ads = config.createSection("Message");
        for (MessageData Message : MessageDataList.values()) {
            Message.serialize(ads.createSection(Message.playerUUID));
        }
    }
}
