package io.lumpq126.enchantAPI.api;

import io.lumpq126.enchantAPI.api.enchantments.CustomEnchantment;
import io.lumpq126.enchantAPI.api.enchantments.manager.EnchantmentManager;

/**
 * The central API access point for the custom enchantment system.
 * <p>
 * Provides methods for registering custom enchantments.
 * An {@link EnchantmentManager} instance should set itself
 * as the active API provider during plugin load.
 */
public class EnchantAPI {
    private static EnchantAPI instance;

    /**
     * Returns the current API instance.
     *
     * @return the API instance, or {@code null} if the plugin
     *         has not been initialized
     */
    public static EnchantAPI getInstance() {
        if (instance == null) {
            // Returns null if the plugin has not been loaded
            return null;
        }
        return instance;
    }

    /**
     * Sets the active API instance.
     * <p>
     * Should only be called by {@link EnchantmentManager}.
     *
     * @param api the API instance
     */
    public static void setInstance(EnchantAPI api) {
        instance = api;
    }

    /**
     * Registers a custom enchantment.
     * <p>
     * The actual behavior is implemented in {@link EnchantmentManager}.
     *
     * @param enchantment the enchantment to register
     */
    public void registerEnchantment(CustomEnchantment enchantment) {
        // Implemented in EnchantmentManager
    }
}
