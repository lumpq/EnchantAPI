package io.lumpq126.enchantAPI;

import io.lumpq126.enchantAPI.v1_20_R3.api.EnchantAPI_v1_20_R3;
import io.lumpq126.enchantAPI.v1_20_R3.enchantment.CustomEnchantment_v1_20_R3;
import io.lumpq126.enchantAPI.v1_20_R3.enchantment.manager.EnchantmentManager_v1_20_R3;
import io.lumpq126.enchantAPI.utilities.Log;
import io.lumpq126.enchantAPI.v1_21_R3.api.EnchantAPI_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.CustomEnchantment_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.manager.EnchantmentManager_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R5.api.EnchantAPI_v1_21_R5;
import io.lumpq126.enchantAPI.v1_21_R5.enchantment.CustomEnchantment_v1_21_R5;
import io.lumpq126.enchantAPI.v1_21_R5.enchantment.manager.EnchantmentManager_v1_21_R5;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantAPIPlugin extends JavaPlugin {
    private EnchantmentManager_v1_20_R3 enchantmentManager_v1_20_R3;
    private EnchantmentManager_v1_21_R3 enchantmentManager_v1_21_R3;
    private EnchantmentManager_v1_21_R5 enchantmentManager_v1_21_R4;

    @Override
    public void onEnable() {

        enchantmentManager_v1_20_R3 = new EnchantmentManager_v1_20_R3(this);
        EnchantAPI_v1_20_R3.setInstance(enchantmentManager_v1_20_R3);
        Bukkit.getScheduler().runTaskLater(this, () -> enchantmentManager_v1_20_R3.loadInjectedEnchantments(), 1L);

        enchantmentManager_v1_21_R3 = new EnchantmentManager_v1_21_R3(this);
        EnchantAPI_v1_21_R3.setInstance(enchantmentManager_v1_21_R3);
        Bukkit.getScheduler().runTaskLater(this, () -> enchantmentManager_v1_21_R3.loadInjectedEnchantments(), 1L);

        enchantmentManager_v1_21_R4 = new EnchantmentManager_v1_21_R5(this);
        EnchantAPI_v1_21_R5.setInstance(enchantmentManager_v1_21_R4);
        Bukkit.getScheduler().runTaskLater(this, () -> enchantmentManager_v1_21_R4.loadInjectedEnchantments(), 1L);

        Log.init(this);
        CustomEnchantment_v1_20_R3.init(this);
        CustomEnchantment_v1_21_R3.init(this);
        CustomEnchantment_v1_21_R5.init(this);
    }
}
