package io.lumpq126.enchantAPI.api;

import io.lumpq126.enchantAPI.api.enchantment.CustomEnchantment;

/**
 * Handles the low-level injection of a {@link CustomEnchantment}
 * into the Minecraft server.
 * <p>
 * Implementations of this interface are version-specific
 * (NMS-based) and loaded dynamically depending on the server version.
 */
public interface CustomEnchantmentInjector {

    /**
     * Injects a custom enchantment into the server's enchantment registry.
     *
     * @param enchantment the custom enchantment to inject
     */
    void inject(CustomEnchantment enchantment);
}
