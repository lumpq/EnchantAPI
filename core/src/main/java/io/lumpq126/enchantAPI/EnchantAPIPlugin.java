package io.lumpq126.enchantAPI;

import io.lumpq126.enchantAPI.api.EnchantAPI;
import io.lumpq126.enchantAPI.enchantment.CustomEnchantment;
import io.lumpq126.enchantAPI.utilities.Log;
import io.lumpq126.enchantAPI.enchantment.manager.EnchantmentManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantAPIPlugin extends JavaPlugin {
    private EnchantmentManager enchantmentManager;

    @Override
    public void onEnable() {

        enchantmentManager = new EnchantmentManager(this);
        EnchantAPI.setInstance(enchantmentManager);

        Bukkit.getScheduler().runTaskLater(this, () -> enchantmentManager.loadInjectedEnchantments(), 1L);

        Log.init(this);
        CustomEnchantment.init(this);
    }
}
