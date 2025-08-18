package io.lumpq126.enchantAPI.api.enchantments.properties;

/**
 * Represents the rarity of a custom enchantment.
 * <p>
 * This value determines how frequently an enchantment appears
 * in the enchantment table or loot tables.
 */
public enum Rarity {
    /** Common enchantment, appears frequently. */
    COMMON,

    /** Uncommon enchantment, appears less often than common ones. */
    UNCOMMON,

    /** Rare enchantment, appears infrequently. */
    RARE,

    /** Very rare enchantment, appears extremely rarely. */
    VERY_RARE;
}
