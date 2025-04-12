package me.supposedly_sam.mymcLite.Commands;

import me.supposedly_sam.mymcLite.Utils.Responses;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            plugin.getLogger().warning("'/setspawn' can only be executed by a player.");
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
        plugin.getConfig().set("spawn.spawn-location", centeredLocation);

        plugin.getLogger().info("Spawn location set [X: " + centeredLocation.getX() + " | Y: " + centeredLocation.getY() + " | Z: " + centeredLocation.getZ() + "]");
        p.sendMessage(ChatColor.GREEN + "Spawn location set at, [X: " + centeredLocation.getX() + " | Y: " + centeredLocation.getY() + " | Z: " + centeredLocation.getZ() + "]");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
