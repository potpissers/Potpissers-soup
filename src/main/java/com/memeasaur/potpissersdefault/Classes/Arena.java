package com.memeasaur.potpissersdefault.Classes;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Arena implements Serializable {
    public final String name;
    public String creator;
    public LocationCoordinate warp1;
    public LocationCoordinate warp2;
    public Arena(String name) {
        this.name = name;
        this.creator = "null";
    }
}
