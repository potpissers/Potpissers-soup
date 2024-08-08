package com.memeasaur.potpissersdefault.Util.Methods;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.LocationCoordinate;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.CLAIM_BLOCK_TIMER;
import static com.memeasaur.potpissersdefault.Util.Constants.SerializationConstants.DATA_CLAIMS_MAP;
import static com.memeasaur.potpissersdefault.Util.Methods.SerializationUtils.writeBinary;

public class ClaimUtils {
    public static boolean doClaimArea(Player p, String object, Plugin plugin) {
        if (claimPos1 == null || claimPos2 == null) {
            p.sendMessage("?");
            return true;
        }
        String worldName = p.getWorld().getName();
        HashMap<ClaimCoordinate, String> worldClaim = claims.getWorld(worldName);
        for (int x = Math.min(claimPos1.getBlockX(), claimPos2.getBlockX()); x < Math.max(claimPos1.getBlockX(), claimPos2.getBlockX()); x++)
            for (int z = Math.min(claimPos1.getBlockZ(), claimPos2.getBlockZ()); z < Math.max(claimPos1.getBlockZ(), claimPos2.getBlockZ()); z++) {
                if (worldClaim.putIfAbsent(new ClaimCoordinate(x, z), object) != null)
                    p.sendMessage("claim iteration blocked by existing claim");
            }
        handleSaveClaimsMap(plugin);
        p.sendMessage(object + " claim complete");
        return true;
    }

    public static boolean doUnclaimArea(Player p, String object, Plugin plugin) {
        HashMap<ClaimCoordinate, String> worldClaim = claims.getWorld(p.getWorld().getName());
        Iterator<Map.Entry<ClaimCoordinate, String>> claimIterator = worldClaim.entrySet().iterator();

        while (claimIterator.hasNext()) {
            Map.Entry<ClaimCoordinate, String> entry = claimIterator.next();
            if (entry.getValue().equals(object))
                claimIterator.remove(); // make async??
        }

        p.sendMessage("done");
        handleSaveClaimsMap(plugin);
        return true;
    }

    public static boolean doClaimSet(String[] args, Player p, boolean claim1) {
        Location location = p.getLocation();
        if (args.length == 0) {
            if (claim1) claimPos1 = location;
            else claimPos2 = location;
            p.sendMessage(location.toVector().toBlockVector().toString());
            return true;
        }
        if (args.length == 1) {
            p.sendMessage("havent added parsing soz");
            // parse coords
            return true;
        }
        if (args.length == 2) {
            p.sendMessage("this uses your current world, it's also gutted atm soz");
//            if (claim1) claimPos1 = new LocationCoordinate(Integer.parseInt(args[0]), 64, Integer.parseInt(args[1]));
//            else claimPos2 = new LocationCoordinate(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            return true;
        } else p.sendMessage("?");
        return true;
    }

    public static void doChunkClaim(Chunk chunk, String team, boolean claim) { // PASS HASHMAP?!?!
        HashMap<ClaimCoordinate, String> worldClaim = claims.getWorld(chunk.getWorld().getName());
        if (claim)
            for (int x = 0; x < 16; x++)
                for (int z = 0; z < 16; z++)
                    worldClaim.put(new ClaimCoordinate(chunk.getBlock(x, 64, z)), team);
        else
            for (int x = 0; x < 16; x++)
                for (int z = 0; z < 16; z++)
                    worldClaim.put(new ClaimCoordinate(chunk.getBlock(x, 64, z)), team);
    }

    public static void handleSaveClaimsMap(Plugin plugin) {
        if (saveClaimsTask == null || saveClaimsTask.isCancelled())
            saveClaimsTask = getSaveClaimsTask(plugin);
        else {
            saveClaimsTask.cancel();
            saveClaimsTask = getSaveClaimsTask(plugin);
        }
    }

    static BukkitTask getSaveClaimsTask(Plugin plugin) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                writeBinary(DATA_CLAIMS_MAP, claims);
                saveClaimsTask = null;
                synchronized (lock) {
                    lock.notify();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
    public static void handleClaimBlockTask(Block block, Material originaBlockMaterial, Plugin plugin) {
        LocationCoordinate locationCoordinate = new LocationCoordinate(block);
        int[] timer = new int[]{CLAIM_BLOCK_TIMER};
        claimsBlockMap.put(locationCoordinate, timer);
        new BukkitRunnable() {
            final World world = block.getWorld();
            @Override
            public void run() {
                if (world.getBlockAt(locationCoordinate.toLocation()).getType().equals(originaBlockMaterial)) {
                    claimsBlockMap.remove(locationCoordinate);
                    cancel();
                    return;
                }
                else if (timer[0] != 0)
                    timer[0]--;
                else {
                    world.getBlockAt(locationCoordinate.toLocation()).setType(originaBlockMaterial);
                    claimsBlockMap.remove(locationCoordinate);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
}
