package io.lumpq126.enchantAPI.v1_21_R5.api;

import io.lumpq126.enchantAPI.v1_21_R5.enchantment.CustomEnchantment_v1_21_R5;

public class EnchantAPI_v1_21_R5 {
    private static EnchantAPI_v1_21_R5 instance;

    public static EnchantAPI_v1_21_R5 getInstance() {
        if (instance == null) {
            // 이 플러그인이 로드되지 않았을 경우 null 반환
            return null;
        }
        return instance;
    }

    public static void setInstance(EnchantAPI_v1_21_R5 api) {
        instance = api;
    }

    public void registerEnchantment(CustomEnchantment_v1_21_R5 enchantment) {
        // 이 메소드는 EnchantmentManager에서 구현됩니다.
        // 다른 플러그인이 이를 호출하여 인챈트를 등록합니다.
    }
}
