package io.lumpq126.enchantAPI.v1_20_R3.api;

import io.lumpq126.enchantAPI.v1_20_R3.enchantment.CustomEnchantment_v1_20_R3;

public class EnchantAPI_v1_20_R3 {
    private static EnchantAPI_v1_20_R3 instance;

    public static EnchantAPI_v1_20_R3 getInstance() {
        if (instance == null) {
            // 이 플러그인이 로드되지 않았을 경우 null 반환
            return null;
        }
        return instance;
    }

    public static void setInstance(EnchantAPI_v1_20_R3 api) {
        instance = api;
    }

    public void registerEnchantment(CustomEnchantment_v1_20_R3 enchantment) {
        // 이 메소드는 EnchantmentManager에서 구현됩니다.
        // 다른 플러그인이 이를 호출하여 인챈트를 등록합니다.
    }
}
