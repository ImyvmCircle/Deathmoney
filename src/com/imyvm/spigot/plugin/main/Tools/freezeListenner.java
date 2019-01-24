package com.imyvm.spigot.plugin.main.Tools;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.UUID;

public class freezeListenner implements Listener {

    private PluginMain plugin;

    public freezeListenner(PluginMain pl) {
        plugin = pl;
        plugin.getServer().getPluginManager().registerEvents(this, pl);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        List<UUID> uuidList = plugin.freeze.uuidList;
        if (uuidList.contains(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        List<UUID> uuidList = plugin.freeze.uuidList;
        if (uuidList.contains(event.getPlayer().getUniqueId()) && !(event.getPlayer().hasPermission("imyvmcommand.unfreeze"))){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        List<UUID> uuidList = plugin.freeze.uuidList;
        if (uuidList.contains(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }
}
