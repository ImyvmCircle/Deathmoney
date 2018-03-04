package com.imyvm.spigot.plugin.main.Customjoinandleave;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class JoinLeaveListenner implements Listener{
    final private PluginMain plugin;


    public JoinLeaveListenner(PluginMain pl) {
        plugin = pl;
        plugin.getServer().getPluginManager().registerEvents(this, pl);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(plugin.cfg.MessageConfig.MessageDataList.containsKey(player.getUniqueId())){
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.cfg.MessageConfig.MessageDataList.get(player.getUniqueId()).join.replace("{player}", player.getDisplayName())));
        }else {
            event.setJoinMessage(player.getDisplayName()+" joined the game");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event1) {
        Player player = event1.getPlayer();
        if (plugin.cfg.MessageConfig.MessageDataList.containsKey(player.getUniqueId())) {
            event1.setQuitMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.cfg.MessageConfig.MessageDataList.get(player.getUniqueId()).leave.replace("{player}", player.getDisplayName())));
        } else {
            event1.setQuitMessage(player.getDisplayName() + " left the game");
        }
    }
}
