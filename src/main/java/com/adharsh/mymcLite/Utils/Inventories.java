package com.adharsh.mymcLite.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import com.adharsh.mymcLite.Models.GameSelectorItemModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Inventories {

    private static Plugin plugin;
    public Inventories(Plugin plugin) {
        Inventories.plugin = plugin;
    }

    public Inventory getGameSelectorInventory(Player player) {
        String title = plugin.getConfig().getString("game-selector.title");
        if (title == null) {
            title = "&3Game Selector";
            plugin.getLogger().warning("Game selector title is invalid. Reverting to default");
        }
        title = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, title));

        int size = plugin.getConfig().getInt("game-selector.size");
        if (size == 0 || (size % 9 != 0)) {
            size = 27;
            plugin.getLogger().warning("Game selector size is invalid. Reverting to default");
        }

        Inventory gameSelectorInventory = Bukkit.createInventory(player, size, title);

        // Dynamic Items
        List<Integer> itemPositions = setGSelItems(player, gameSelectorInventory);
        if (itemPositions == null || itemPositions.isEmpty()){
            return null;
        }

        // Filler Glass
        if (plugin.getConfig().getBoolean("game-selector.filler-glass-enabled")){
            ItemStack fillerItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
            ItemMeta fillerMeta = fillerItem.getItemMeta();
            fillerMeta.setDisplayName(" ");
            fillerItem.setItemMeta(fillerMeta);

            for (int i = 0; i < size; i ++) {
                if (itemPositions.contains(i)){
                    continue;
                }
                gameSelectorInventory.setItem(i, fillerItem);
            }
        }

        return gameSelectorInventory;
    }

    private List<Integer> setGSelItems(Player player, Inventory gameSelectorInventory) {
        List<GameSelectorItemModel> itemModels = gSelItemModels();
        if (itemModels == null || itemModels.isEmpty()) return null;

        List<Integer> itemPositions = new ArrayList<>();

        for (GameSelectorItemModel itemModel : itemModels) {
            ItemStack itemStack = new ItemStack(itemModel.getItem(), itemModel.getItemCount());
            ItemMeta itemMeta = itemStack.getItemMeta();

            String displayName = PlaceholderAPI.setPlaceholders(player, itemModel.getDisplayName());
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

            List<String> loreFormatted = new ArrayList<>();
            for (String line : itemModel.getLore()) {
                line = PlaceholderAPI.setPlaceholders(player, line);
                loreFormatted.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            itemMeta.setLore(loreFormatted);

            if (itemModel.isEnchanted()) {
                itemMeta.addEnchant(Enchantment.CHANNELING, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            itemStack.setItemMeta(itemMeta);
            int itemPosition = itemModel.getInvPosition();
            gameSelectorInventory.setItem(itemPosition, itemStack);
            itemPositions.add(itemPosition);
        }

        return itemPositions;
    }

    private List<GameSelectorItemModel> gSelItemModels() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("game-selector.items");
        if (section == null) return null;

        List<GameSelectorItemModel> itemModels = new ArrayList<>();

        for (String key: section.getKeys(false)) {
            try {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                if (itemSection == null) continue;

                Material itemMaterial = Material.getMaterial(itemSection.getString("material").toUpperCase());
                if (itemMaterial == null) continue;

                int count = itemSection.getInt("count");
                if (count < 1 || count > 64) count = 1;

                boolean enchanted = itemSection.getBoolean("enchanted");

                String displayName = itemSection.getString("display-name");
                if (displayName == null) displayName = key;

                List<String> lore = itemSection.getStringList("lore");
                if (lore.isEmpty()) lore.add(" ");

                int invPosition = itemSection.getInt("inventory-position");
                if (invPosition < 0) invPosition = 0;

                itemModels.add(new GameSelectorItemModel(key, itemMaterial, count, displayName, lore, invPosition, enchanted));
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Invalid data in config (game-selector): " + key) ;
            }
        }

        return itemModels;
    }

}
