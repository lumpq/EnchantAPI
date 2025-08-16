package io.lumpq126.enchantAPI.legacy.enchantment.manager;

import io.lumpq126.enchantAPI.legacy.api.LegacyEnchantAPI;
import io.lumpq126.enchantAPI.legacy.enchantment.LegacyCustomEnchantment;
import io.lumpq126.enchantAPI.legacy.enchantment.LegacyEnchantmentInjector;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class LegacyEnchantmentManager extends LegacyEnchantAPI {
    private final JavaPlugin plugin;
    private final Map<NamespacedKey, LegacyCustomEnchantment> customEnchantments = new ConcurrentHashMap<>();
    private final LegacyEnchantmentInjector injector;

    public LegacyEnchantmentManager(JavaPlugin plugin) {
        this.plugin = plugin;
        String version = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            Class<?> injectorClass = Class.forName("io.lumpq126.enchantAPI.nms." + version + ".EnchantmentRegister");
            this.injector = (LegacyEnchantmentInjector) injectorClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("NMS 모듈을 로드할 수 없습니다: " + version, e);
        }
    }

    @Override
    public void registerEnchantment(LegacyCustomEnchantment enchantment) {
        customEnchantments.put(enchantment.getKey(), enchantment);
    }

    public void loadInjectedEnchantments() {
        for (LegacyCustomEnchantment enchantment : customEnchantments.values()) {
            injector.inject(enchantment);
            plugin.getLogger().info("'" + enchantment.getName() + "' 인챈트가 성공적으로 주입되었습니다.");
        }
    }

    // 추가된 메서드
    public void onEnchant(ItemStack item, NamespacedKey key, int level) {
        LegacyCustomEnchantment enchantment = customEnchantments.get(key);
        if (enchantment != null) {
            enchantment.onEnchant(item, level);
        }
    }

    public void onUnenchant(ItemStack item, NamespacedKey key) {
        LegacyCustomEnchantment enchantment = customEnchantments.get(key);
        if (enchantment != null) {
            enchantment.onUnenchant(item);
        }
    }
}
