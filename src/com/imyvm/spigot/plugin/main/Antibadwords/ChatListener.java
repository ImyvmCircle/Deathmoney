package com.imyvm.spigot.plugin.main.Antibadwords;

import com.imyvm.spigot.plugin.main.PluginMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChatListener implements Listener {
    final private PluginMain plugin;

    public ChatListener(PluginMain pl) {
        plugin = pl;
        plugin.getServer().getPluginManager().registerEvents(this, pl);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        List<String> words = plugin.cfg.badwords;

        for (String cens: words) {
            String message = event.getMessage();
            if (message.toLowerCase().contains(cens)) {
                String replacement = plugin.cfg.replacement;
                //String smessage = event.getMessage().replaceAll("(?i)"+ cens, replacement);
                String smessage = replaceAll3(message, cens, replacement);
                event.setMessage(smessage);
            }
        }
    }

    private String replaceAll3(String input, String regex, String replacement) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);
        String result = m.replaceAll(replacement);
        return result;
    }
}
