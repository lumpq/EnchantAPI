package io.lumpq126.enchantAPI.api.enchantments;

import io.lumpq126.enchantAPI.api.enchantments.properties.Rarity;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a custom enchantment.
 * <p>
 * This class defines the base abstraction for creating custom enchantments.
 * It is version-independent and injected by a version-specific
 * {@code EnchantmentRegister} implementation.
 *
 * @apiNote
 * Targeting rules differ by Minecraft version:
 * <ul>
 *   <li><b>1.17–1.20.3</b>:
 *   {@link #enchantmentTarget} is required. {@link #targetTagKey} is ignored.</li>
 *   <li><b>1.20.4–1.21.3</b>:
 *   {@link #enchantmentTarget} is required. Internally mapped to item tags.</li>
 *   <li><b>1.21.4+</b>:
 *   {@link #targetTagKey} is required. {@link #enchantmentTarget} is ignored.</li>
 * </ul>
 */
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

    /**
     * Initializes this API with the owning plugin instance.
     * <p>
     * Must be called during plugin enable before any enchantments are created.
     *
     * @param instance the plugin instance
     */
    public static void init(JavaPlugin instance) {
        plugin = instance;
    }

    /**
     * Constructs a new custom enchantment.
     *
     * @param builder the builder containing enchantment properties
     */
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

    /**
     * Builder for creating {@link CustomEnchantment} instances.
     * <p>
     * Version-specific usage rules are described in
     * {@link CustomEnchantment class-level documentation}.
     */
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

        /**
         * Creates a new builder.
         *
         * @param id       the internal ID of the enchantment
         * @param name     the display name of the enchantment
         * @param maxLevel the maximum level of the enchantment
         */
        public Builder(String id, String name, int maxLevel) {
            this.id = id;
            this.name = name;
            this.maxLevel = maxLevel;
        }

        /**
         * Sets the additional cost when using this enchantment on an anvil.
         *
         * @param anvilCost the anvil cost
         * @return this builder
         */
        public Builder anvilCost(int anvilCost) {
            this.anvilCost = anvilCost;
            return this;
        }

        /**
         * Sets the weight (rarity) of this enchantment in the enchantment table.
         *
         * @param weight the enchantment weight
         * @return this builder
         */
        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        /**
         * Marks this enchantment as a treasure enchantment.
         *
         * @param isTreasure true if treasure-only
         * @return this builder
         */
        public Builder treasure(boolean isTreasure) {
            this.isTreasure = isTreasure;
            return this;
        }

        /**
         * Marks this enchantment as cursed.
         *
         * @param isCursed true if cursed
         * @return this builder
         */
        public Builder cursed(boolean isCursed) {
            this.isCursed = isCursed;
            return this;
        }

        /**
         * Sets whether this enchantment can appear in villager trades.
         *
         * @param canTrade true if tradable
         * @return this builder
         */
        public Builder canTrade(boolean canTrade) {
            this.canTrade = canTrade;
            return this;
        }

        /**
         * Sets whether this enchantment can appear in loot tables or world generation.
         *
         * @param isDiscoverable true if discoverable
         * @return this builder
         */
        public Builder discoverable(boolean isDiscoverable) {
            this.isDiscoverable = isDiscoverable;
            return this;
        }

        /**
         * Sets the rarity of this enchantment.
         *
         * @param rarity the rarity
         * @return this builder
         */
        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        /**
         * Sets the {@link EnchantmentTarget}.
         * <p>
         * Used in versions 1.17–1.21.3. Ignored in 1.21.4+.
         *
         * @param enchantmentTarget the target
         * @return this builder
         */
        public Builder enchantmentTarget(EnchantmentTarget enchantmentTarget) {
            this.enchantmentTarget = enchantmentTarget;
            return this;
        }

        /**
         * Sets the {@link NamespacedKey} of the item tag this enchantment can be applied to.
         * <p>
         * Required in 1.21.4+.
         *
         * @param targetTagKey the item tag key
         * @return this builder
         */
        public Builder targetTagKey(NamespacedKey targetTagKey) {
            this.targetTagKey = targetTagKey;
            return this;
        }

        /**
         * Sets the equipment slots this enchantment can be applied to.
         *
         * @param applicableSlots the applicable slots
         * @return this builder
         */
        public Builder applicableSlots(EquipmentSlot... applicableSlots) {
            this.applicableSlots = applicableSlots;
            return this;
        }

        /**
         * Sets the minimum cost formula for this enchantment.
         *
         * @param base     the base cost
         * @param perLevel the per-level increment
         * @return this builder
         */
        public Builder minCost(int base, int perLevel) {
            this.minCostBase = base;
            this.minCostPerLevel = perLevel;
            return this;
        }

        /**
         * Sets the maximum cost formula for this enchantment.
         *
         * @param base     the base cost
         * @param perLevel the per-level increment
         * @return this builder
         */
        public Builder maxCost(int base, int perLevel) {
            this.maxCostBase = base;
            this.maxCostPerLevel = perLevel;
            return this;
        }

        /**
         * Builds a new {@link CustomEnchantment}.
         *
         * @return the custom enchantment
         */
        public abstract CustomEnchantment build();
    }

    // ───────────── Abstract API ─────────────

    /**
     * Applies this enchantment to the given item.
     *
     * @param item  the item
     * @param level the level to apply
     */
    public abstract void addEnchant(ItemStack item, int level);

    /**
     * Removes this enchantment from the given item.
     *
     * @param item the item
     */
    public abstract void removeEnchant(ItemStack item);

    /**
     * Checks whether this enchantment can be applied to the given item.
     *
     * @param item the item
     * @return true if applicable
     */
    public abstract boolean canEnchant(ItemStack item);

    // ───────────── Getters ─────────────

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
