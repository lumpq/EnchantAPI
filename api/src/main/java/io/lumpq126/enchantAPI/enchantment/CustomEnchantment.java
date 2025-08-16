package io.lumpq126.enchantAPI.enchantment;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CustomEnchantment {
    private static JavaPlugin plugin;
    protected final NamespacedKey key;
    protected final String name;
    protected final int maxLevel;
    protected final int anvilCost;
    protected final boolean isTreasure;
    protected final boolean isCursed;
    protected final boolean canTrade;
    protected final boolean isDiscoverable; // 추가됨

    public static void init(JavaPlugin instance) {
        plugin = instance;
    }

    public CustomEnchantment(String id, String name,
                             int maxLevel, int anvilCost,
                             boolean isTreasure, boolean isCursed, boolean canTrade, boolean isDiscoverable) {
        this.key = new NamespacedKey(plugin, id);
        this.name = name;
        this.maxLevel = maxLevel;
        this.anvilCost = anvilCost;
        this.isTreasure = isTreasure;
        this.isCursed = isCursed;
        this.canTrade = canTrade;
        this.isDiscoverable = isDiscoverable; // 추가됨
    }

    public abstract void onEnchant(ItemStack item, int level);
    public abstract void onUnenchant(ItemStack item);
    public abstract boolean canEnchant(ItemStack item);

    public NamespacedKey getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getAnvilCost() {
        return anvilCost;
    }

    public boolean isTreasure() {
        return isTreasure;
    }

    public boolean isCursed() {
        return isCursed;
    }

    public boolean canTrade() {
        return canTrade;
    }

    public boolean isDiscoverable() {
        return isDiscoverable;
    }
}
