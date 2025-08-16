package io.lumpq126.enchantAPI;

import io.lumpq126.enchantAPI.listeners.EnchantmentLoadListener;
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
    private EnchantmentManager_v1_20_R3 enchantmentManager_v1_20_R3;
    private EnchantmentManager_v1_21_R3 enchantmentManager_v1_21_R3;
    private EnchantmentManager_v1_21_R5 enchantmentManager_v1_21_R4;
    private static EnchantAPIPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

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

        // 3. 서버 로드 이벤트를 기다리는 리스너 등록
        // 이 시점에는 아직 레지스트리가 완전히 로드되지 않았으므로, 딜레이 스케줄러를 제거합니다.
        getServer().getPluginManager().registerEvents(new EnchantmentLoadListener(), this);
    }

    // 이 메서드를 추가하여 다른 클래스에서 EnchantmentManager에 접근할 수 있도록 합니다.
    public EnchantmentManager_v1_20_R3 getEnchantmentManager_v1_20_R3() { return enchantmentManager_v1_20_R3; }
    public EnchantmentManager_v1_21_R3 getEnchantmentManager_v1_21_R3() { return enchantmentManager_v1_21_R3; }
    public EnchantmentManager_v1_21_R5 getEnchantmentManager_v1_21_R4() { return enchantmentManager_v1_21_R4; }

    public static EnchantAPIPlugin getInstance() {
        return instance;
    }
}
