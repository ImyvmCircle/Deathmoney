package com.imyvm.spigot.plugin.main.Tools;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Freeze implements CommandExecutor {

    private PluginMain plugin;

    public Freeze(PluginMain pl) {
        plugin = pl;
    }

    public List<UUID> uuidList = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command cmdObj, String label, String[] args) {
        if (!sender.hasPermission("imyvmcommand.freeze")){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4权限不足！"));
            return false;
        }

        Player p = Bukkit.getPlayerExact(args[0]);
        if (p==null){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4该玩家不存在或不在线"));
            return false;
        }
        if (p.equals((Player) sender)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4你不能冻住自己！"));
            return false;
        }
        if (uuidList.contains(p.getUniqueId())){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c该玩家已经冻住了！"));
            return false;
        }
        uuidList.add(p.getUniqueId());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c该玩家已冻住！"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c你已被冻住！"));
        return true;
    }

}
