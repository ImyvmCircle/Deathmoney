package com.imyvm.spigot.plugin.main.Currencychecker;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import think.rpgitems.data.RPGMetadata;

import static com.imyvm.spigot.plugin.main.PluginMain.econ;
import static think.rpgitems.item.RPGItem.getMetadata;
import static think.rpgitems.item.RPGItem.updateItem;

public class Takecurrency implements CommandExecutor {
    PluginMain plugin;

    public Takecurrency(PluginMain pl) {
        plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmdObj, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "That is not a valid command!");
        } else {
            String cmd = args[0];
            if (cmd.equalsIgnoreCase("take")) {
                if (!(args.length == 3) || !sender.hasPermission("checker.take")) {
                    sender.sendMessage(ChatColor.RED + "That is not a valid command!");
                } else {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        String amount = args[1];
                        double value = Double.parseDouble(amount);
                        if (econ.has(player, value)) {
                            if (player.isOp()) {
                                sender.sendMessage(ChatColor.GOLD + "氪金成功!");
                            } else {
                                econ.withdrawPlayer(player, value);
                                sender.sendMessage(ChatColor.GOLD + "氪金成功!");
                            }
                            String repair = args[2];
                            ItemStack item = player.getInventory().getItemInMainHand();
                            int durability = Integer.parseInt(repair);
                            RPGMetadata meta = getMetadata(item);
                            meta.put(RPGMetadata.DURABILITY, durability);
                            updateItem(item, meta);
                        } else {
                            sender.sendMessage(ChatColor.RED + "没钱请不要氪金!");
                        }

                    } else {
                        sender.sendMessage(ChatColor.RED + "You must be a player to do that!");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "That is not a valid command!");
            }
        }
        return true;
    }
}
