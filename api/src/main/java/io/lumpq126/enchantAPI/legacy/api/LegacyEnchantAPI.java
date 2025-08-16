package io.lumpq126.enchantAPI.legacy.api;

import io.lumpq126.enchantAPI.legacy.enchantment.LegacyCustomEnchantment;

public class LegacyEnchantAPI {
    private static LegacyEnchantAPI instance;

    public static LegacyEnchantAPI getInstance() {
        if (instance == null) {
            // 이 플러그인이 로드되지 않았을 경우 null 반환
            return null;
        }
        return instance;
    }

    public static void setInstance(LegacyEnchantAPI api) {
        instance = api;
    }

    public void registerEnchantment(LegacyCustomEnchantment enchantment) {
        // 이 메소드는 EnchantmentManager에서 구현됩니다.
        // 다른 플러그인이 이를 호출하여 인챈트를 등록합니다.
    }
}
