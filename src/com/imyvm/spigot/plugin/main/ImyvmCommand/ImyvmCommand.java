package com.imyvm.spigot.plugin.main.ImyvmCommand;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.imyvm.spigot.plugin.main.PluginMain.econ;
import static org.apache.commons.lang.StringUtils.isBlank;

public class ImyvmCommand implements CommandExecutor{
    private PluginMain plugin;

    public ImyvmCommand(PluginMain pl) {
        plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmdObj, String label, String[] args) {
        if (!sender.hasPermission("ic.command.*") && !sender.hasPermission("ic.command.tp")) {
            sender.sendMessage("You don't have the permissions!");
            return false;
        }
        else if(args.length <= 1){
            sender.sendMessage(ChatColor.GOLD +"------ ImyvmCommand Help ------ ");
            sender.sendMessage(ChatColor.GOLD + "/" + label + " [name]" +" create  -"+
                    ChatColor.translateAlternateColorCodes('&',"&b创建指令集"));
            sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] revoke  -"+
                    ChatColor.translateAlternateColorCodes('&',"&b删除指令集"));
            sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] add [command]  -"+
                    ChatColor.translateAlternateColorCodes('&',"&b添加指定至指令集"));
            sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] remove [index]  -"+
                    ChatColor.translateAlternateColorCodes('&',"&b删除指定索引指令"));
            sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] set [index] [command]  -"+
                    ChatColor.translateAlternateColorCodes('&',"&b修改指定索引指令"));
            sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] view  -"+
                    ChatColor.translateAlternateColorCodes('&',"&b查看指令集"));
            sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] ex/execute [player/TRUE]  -"+
                    ChatColor.translateAlternateColorCodes('&',"&b执行指令集"));
            sender.sendMessage(ChatColor.GOLD + " ------------------------ ");
            return false;
        }
        else if (args[1].equalsIgnoreCase("create") && sender.hasPermission("ic.command.*")) {
            if(args.length==2){
                String name = args[0];
                if (plugin.cfg.imyvmConfig.ImyvmDataList.containsKey(name)) {
                    sender.sendMessage("指令集 ["+args[0]+"] 已存在");
                } else {
                    List<String> command = new ArrayList<>();
                    ImyvmData mydata = new ImyvmData(name, command);
                    plugin.cfg.imyvmConfig.ImyvmDataList.put(name, mydata);
                    plugin.cfg.imyvmConfig.save();
                    sender.sendMessage("指令集 ["+args[0]+"] 已创建");
                }
                return true;
            }else {
                sender.sendMessage(ChatColor.GOLD + "/" + label + " [name]" +" create  -"+
                        ChatColor.translateAlternateColorCodes('&',"&b创建指令集"));
            }
            return false;
        }
        else if (plugin.cfg.imyvmConfig.ImyvmDataList.containsKey(args[0]) && args[1].equalsIgnoreCase
                ("add") && sender.hasPermission("ic.command.*")) {
            if(args.length==2){
                sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] add [command]  -"+
                        ChatColor.translateAlternateColorCodes('&',"&b添加指定至指令集"));
            }else {
                String s = stringCommand(args, 2);
                List<String> command = plugin.cfg.imyvmConfig.ImyvmDataList.get(args[0]).command;
                command.add(s);
                plugin.cfg.imyvmConfig.ImyvmDataList.put(args[0], new ImyvmData(args[0], command));
                plugin.cfg.imyvmConfig.save();
                sender.sendMessage("指令 "+ s + " 已添加至指令集 ["+args[0]+"] ");
            }
            return true;
        }
        else if (args[1].equalsIgnoreCase("revoke") && sender.hasPermission("ic.command.*")) {
            if(args.length==2){
                if (plugin.cfg.imyvmConfig.ImyvmDataList.containsKey(args[0])) {
                    plugin.cfg.imyvmConfig.ImyvmDataList.remove(args[0]);
                    plugin.cfg.imyvmConfig.save();
                    sender.sendMessage("指令集 ["+args[0]+"] 已删除");
                    return true;
                } else {
                    sender.sendMessage("指令集["+args[0]+"]不存在");
                    return true;
                }
            }else {
                sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] revoke  -"+
                        ChatColor.translateAlternateColorCodes('&',"&b删除指令集"));
                return true;
            }
        }
        else if (plugin.cfg.imyvmConfig.ImyvmDataList.containsKey(args[0]) && args[1].equalsIgnoreCase
                ("remove") && sender.hasPermission("ic.command.*")) {
            if (args.length==3 && isPureDigital(args[2])){
                int index = Integer.parseInt(args[2]);
                List<String> command = plugin.cfg.imyvmConfig.ImyvmDataList.get(args[0]).command;
                if (index < command.size()) {
                    command.remove(index);
                    plugin.cfg.imyvmConfig.ImyvmDataList.put(args[0], new ImyvmData(args[0], command));
                    plugin.cfg.imyvmConfig.save();
                    sender.sendMessage("指令集 ["+args[0]+"] 的索引 "+args[2]+" 已删除");
                } else {
                    sender.sendMessage("超出索引");
                }
                return true;
            }else {
                sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] remove [index]  -"+
                        ChatColor.translateAlternateColorCodes('&',"&b删除指定索引指令"));
                return true;
            }
        }
        else if (plugin.cfg.imyvmConfig.ImyvmDataList.containsKey(args[0]) && args[1].equalsIgnoreCase
                ("set") && sender.hasPermission("ic.command.*")){
            if (args.length<=3 || !isPureDigital(args[2])){
                sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] set [index(Int)] [command]  -"+
                        ChatColor.translateAlternateColorCodes('&',"&b修改指定索引指令"));
            }else {
                int index = Integer.parseInt(args[2]);
                List<String> command = plugin.cfg.imyvmConfig.ImyvmDataList.get(args[0]).command;
                if (index < command.size()) {
                    String s = stringCommand(args, 3);
                    command.set(index, s);
                    plugin.cfg.imyvmConfig.ImyvmDataList.put(args[0], new ImyvmData(args[0], command));
                    plugin.cfg.imyvmConfig.save();
                    sender.sendMessage("指令集 ["+args[0]+"] 的索引 "+args[2]+" 已更改");
                }else {
                    sender.sendMessage("超出索引");
                }
            }
            return true;
        }
        else if (args[1].equalsIgnoreCase("view") && sender.hasPermission("ic.command.*")){
            if (args.length==2){
                if (plugin.cfg.imyvmConfig.ImyvmDataList.containsKey(args[0])) {
                    List<String> command = plugin.cfg.imyvmConfig.ImyvmDataList.get(args[0]).command;
                    sender.sendMessage("指令集 ["+args[0]+"] ");
                    sender.sendMessage("-------------------");
                    for (String ss:command){
                        sender.sendMessage(" - /"+ss);
                    }
                } else {
                    sender.sendMessage("指令集 ["+args[0]+"] 不存在");
                }
            }else {
                sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] view  -"+
                        ChatColor.translateAlternateColorCodes('&',"&b查看指令集"));
            }
            return true;
        }
        else if ((args[1].equalsIgnoreCase("execute") || args[1].equalsIgnoreCase
                ("ex")) && sender.hasPermission("ic.command.*")){
            if(args.length==3){
                if (plugin.cfg.imyvmConfig.ImyvmDataList.containsKey(args[0])) {
                    List<String> command = plugin.cfg.imyvmConfig.ImyvmDataList.get(args[0]).command;
                    for (String ss:command){
                        if (ss.contains("{player}")){
                            Bukkit.dispatchCommand(sender, ss.replace("{player}", args[2]));
                        }else {
                            Bukkit.dispatchCommand(sender, ss);
                        }
                    }
                    sender.sendMessage("指令集 ["+args[0]+"] 已执行");
                    return true;
                } else {
                    sender.sendMessage("指令集 ["+args[0]+"] 不存在");
                }
            }else {
                sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] ex/execute [player/TRUE]  -"+
                        ChatColor.translateAlternateColorCodes('&',"&b执行指令集"));
            }
            return true;
        }
        else if ((args[1].equalsIgnoreCase("teleport") || args[1].equalsIgnoreCase
                ("tp")) && sender.hasPermission("ic.command.tp")){
            if(args.length==2){
                if (plugin.cfg.imyvmConfig.ImyvmDataList.containsKey(args[0])) {
                    List<String> command = plugin.cfg.imyvmConfig.ImyvmDataList.get(args[0]).command;
                    for (String ss:command){
                        Location location = getLiteLocationFromString(ss);
                        Player player = (Player) sender;
                        player.teleport(location);
                    }
                    return true;
                } else {
                    sender.sendMessage("指令集 ["+args[0]+"] 不存在");
                }
            }else {
                sender.sendMessage(ChatColor.GOLD + "/" + label + " [name] tp/teleport  -"+
                        ChatColor.translateAlternateColorCodes('&',"&b执行传送"));
            }
            return true;
        } else if ((args[1].equalsIgnoreCase("fly") || args[1].equalsIgnoreCase
                ("tp")) && sender.hasPermission("ic.command.fly")) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player player = (Player) sender;
            if (!(plugin.cfg.deathloss_enable_world.contains(player.getWorld().getName()))) {
                player.sendMessage(ChatColor.RED + "当前世界不支持飞行");
                return false;
            }
            if (args.length == 2) {
                if (args[0].equals("onetimefly")) {
                    if (player.getAllowFlight()) {
                        player.sendMessage(ChatColor.AQUA + "你已经在飞行中！");
                        return false;
                    } else if (econ.has(player, plugin.cfg.onetimefly)) {
                        player.setAllowFlight(true);
                        econ.withdrawPlayer(player, plugin.cfg.onetimefly);
                        player.sendMessage(ChatColor.AQUA + "你已成功购买一次性飞行，花费 " +
                                plugin.cfg.onetimefly + " " + ChatColor.YELLOW + plugin.cfg.Curname);
                        econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.cfg.getmoneyuuid)),
                                plugin.cfg.onetimefly);
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "钱不够啊大哥大姐");
                        return false;
                    }
                } else {
                    sender.sendMessage("指令集 [" + args[0] + "] 不存在");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + "/" + label + " onetimefly fly  -" +
                        ChatColor.translateAlternateColorCodes('&', "&b购买飞行"));
                return false;
            }
        } else {
            sender.sendMessage(ChatColor.RED +"That is not a valid command!");
            return false;
        }
    }

    private static boolean isPureDigital(String string) {
        if (isBlank(string))
            return false;
        String regEx1 = "\\d+";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        return m.matches();
    }

    private String stringCommand(String[] args, int num){
        StringBuilder myString = new StringBuilder();
        for (int i = num; i < args.length - 1; i++) {
            String arg = args[i] + " ";
            myString.append(arg);
        }
        myString.append(args[args.length - 1]);
        return myString.toString();
    }

    private static Location getLiteLocationFromString(String s) {
        if (s == null || s.trim().equalsIgnoreCase("")) {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            World w = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }

}
