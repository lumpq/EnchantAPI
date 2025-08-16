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
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantAPIPlugin extends JavaPlugin {
    private EnchantmentManager_v1_20_R3 enchantmentManager_v1_20_R3;
    private EnchantmentManager_v1_21_R3 enchantmentManager_v1_21_R3;
    private EnchantmentManager_v1_21_R5 enchantmentManager_v1_21_R4;

    @Override
    public void onEnable() {
        Log.init(this);

        // 1. 버전별 CustomEnchantment 초기화
        CustomEnchantment_v1_20_R3.init(this);
        CustomEnchantment_v1_21_R3.init(this);
        CustomEnchantment_v1_21_R5.init(this);

        // 2. 버전별 EnchantmentManager 생성 + 싱글톤 등록
        enchantmentManager_v1_20_R3 = new EnchantmentManager_v1_20_R3(this);
        EnchantAPI_v1_20_R3.setInstance(enchantmentManager_v1_20_R3);

        enchantmentManager_v1_21_R3 = new EnchantmentManager_v1_21_R3(this);
        EnchantAPI_v1_21_R3.setInstance(enchantmentManager_v1_21_R3);

        enchantmentManager_v1_21_R4 = new EnchantmentManager_v1_21_R5(this);
        EnchantAPI_v1_21_R5.setInstance(enchantmentManager_v1_21_R4);

        // 3. 서버가 레지스트리 준비 후 주입하도록 스케줄러 딜레이
        Bukkit.getScheduler().runTaskLater(this, () -> {
            try {
                enchantmentManager_v1_20_R3.loadInjectedEnchantments();
            } catch (Exception e) {
                getLogger().severe("Failed to inject v1_20_R3 enchantments");
                Log.log("error", "", e);
            }

            try {
                enchantmentManager_v1_21_R3.loadInjectedEnchantments();
            } catch (Exception e) {
                getLogger().severe("Failed to inject v1_21_R3 enchantments");
                Log.log("error", "", e);
            }

            try {
                enchantmentManager_v1_21_R4.loadInjectedEnchantments();
            } catch (Exception e) {
                getLogger().severe("Failed to inject v1_21_R5 enchantments");
                Log.log("error", "", e);
            }
        }, 1L);
    }
}
