package me.supposedly_sam.mymcLite.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class JoinEvent implements Listener {

    private Plugin plugin;

    public JoinEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        if (plugin.getConfig().getBoolean("spawn.force-spawn-on-join")) {
            sendToSpawn(player);
        }

    }

    private void sendToSpawn(Player player) {
        Location setSpawnLoc = plugin.getConfig().getLocation("spawn.spawn-location");
        if ( setSpawnLoc == null) {
            return;
        }

        player.teleport(setSpawnLoc);
    }
}
