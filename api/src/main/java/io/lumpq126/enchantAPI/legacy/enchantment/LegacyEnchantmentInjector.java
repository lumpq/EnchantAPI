package io.lumpq126.enchantAPI.legacy.enchantment;

public interface LegacyEnchantmentInjector {
    /**
     * NMS 레지스트리에 커스텀 인챈트를 주입(Inject)합니다.
     * @param enchantment 주입할 커스텀 인챈트 객체
     */
    void inject(LegacyCustomEnchantment enchantment);
}
