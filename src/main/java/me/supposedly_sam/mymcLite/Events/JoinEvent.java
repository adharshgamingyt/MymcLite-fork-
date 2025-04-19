package me.supposedly_sam.mymcLite.Events;

import com.google.common.collect.Iterables;
import me.clip.placeholderapi.PlaceholderAPI;
import me.supposedly_sam.mymcLite.Commands.Spawn;
import me.supposedly_sam.mymcLite.Utils.SpawnFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        // Join Message
        String joinMessage = plugin.getConfig().getString("join-message");
        if (joinMessage != null && !joinMessage.isBlank()) {
            joinMessage = PlaceholderAPI.setPlaceholders(player, joinMessage);
            joinMessage = ChatColor.translateAlternateColorCodes('&', joinMessage);
            e.setJoinMessage(joinMessage);
        }

        // Force spawn
        if (plugin.getConfig().getBoolean("spawn.force-spawn-on-join")) {
            sendPlayerToSpawn(player);
        }

        // Send Get servers in bungee network plugin message
        if (!hasRequestedServerList()) {
            sendGetServers(player);
        }

        // Game selector item
        if (plugin.getConfig().getBoolean("game-selector.hotbar-item.enabled")) {
            setHotbarItem(player);
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

    private void setHotbarItem(Player player) {
        int slot = plugin.getConfig().getInt("game-selector.hotbar-item.hotbar-slot");
        if (slot < 0 || slot > 8) slot = 0;

        String hotbarMaterialString = plugin.getConfig().getString("game-selector.hotbar-item.material");
        if (hotbarMaterialString == null) return;

        Material hotbarMaterial = Material.getMaterial(hotbarMaterialString);
        if (hotbarMaterial == null) return;

        String hotbarItemName = plugin.getConfig().getString("game-selector.hotbar-item.display-name");
        if (hotbarItemName == null) hotbarItemName = "Game Selector";

        hotbarItemName = PlaceholderAPI.setPlaceholders(player, hotbarItemName);
        hotbarItemName = ChatColor.translateAlternateColorCodes('&', hotbarItemName);

        ItemStack hotbarItem = new ItemStack(hotbarMaterial, 1);
        ItemMeta hotbarMeta = hotbarItem.getItemMeta();
        hotbarMeta.setDisplayName(hotbarItemName);
        hotbarItem.setItemMeta(hotbarMeta);

        player.getInventory().setItem(slot, hotbarItem);
    }
}
