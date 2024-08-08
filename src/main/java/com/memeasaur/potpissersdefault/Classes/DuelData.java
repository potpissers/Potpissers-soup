package com.memeasaur.potpissersdefault.Classes;

import java.util.UUID;

public class DuelData {
    public Party party;
    public UUID playerUuid;
    public String kitName;
    public double attackSpeed;
    public String arenaName;
    public DuelData(UUID playerUuid, String kitName, double attackSpeed) {
        this.playerUuid = playerUuid;
        this.kitName = kitName;
        this.attackSpeed = attackSpeed;
    }
    public DuelData(Party party, String kitName, double attackSpeed) {
        this.party = party;
        this.kitName = kitName;
        this.attackSpeed = attackSpeed;
    }
}
