package com.imyvm.spigot.plugin.main.lootprotect;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class LootProtectListener implements Listener {
    final private PluginMain plugin;

    public LootProtectListener(PluginMain pl) {
        plugin = pl;
        plugin.getServer().getPluginManager().registerEvents(this, pl);
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMobKilled(EntityDeathEvent ev) {
        if (plugin.cfg.lootProtectMode == LootProtectMode.OFF || ev.getEntity() instanceof Player)
            return;
        Player p = null;
        if (plugin.cfg.lootProtectMode == LootProtectMode.FINAL_DAMAGE) {
            p = ev.getEntity().getKiller();
        }
        if (p == null) return;
        //if (bypassPlayer.contains(p.getUniqueId())) return;
        /* Bypass Vanilla */
        if (plugin.cfg.bypassVanilla) {
            List<ItemStack> customItems = ev.getDrops().stream().filter(item -> item.hasItemMeta() && item.getItemMeta().hasLore()).collect(Collectors.toList());
            ev.getDrops().removeAll(customItems);
            Map<Integer, ItemStack> leftItem =
                    p.getInventory().addItem(customItems.toArray(new ItemStack[0]));
            ev.getDrops().addAll(leftItem.values());
        } else {
            Map<Integer, ItemStack> leftItem =
                    p.getInventory().addItem(ev.getDrops().toArray(new ItemStack[0]));
            ev.getDrops().clear();
            ev.getDrops().addAll(leftItem.values());
        }
    }


}
