package com.memeasaur.potpissersdefault.Classes;

import java.io.Serializable;
import java.util.HashMap;

public class ClaimsMap implements Serializable { // SPARSE REAL MATRIX!
    private final HashMap<ClaimCoordinate, String> overWorld = new HashMap<>();
    private final HashMap<ClaimCoordinate, String> nether = new HashMap<>();
    private final HashMap<ClaimCoordinate, String> the_end = new HashMap<>();
    public HashMap<ClaimCoordinate, String> getWorld(String name) {
        switch (name) {
            case "world" -> {return overWorld;}
            case "world_nether" -> {return nether;}
            case "world_the_end" -> {return the_end;}
        }
        return null;
    }
}