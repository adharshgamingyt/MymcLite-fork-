package com.adharsh.mymcLite.Commands;

import com.adharsh.mymcLite.Utils.Responses;
import com.adharsh.mymcLite.Utils.SpawnFile;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SetSpawn implements CommandExecutor, TabCompleter {

    private final Plugin plugin;

    public SetSpawn(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player p)) {
            Responses.sendNonPlayerExecution(sender);
            return true;
        }

        if (!p.hasPermission("mymclite.setspawn")) {
            Responses.sendNotAuthorized(p);
            return true;
        }

        setSpawnMethod(p);
        return true;
    }

    private void setSpawnMethod(Player p) {
        Location playerLoc = p.getLocation();
        double centeredX = playerLoc.getBlockX() + 0.5;
        double centeredZ = playerLoc.getBlockZ() + 0.5;
        Location centeredLocation = new Location(playerLoc.getWorld(), centeredX, playerLoc.getY(), centeredZ, playerLoc.getYaw(), playerLoc.getPitch());

        p.getWorld().setSpawnLocation(centeredLocation);

        FileConfiguration spawnFile = SpawnFile.getSpawnFile();
        spawnFile.set("spawn.world", centeredLocation.getWorld().getName());
        spawnFile.set("spawn.x", centeredLocation.getX());
        spawnFile.set("spawn.y", centeredLocation.getY());
        spawnFile.set("spawn.z", centeredLocation.getZ());
        spawnFile.set("spawn.yaw", centeredLocation.getYaw());
        spawnFile.set("spawn.pitch", centeredLocation.getPitch());

        try {
            SpawnFile.saveSpawnFile();
        } catch (IOException ex) {
            ex.printStackTrace();
            plugin.getLogger().warning("Error saving spawn.yml");
        }


        plugin.getLogger().info("Spawn location set [X: " + centeredLocation.getX() + " | Y: " + centeredLocation.getY() + " | Z: " + centeredLocation.getZ() + "]");
        p.sendMessage(ChatColor.GREEN + "Spawn location set at, " + ChatColor.YELLOW +  "[X: " + centeredLocation.getX() + " | Y: " + centeredLocation.getY() + " | Z: " + centeredLocation.getZ() + "]");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
