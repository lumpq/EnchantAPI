package io.lumpq126.enchantAPI;

import io.lumpq126.enchantAPI.legacy.api.LegacyEnchantAPI;
import io.lumpq126.enchantAPI.legacy.enchantment.LegacyCustomEnchantment;
import io.lumpq126.enchantAPI.utilities.Log;
import io.lumpq126.enchantAPI.legacy.enchantment.manager.LegacyEnchantmentManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantAPIPlugin extends JavaPlugin {
    private LegacyEnchantmentManager enchantmentManager;

    @Override
    public void onEnable() {

        enchantmentManager = new LegacyEnchantmentManager(this);
        LegacyEnchantAPI.setInstance(enchantmentManager);

        Bukkit.getScheduler().runTaskLater(this, () -> enchantmentManager.loadInjectedEnchantments(), 1L);

        Log.init(this);
        LegacyCustomEnchantment.init(this);
    }
}
