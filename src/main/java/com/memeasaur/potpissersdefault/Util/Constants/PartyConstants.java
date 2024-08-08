package com.memeasaur.potpissersdefault.Util.Constants;

import com.memeasaur.potpissersdefault.Classes.Party;
import net.kyori.adventure.text.Component;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class PartyConstants {
    public static final WeakReference<Party> NULL_PARTY = new WeakReference<>(null);
    public static final String COLEADER_STRING = "leader";
    public static final String OFFICER_STRING = "officer";
    public static final String MEMBER_STRING = "member";
    public static final HashMap<String, Integer> PARTY_PERMS_MAP = new HashMap<>(Map.of(COLEADER_STRING, 2, OFFICER_STRING, 1, MEMBER_STRING, 0));
    public static final String PARTY_USAGE = "Usage: /party create/disband OR invite/kick/accept (player)";
    public static final Component UN_ALLY_MSG = Component.text(" has ended your alliance with ");
    public static final Component UN_ALLY_MSG_ARG = Component.text("alliance ended with ");
    public static final Component UN_ENEMY_MSG = Component.text(" has removed ");
    public static final Component UN_ENEMY_MSG_1 = Component.text(" as an enemy");
    public static final Component UN_ENEMY_MSG_ARG = Component.text(" has removed your party as an enemy");
    public static final int RALLY_TIMER = 30;
    public static final int RALLY_TIMER_TICK = RALLY_TIMER * 20;
}
