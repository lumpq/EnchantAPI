package io.lumpq126.enchantAPI.legacy.enchantment;

import io.lumpq126.enchantAPI.legacy.enchantment.properties.Rarity;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 레거시 커스텀 인챈트 정의 클래스 (v1_20_R3 용).
 * 단순한 속성만 제공.
 */
public abstract class LegacyCustomEnchantment {
    private static JavaPlugin plugin;
    protected final NamespacedKey key;
    protected final String name;
    protected final int maxLevel;
    protected final int anvilCost;
    protected final boolean isTreasure;
    protected final boolean isCursed;
    protected final boolean canTrade;
    protected final boolean isDiscoverable;
    protected final Rarity rarity;
    protected final EnchantmentTarget enchantmentTarget;
    protected final EquipmentSlot[] applicableSlots;
    protected final int weight;

    public static void init(JavaPlugin instance) {
        plugin = instance;
    }

    public LegacyCustomEnchantment(String id, String name, int maxLevel, int anvilCost,
                                   Rarity rarity, EnchantmentTarget enchantmentTarget, EquipmentSlot[] applicableSlots,
                                   boolean isTreasure, boolean isCursed, boolean canTrade, boolean isDiscoverable, int weight) {
        this.key = new NamespacedKey(plugin, id);
        this.name = name;
        this.maxLevel = maxLevel;
        this.anvilCost = anvilCost;
        this.rarity = rarity;
        this.enchantmentTarget = enchantmentTarget;
        this.applicableSlots = applicableSlots;
        this.isTreasure = isTreasure;
        this.isCursed = isCursed;
        this.canTrade = canTrade;
        this.isDiscoverable = isDiscoverable;
        this.weight = weight;
    }

    public abstract void onEnchant(ItemStack item, int level);
    public abstract void onUnenchant(ItemStack item);
    public abstract boolean canEnchant(ItemStack item);

    public NamespacedKey getKey() { return key; }
    public String getName() { return name; }
    public int getMaxLevel() { return maxLevel; }
    public int getAnvilCost() { return anvilCost; }
    public Rarity getRarity() { return rarity; }
    public EnchantmentTarget getEnchantmentTarget() { return enchantmentTarget; }
    public EquipmentSlot[] getApplicableSlots() { return applicableSlots; }
    public boolean isTreasure() { return isTreasure; }
    public boolean isCursed() { return isCursed; }
    public boolean canTrade() { return canTrade; }
    public boolean isDiscoverable() { return isDiscoverable; }
    public int getWeight() { return weight; }
}
