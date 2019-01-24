package com.imyvm.spigot.plugin.main.Tools;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class unFreeze implements CommandExecutor {
    private PluginMain plugin;

    public unFreeze(PluginMain pl) {
        plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmdObj, String label, String[] args) {
        List<UUID> uuidList = plugin.freeze.uuidList;
        if (!sender.hasPermission("imyvmcommand.unfreeze")){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4权限不足！"));
            return false;
        }
        Player p = Bukkit.getPlayerExact(args[0]);
        if (p==null){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4该玩家不存在或不在线！"));
            return false;
        }
        if (!uuidList.contains(p.getUniqueId())){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4该玩家没有冻住！"));
            return false;
        }
        uuidList.remove(p.getUniqueId());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4该玩家已解冻！"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c你已被解冻！"));
        return true;
    }
}
