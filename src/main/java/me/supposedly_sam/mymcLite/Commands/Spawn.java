package me.supposedly_sam.mymcLite.Commands;

import me.supposedly_sam.mymcLite.Utils.HelperFunctions;
import me.supposedly_sam.mymcLite.Utils.Responses;
import me.supposedly_sam.mymcLite.Utils.SpawnFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Spawn implements CommandExecutor {

    private static Plugin plugin;
    public Spawn(Plugin plugin) {
        Spawn.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!plugin.getConfig().getBoolean("spawn.command-enabled")) {
            Responses.sendDisabledCommand(sender);
            return true;
        }

        if (sender instanceof Player p) {
            String spawnServer = plugin.getConfig().getString("spawn.send-to-server");
            if (spawnServer != null && !spawnServer.isBlank()) {
                p.performCommand("goto " + spawnServer);
                return true;
            }

            Location spawnLoc = getSpawnLocation();
            if (spawnLoc == null) {
                spawnLoc = p.getWorld().getSpawnLocation();
            }

            if (args.length == 0) {
                p.teleport(spawnLoc);
                p.sendMessage(ChatColor.GREEN + "Teleported to spawn");
            } else if (args.length == 1) {
                if (!p.hasPermission("mymclite.spawn.others")) {
                    Responses.sendNotAuthorized(p);
                    return true;
                }

                String targetName = args[0];
                if (!HelperFunctions.isPlayerOnline(p, targetName)) {
                    return true;
                }

                Player target = Bukkit.getPlayerExact(targetName);
                target.teleport(spawnLoc);
            } else {
                Responses.sendCommandUsage(p, "/spawn [player]");
            }
        }
        return true;
    }

    public static Location getSpawnLocation() {
        FileConfiguration spawnFile = SpawnFile.getSpawnFile();

        if (!spawnFile.contains("spawn.world")) {
            plugin.getLogger().warning("Spawn location not set, using world spawn");
            return null;
        }

        World world = Bukkit.getWorld(spawnFile.getString("spawn.world"));
        if (world == null) {
            plugin.getLogger().warning("World not found: " + spawnFile.getString("spawn.world"));
            return null;
        }

        double x = spawnFile.getDouble("spawn.x");
        double y = spawnFile.getDouble("spawn.y");
        double z = spawnFile.getDouble("spawn.z");
        float yaw = (float) spawnFile.getDouble("spawn.yaw");
        float pitch = (float) spawnFile.getDouble("spawn.pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }
}
