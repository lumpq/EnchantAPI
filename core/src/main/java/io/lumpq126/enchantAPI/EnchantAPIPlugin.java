package io.lumpq126.enchantAPI;

import io.lumpq126.enchantAPI.utilities.Log;
import io.lumpq126.enchantAPI.v1_20_R3.api.EnchantAPI_v1_20_R3;
import io.lumpq126.enchantAPI.v1_20_R3.enchantment.CustomEnchantment_v1_20_R3;
import io.lumpq126.enchantAPI.v1_20_R3.enchantment.manager.EnchantmentManager_v1_20_R3;
import io.lumpq126.enchantAPI.v1_21_R3.api.EnchantAPI_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.CustomEnchantment_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.manager.EnchantmentManager_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R5.api.EnchantAPI_v1_21_R5;
import io.lumpq126.enchantAPI.v1_21_R5.enchantment.CustomEnchantment_v1_21_R5;
import io.lumpq126.enchantAPI.v1_21_R5.enchantment.manager.EnchantmentManager_v1_21_R5;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Log.init(this);
    }

    @Override
    public void onLoad() {
        // 1. 버전별 CustomEnchantment 초기화
        CustomEnchantment_v1_20_R3.init(this);
        CustomEnchantment_v1_21_R3.init(this);
        CustomEnchantment_v1_21_R5.init(this);

        // 2. 버전별 EnchantmentManager 생성 + 싱글톤 등록
        EnchantmentManager_v1_20_R3 enchantmentManager_v1_20_R3 = new EnchantmentManager_v1_20_R3(this);
        EnchantAPI_v1_20_R3.setInstance(enchantmentManager_v1_20_R3);

        EnchantmentManager_v1_21_R3 enchantmentManager_v1_21_R3 = new EnchantmentManager_v1_21_R3(this);
        EnchantAPI_v1_21_R3.setInstance(enchantmentManager_v1_21_R3);

        EnchantmentManager_v1_21_R5 enchantmentManager_v1_21_R5 = new EnchantmentManager_v1_21_R5(this);
        EnchantAPI_v1_21_R5.setInstance(enchantmentManager_v1_21_R5);

        enchantmentManager_v1_20_R3.loadInjectedEnchantments();
        enchantmentManager_v1_21_R3.loadInjectedEnchantments();
        enchantmentManager_v1_21_R5.loadInjectedEnchantments();
    }
}
