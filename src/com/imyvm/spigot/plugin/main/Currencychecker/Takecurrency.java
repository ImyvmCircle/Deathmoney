package com.imyvm.spigot.plugin.main.Currencychecker;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import think.rpgitems.item.ItemManager;
import think.rpgitems.item.RPGItem;

import java.util.Optional;
import java.util.UUID;

import static com.imyvm.spigot.plugin.main.PluginMain.econ;

public class Takecurrency implements CommandExecutor {
    public PluginMain plugin;

    public Takecurrency(PluginMain pl) {
        plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmdObj, String label, String[] args) {
        if (!(plugin.getServer().getPluginManager().isPluginEnabled("RPGItems")))
            return false;
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

                        ItemStack item = player.getInventory().getItemInMainHand();
                        RPGItem rpgItem = ItemManager.toRPGItem(item);
                        int max = rpgItem.getMaxDurability();
                        Optional<Integer> itemDurability = rpgItem.getDurability(item);
                        if (!(itemDurability.isPresent())){
                            return false;
                        }
                        int delta = max - itemDurability.get();
                        if (max == -1 || delta == 0)
                            return false;
                        if (econ.has(player, value)) {
                            if (player.isOp()) {
                                sender.sendMessage(ChatColor.GOLD + "氪金成功 " + String.valueOf(itemDurability.get()) + "-->" +
                                        String.valueOf(max));
                            } else {
                                econ.withdrawPlayer(player, value);
                                econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getmoneyuuid)), value);
                                sender.sendMessage(ChatColor.GOLD + "氪金成功 " + String.valueOf(itemDurability.get()) + "-->" +
                                        String.valueOf(max));
                            }
                            String repair = args[2];
                            int durability = Integer.parseInt(repair);
                            rpgItem.setDurability(item, Math.min(itemDurability.get() + durability, max));

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
