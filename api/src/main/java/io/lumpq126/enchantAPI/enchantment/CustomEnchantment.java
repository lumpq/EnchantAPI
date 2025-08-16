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
    protected final boolean canEnchant;
    protected final boolean canTrade;
    //protected final EnchantmentRarity rarity;

    /**
     * 로그 유틸리티 초기화
     *
     * @param instance 플러그인 인스턴스
     */
    public static void init(JavaPlugin instance) {
        plugin = instance;
    }

    public CustomEnchantment(String id, String name,
                             int maxLevel, int anvilCost,
                             boolean isTreasure, boolean isCursed, boolean canEnchant, boolean canTrade) {
        this.key = new NamespacedKey(plugin, id);
        this.name = name;
        this.maxLevel = maxLevel;
        this.anvilCost = anvilCost;
        this.isTreasure = isTreasure;
        this.isCursed = isCursed;
        this.canEnchant = canEnchant;
        this.canTrade = canTrade;
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
}
