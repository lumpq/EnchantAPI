package io.lumpq126.enchantAPI.api.enchantment;

import io.lumpq126.enchantAPI.api.enchantment.properties.Rarity;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public abstract class CustomEnchantment {
    private static JavaPlugin plugin;

    protected final NamespacedKey key;
    protected final String name;

    protected final int maxLevel;
    protected final int anvilCost;
    protected final int weight;

    protected final boolean isTreasure;
    protected final boolean isCursed;
    protected final boolean canTrade;
    protected final boolean isDiscoverable;

    protected final Rarity rarity;
    protected final EnchantmentTarget enchantmentTarget;
    protected final NamespacedKey targetTagKey;

    protected final EquipmentSlot[] applicableSlots;

    protected final int minCostBase;
    protected final int minCostPerLevel;
    protected final int maxCostBase;
    protected final int maxCostPerLevel;

    public static void init(JavaPlugin instance) {
        plugin = instance;
    }

    protected CustomEnchantment(Builder builder) {
        this.key = new NamespacedKey(plugin, builder.id);
        this.name = builder.name;
        this.maxLevel = builder.maxLevel;
        this.anvilCost = builder.anvilCost;
        this.weight = builder.weight;
        this.isTreasure = builder.isTreasure;
        this.isCursed = builder.isCursed;
        this.canTrade = builder.canTrade;
        this.isDiscoverable = builder.isDiscoverable;
        this.rarity = builder.rarity;
        this.enchantmentTarget = builder.enchantmentTarget;
        this.targetTagKey = builder.targetTagKey;
        this.applicableSlots = builder.applicableSlots;
        this.minCostBase = builder.minCostBase;
        this.minCostPerLevel = builder.minCostPerLevel;
        this.maxCostBase = builder.maxCostBase;
        this.maxCostPerLevel = builder.maxCostPerLevel;
    }

    public static abstract class Builder {
        private final String id;
        private final String name;
        private final int maxLevel;

        private int anvilCost = 0;
        private int weight = 10;
        private boolean isTreasure = false;
        private boolean isCursed = false;
        private boolean canTrade = true;
        private boolean isDiscoverable = true;

        private Rarity rarity = null;
        private EnchantmentTarget enchantmentTarget = null;
        private NamespacedKey targetTagKey = null;

        private EquipmentSlot[] applicableSlots = new EquipmentSlot[0];

        private int minCostBase = 0;
        private int minCostPerLevel = 0;
        private int maxCostBase = 0;
        private int maxCostPerLevel = 0;

        public Builder(String id, String name, int maxLevel) {
            this.id = id;
            this.name = name;
            this.maxLevel = maxLevel;
        }

        public Builder anvilCost(int anvilCost) {
            this.anvilCost = anvilCost;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder treasure(boolean isTreasure) {
            this.isTreasure = isTreasure;
            return this;
        }

        public Builder cursed(boolean isCursed) {
            this.isCursed = isCursed;
            return this;
        }

        public Builder canTrade(boolean canTrade) {
            this.canTrade = canTrade;
            return this;
        }

        public Builder discoverable(boolean isDiscoverable) {
            this.isDiscoverable = isDiscoverable;
            return this;
        }

        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder enchantmentTarget(EnchantmentTarget enchantmentTarget) {
            this.enchantmentTarget = enchantmentTarget;
            return this;
        }

        public Builder targetTagKey(NamespacedKey targetTagKey) {
            this.targetTagKey = targetTagKey;
            return this;
        }

        public Builder applicableSlots(EquipmentSlot... applicableSlots) {
            this.applicableSlots = applicableSlots;
            return this;
        }

        public Builder minCost(int base, int perLevel) {
            this.minCostBase = base;
            this.minCostPerLevel = perLevel;
            return this;
        }

        public Builder maxCost(int base, int perLevel) {
            this.maxCostBase = base;
            this.maxCostPerLevel = perLevel;
            return this;
        }

        public abstract CustomEnchantment build();
    }

    // 추상 메서드
    public abstract void addEnchant(ItemStack item, int level);
    public abstract void removeEnchant(ItemStack item);
    public abstract boolean canEnchant(ItemStack item);

    // 게터 (Getters)
    public String getName() { return name; }
    public NamespacedKey getKey() { return key; }
    public Rarity getRarity() { return rarity; }
    public EnchantmentTarget getEnchantmentTarget() { return enchantmentTarget; }
    public NamespacedKey getTargetTagKey() { return targetTagKey; }
    public EquipmentSlot[] getApplicableSlots() { return applicableSlots; }
    public int getMaxLevel() { return maxLevel; }
    public int getAnvilCost() { return anvilCost; }
    public int getWeight() { return weight; }
    public boolean isTreasure() { return isTreasure; }
    public boolean isCursed() { return isCursed; }
    public boolean canTrade() { return canTrade; }
    public boolean isDiscoverable() { return isDiscoverable; }
    public int getMinCostBase() { return minCostBase; }
    public int getMinCostPerLevel() { return minCostPerLevel; }
    public int getMaxCostBase() { return maxCostBase; }
    public int getMaxCostPerLevel() { return maxCostPerLevel; }
}
