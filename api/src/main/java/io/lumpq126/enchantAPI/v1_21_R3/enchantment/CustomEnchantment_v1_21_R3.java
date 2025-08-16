package io.lumpq126.enchantAPI.v1_21_R3.enchantment;

import io.lumpq126.enchantAPI.v1_20_R3.enchantment.properties.Rarity_v1_20_R3;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 커스텀 인챈트 정의 클래스.
 * NMS EnchantmentDefinition의 속성들을 전부 커스터마이징 가능하게 제공합니다.
 */
public abstract class CustomEnchantment_v1_21_R3 {
    private static JavaPlugin plugin;

    protected final NamespacedKey key;
    protected final String name;

    // 기본 속성들
    protected final int maxLevel;
    protected final int anvilCost;
    protected final int weight;

    // 기타 속성
    protected final boolean isTreasure;
    protected final boolean isCursed;
    protected final boolean canTrade;
    protected final boolean isDiscoverable;

    // Bukkit / API 전용 속성
    protected final Rarity_v1_20_R3 rarity;
    protected final EnchantmentTarget enchantmentTarget;
    protected final EquipmentSlot[] applicableSlots;

    // 비용 커스터마이징
    protected final int minCostBase;
    protected final int minCostPerLevel;
    protected final int maxCostBase;
    protected final int maxCostPerLevel;

    public static void init(JavaPlugin instance) {
        plugin = instance;
    }

    public CustomEnchantment_v1_21_R3(String id,
                                      String name,
                                      int maxLevel,
                                      int anvilCost,
                                      int weight,
                                      Rarity_v1_20_R3 rarity,
                                      EnchantmentTarget enchantmentTarget,
                                      EquipmentSlot[] applicableSlots,
                                      boolean isTreasure,
                                      boolean isCursed,
                                      boolean canTrade,
                                      boolean isDiscoverable,
                                      int minCostBase,
                                      int minCostPerLevel,
                                      int maxCostBase,
                                      int maxCostPerLevel) {
        this.key = new NamespacedKey(plugin, id);
        this.name = name;

        this.maxLevel = maxLevel;
        this.anvilCost = anvilCost;
        this.weight = weight;

        this.rarity = rarity;
        this.enchantmentTarget = enchantmentTarget;
        this.applicableSlots = applicableSlots;

        this.isTreasure = isTreasure;
        this.isCursed = isCursed;
        this.canTrade = canTrade;
        this.isDiscoverable = isDiscoverable;

        this.minCostBase = minCostBase;
        this.minCostPerLevel = minCostPerLevel;
        this.maxCostBase = maxCostBase;
        this.maxCostPerLevel = maxCostPerLevel;
    }

    // 추상 메서드 (각 엔챈트마다 구현)
    public abstract void onEnchant(ItemStack item, int level);
    public abstract void onUnenchant(ItemStack item);
    public abstract boolean canEnchant(ItemStack item);

    // 게터들
    public NamespacedKey getKey() { return key; }
    public String getName() { return name; }

    public int getMaxLevel() { return maxLevel; }
    public int getAnvilCost() { return anvilCost; }
    public int getWeight() { return weight; }

    public Rarity_v1_20_R3 getRarity() { return rarity; }
    public EnchantmentTarget getEnchantmentTarget() { return enchantmentTarget; }
    public EquipmentSlot[] getApplicableSlots() { return applicableSlots; }

    public boolean isTreasure() { return isTreasure; }
    public boolean isCursed() { return isCursed; }
    public boolean canTrade() { return canTrade; }
    public boolean isDiscoverable() { return isDiscoverable; }

    public int getMinCostBase() { return minCostBase; }
    public int getMinCostPerLevel() { return minCostPerLevel; }
    public int getMaxCostBase() { return maxCostBase; }
    public int getMaxCostPerLevel() { return maxCostPerLevel; }
}
