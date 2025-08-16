package io.lumpq126.enchantAPI.utilities.manager;

import io.lumpq126.enchantAPI.EnchantAPIPlugin;
import io.lumpq126.enchantAPI.api.EnchantAPI;
import io.lumpq126.enchantAPI.enchantment.CustomEnchantment;
import io.lumpq126.enchantAPI.nms.EnchantmentInjector;

import java.util.HashSet;
import java.util.Set;

public class EnchantmentManager extends EnchantAPI {
    private final EnchantAPIPlugin plugin;
    private final Set<CustomEnchantment> customEnchantments = new HashSet<>();
    private final EnchantmentInjector injector;

    public EnchantmentManager(EnchantAPIPlugin plugin) {
        this.plugin = plugin;
        String version = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            // NMS 버전별 EnchantmentInjector 클래스 동적 로딩
            Class<?> injectorClass = Class.forName("io.lumpq126.enchantapi.nms." + version + ".EnchantmentRegister");
            this.injector = (EnchantmentInjector) injectorClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("NMS 모듈을 로드할 수 없습니다: " + version, e);
        }
    }

    @Override
    public void registerEnchantment(CustomEnchantment enchantment) {
        // 실제로 NMS 인젝션을 수행할 목록에 추가
        customEnchantments.add(enchantment);
    }

    public void loadInjectedEnchantments() {
        for (CustomEnchantment enchantment : customEnchantments) {
            injector.inject(enchantment);
            plugin.getLogger().info("'" + enchantment.getName() + "' 인챈트가 성공적으로 주입되었습니다.");
        }
    }
}
