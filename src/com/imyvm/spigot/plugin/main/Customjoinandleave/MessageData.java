package com.imyvm.spigot.plugin.main.Customjoinandleave;

import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageData implements ISerializable {
    @Serializable
    public String id;
   @Serializable
    public String playerUUID;
    @Serializable
    public String join;
    @Serializable
    public String leave;
//    @Serializable
//    public boolean joinorleave;

    public MessageData() {
    }

    public MessageData(UUID player, Player id, String Text, String Text1) {
        this.id = id.getName();
        setUUID(player);
        this.join = Text;
        this.leave = Text1;
    }

    public UUID getUUID() {
        return UUID.fromString(playerUUID);
    }

    public void setUUID(UUID uuid) {
        this.playerUUID = uuid.toString();
    }

}