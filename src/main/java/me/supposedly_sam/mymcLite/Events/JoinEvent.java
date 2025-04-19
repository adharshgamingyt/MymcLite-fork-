package me.supposedly_sam.mymcLite.Events;

import com.google.common.collect.Iterables;
import me.supposedly_sam.mymcLite.Commands.Spawn;
import me.supposedly_sam.mymcLite.Utils.SpawnFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class JoinEvent implements Listener {

    private final Plugin plugin;
    private static ArrayList<String> servers;
    private static boolean requestedServerList = false;

    public JoinEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    public static ArrayList<String> getServers() {
        return servers;
    }
    public static void setServers(ArrayList<String> servers) {
        JoinEvent.servers = servers;
    }

    public boolean hasRequestedServerList() {
        return requestedServerList;
    }
    public static void setRequestedServerList(boolean value) {
        requestedServerList = value;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        if (plugin.getConfig().getBoolean("spawn.force-spawn-on-join")) {
            sendPlayerToSpawn(player);
        }

        // Send Get servers in bungee network plugin message
        if (!hasRequestedServerList()) {
            sendGetServers(player);
        }
    }

    private void sendGetServers(Player player) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("GetServers");

            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());

            out.close();
            b.close();
        } catch (IOException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }

    private void sendPlayerToSpawn(Player player) {
        Location spawnLoc = Spawn.getSpawnLocation();
        if (spawnLoc == null) {
            return;
        }
        player.teleport(spawnLoc);
    }
}
