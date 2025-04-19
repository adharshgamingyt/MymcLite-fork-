package me.supposedly_sam.mymcLite.Events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlayerInteract implements Listener {
    private final Plugin plugin;
    public PlayerInteract(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() == null) return;

            ItemStack hotbarItem = e.getItem();

            if (isGSelHotbarItem(hotbarItem, p)){
                p.performCommand("gsel");
            }
        }
    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent e) {
        Player p = e.getPlayer();

        ItemStack dropItem = e.getItemDrop().getItemStack();
        if (isGSelHotbarItem(dropItem, p)){
            e.setCancelled(true);
        }
    }

    private boolean isGSelHotbarItem(ItemStack item, Player p) {
        String configDisplayName = plugin.getConfig().getString("game-selector.hotbar-item.display-name");
        if (configDisplayName == null) return false;

        configDisplayName = PlaceholderAPI.setPlaceholders(p, configDisplayName);
        configDisplayName = ChatColor.translateAlternateColorCodes('&', configDisplayName);
        configDisplayName = ChatColor.stripColor(configDisplayName);

        String hotbarItemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

        return configDisplayName.equals(hotbarItemName);
    }
}
