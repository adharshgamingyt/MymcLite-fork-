package me.supposedly_sam.mymcLite.Models;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GameSelectorItemModel {
    private final String itemKey;
    private final Material item;
    private final int itemCount;
    private final String displayName;
    private final List<String> lore;
    private final int invPosition;
    private final boolean enchanted;

    public GameSelectorItemModel(String itemKey, Material item, int itemCount, String displayName, List<String> lore, int invPosition, boolean enchanted) {
        this.itemKey = itemKey;
        this.item = item;
        this.itemCount = itemCount;
        this.displayName = displayName;
        this.lore = lore;
        this.invPosition = invPosition;
        this.enchanted = enchanted;
    }

    public String getItemKey() {
        return itemKey;
    }

    public Material getItem() {
        return item;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getInvPosition() {
        return invPosition;
    }

    public boolean isEnchanted() {
        return enchanted;
    }
}

