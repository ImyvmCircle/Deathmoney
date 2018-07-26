package com.imyvm.spigot.plugin.main;

import cat.nyaa.nyaacore.configuration.PluginConfigure;
import cat.nyaa.nyaacore.configuration.ISerializable;
import com.imyvm.spigot.plugin.main.Antibadwords.Wordconfig;
import com.imyvm.spigot.plugin.main.Customjoinandleave.MessageConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Configuration extends PluginConfigure {
    @Serializable
    public String language = "en_US";

    @Serializable(name = "getmoneyuuid")
    public String getmoneyuuid = "db417445-39cd-4b2e-8c58-0e8d7b2f864a";
    @Serializable(name = "getdeathmoneyuuid")
    public String getdeathmoneyuuid = "a641c611-21ef-4b71-b327-e45ef8fdf647";

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

    /*CustomMessage*/
    @Serializable(name = "messageprice")
    public double messageprice = 100;
    @Serializable(name = "addcommandsmessage")
    public String addcommandsmessage = "添加自定义消息";
    @Serializable(name = "revokecommandsmessage")
    public String revokecommandsmessage = "删除自定义消息";
    @Serializable(name = "setupsuccess")
    public String setupsuccess = "设置成功，花费 ";
    @Serializable(name = "setupnomoney")
    public String setupnomoney = "设置失败，余额不足 ";
    @Serializable(name = "revokesuccess")
    public String revokesuccess = "移除成功";
    @Serializable(name = "revokefail")
    public String revokefail = "移除失败";

    /*Anti-BadWords*/
    @Serializable(name = "replacement", alias = "replacement")
    public String replacement = "***";



    private final PluginMain plugin;

    @Override
    protected JavaPlugin getPlugin() {
        return plugin;
    }

    @StandaloneConfig
    public MessageConfig MessageConfig;

    @StandaloneConfig
    public Wordconfig wordconfig;


    public Configuration(PluginMain plugin) {
        this.plugin = plugin;
        this.MessageConfig = new MessageConfig(plugin);
        this.wordconfig = new Wordconfig(plugin);
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
