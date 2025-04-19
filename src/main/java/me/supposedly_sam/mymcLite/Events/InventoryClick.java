package me.supposedly_sam.mymcLite.Events;

import me.clip.placeholderapi.PlaceholderAPI;
import me.supposedly_sam.mymcLite.Models.GSItemClickModel;
import me.supposedly_sam.mymcLite.Models.GameSelectorItemModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class InventoryClick implements Listener {
    private final Plugin plugin;
    public InventoryClick(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        String invTitle = ChatColor.stripColor(e.getView().getTitle());
        String gameSelectorTitle = plugin.getConfig().getString("game-selector.title");

        if (gameSelectorTitle == null) {
            return;
        }

        gameSelectorTitle = ChatColor.translateAlternateColorCodes('&', gameSelectorTitle);

        if (!invTitle.equals(ChatColor.stripColor(gameSelectorTitle))) {
            return;
        }

        e.setCancelled(true);

        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        String clickedItemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        List<GSItemClickModel> clickModels = getClickModels(clickedItemName);
        if (clickModels == null || clickModels.isEmpty()) {
            return;
        }

        for (GSItemClickModel clickModel : clickModels) {
            if (!clickedItemName.equals(clickModel.getDisplayName())) {
                return;
            }

            if (e.getClick() != ClickType.valueOf(clickModel.getClick())) {
                continue;
            }

            Player p = (Player) e.getWhoClicked();
            p.performCommand(clickModel.getCommand());
            p.closeInventory();
            return;
        }
    }

    private List<GSItemClickModel> getClickModels(String clickedItemName) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("game-selector.items");
        if (section == null) return null;

        List<GSItemClickModel> clickModels = new ArrayList<>();

        for (String key: section.getKeys(false)) {
            try {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                if (itemSection == null) continue;

                String displayName = itemSection.getString("display-name");
                if (displayName == null) displayName = key;

                displayName = ChatColor.translateAlternateColorCodes('&', displayName);
                displayName = ChatColor.stripColor(displayName);

                if (!clickedItemName.equals(displayName)) {
                    continue;
                }

                ConfigurationSection clickCommandsSection = itemSection.getConfigurationSection("click-commands");
                if (clickCommandsSection == null) return null;

                if (clickCommandsSection.getString("LEFT") != null) {
                    clickModels.add(new GSItemClickModel(displayName, "LEFT", clickCommandsSection.getString("LEFT")));
                }

                if (clickCommandsSection.getString("RIGHT") != null) {
                    clickModels.add(new GSItemClickModel(displayName, "RIGHT", clickCommandsSection.getString("RIGHT")));
                }

                if (clickCommandsSection.getString("SHIFT_LEFT") != null) {
                    clickModels.add(new GSItemClickModel(displayName, "SHIFT_LEFT", clickCommandsSection.getString("RIGHT")));
                }

                if (clickCommandsSection.getString("SHIFT_RIGHT") != null) {
                    clickModels.add(new GSItemClickModel(displayName, "SHIFT_RIGHT", clickCommandsSection.getString("RIGHT")));
                }

                if (clickCommandsSection.getString("RIGHT") != null) {
                    clickModels.add(new GSItemClickModel(displayName, "RIGHT", clickCommandsSection.getString("RIGHT")));
                }

                if (clickCommandsSection.getString("DROP") != null) {
                    clickModels.add(new GSItemClickModel(displayName, "DROP", clickCommandsSection.getString("RIGHT")));
                }

            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Invalid data in config (game-selector): " + key) ;
            }
        }

        return clickModels;
    }
}
