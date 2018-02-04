package com.imyvm.spigot.plugin.main.Customjoinandleave;


import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.imyvm.spigot.plugin.main.PluginMain.econ;

public class Commands implements CommandExecutor {

    PluginMain plugin;

    public Commands(PluginMain pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmdObj, String label, String[] args) {
        label = label.toLowerCase();
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + " --- Custom Join and Quit Message Help --- ");
            sender.sendMessage(ChatColor.GOLD + "/" + label + " add" +" [Join Message]"+" [Leave Message]  -"+
                    ChatColor.translateAlternateColorCodes('&',plugin.cfg.addcommandsmessage));
            sender.sendMessage(ChatColor.GOLD + "/" + label + " revoke  -"+
                    ChatColor.translateAlternateColorCodes('&',plugin.cfg.revokecommandsmessage));
            sender.sendMessage(ChatColor.GOLD + " ------------------------ ");
        } else {
            String cmd = args[0];
            if (cmd.equalsIgnoreCase("add")) {
                if (sender.hasPermission("custom.add")) {
                    if (!(args.length == 3)) {
                        sender.sendMessage(ChatColor.GOLD + "/" + label + " add" +" [Join Message]"+" [Leave Message]  -"+
                                ChatColor.translateAlternateColorCodes('&',plugin.cfg.addcommandsmessage));
                    } else {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            double displayAmount = args[1].length()+args[2].length();
                            double totalprice = plugin.cfg.messageprice*displayAmount;
                            if (econ.has(player, totalprice) || player.isOp()){
                                plugin.cfg.MessageConfig.MessageDataList.put(player.getUniqueId(),
                                        new MessageData(player.getUniqueId(), player, args[1], args[2]));
                                plugin.cfg.MessageConfig.save();
                                if (!(player.isOp())){
                                    econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getmoneyuuid)),totalprice);
                                    econ.withdrawPlayer(player, totalprice);
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.cfg.setupsuccess)+
                                            econ.format(totalprice)+ChatColor.translateAlternateColorCodes('&',plugin.cfg.Curname));
                                }else {
                                    sender.sendMessage(ChatColor.BLUE +"Setup Success");
                                }
                            }else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.cfg.setupnomoney)+
                                        econ.format(totalprice)+ChatColor.translateAlternateColorCodes('&',plugin.cfg.Curname));
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "You must be a player to do that!");
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to do that!");
                }
            } else if (cmd.equalsIgnoreCase("revoke")) {
                if (sender.hasPermission("custom.revoke") && args.length == 1) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (plugin.cfg.MessageConfig.MessageDataList.containsKey(player.getUniqueId())) {
                            //MessageData md = plugin.cfg.MessageConfig.MessageDataList.get(player.getUniqueId());
                            // plugin.getLogger().info(I18n.format("log.info.ads_remove"));
                            plugin.cfg.MessageConfig.MessageDataList.remove(player.getUniqueId());
                            plugin.cfg.MessageConfig.save();
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.cfg.revokesuccess));
                        }else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.cfg.revokefail));
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You must be a player to do that!");
                    }
                } else {
                    sender.sendMessage(ChatColor.GOLD + "/" + label + " revoke  -"+
                            ChatColor.translateAlternateColorCodes('&',plugin.cfg.revokecommandsmessage));
                }
            }else {
                    sender.sendMessage(ChatColor.RED + "That is not a valid command!");
                    sender.sendMessage(ChatColor.RED + "Please type /" + label + " to see the help menu!");
                }
            }
            return true;
    }
}