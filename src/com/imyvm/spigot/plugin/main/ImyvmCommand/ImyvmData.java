package com.imyvm.spigot.plugin.main.ImyvmCommand;

import cat.nyaa.nyaacore.configuration.ISerializable;

import java.util.ArrayList;
import java.util.List;

public class ImyvmData implements ISerializable {

    @Serializable
    public String name;
    @Serializable
    public List<String> command = new ArrayList<>();


    public ImyvmData() {
    }

    public ImyvmData(String name, List<String> command) {
        this.name = name;
        this.command = command;
    }
}
