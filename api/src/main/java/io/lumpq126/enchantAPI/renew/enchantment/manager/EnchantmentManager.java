package io.lumpq126.enchantAPI.renew.enchantment.manager;

import io.lumpq126.enchantAPI.renew.api.EnchantAPI;
import io.lumpq126.enchantAPI.renew.enchantment.CustomEnchantment;
import io.lumpq126.enchantAPI.renew.enchantment.EnchantmentInjector;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnchantmentManager extends EnchantAPI {
    private final JavaPlugin plugin;
    private final Map<NamespacedKey, CustomEnchantment> customEnchantments = new ConcurrentHashMap<>();
    private final EnchantmentInjector injector;

    public EnchantmentManager(JavaPlugin plugin) {
        this.plugin = plugin;
        String version = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            Class<?> injectorClass = Class.forName("io.lumpq126.enchantAPI.nms." + version + ".EnchantmentRegister");
            this.injector = (EnchantmentInjector) injectorClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("NMS 모듈을 로드할 수 없습니다: " + version, e);
        }
    }

    @Override
    public void registerEnchantment(CustomEnchantment enchantment) {
        customEnchantments.put(enchantment.getKey(), enchantment);
    }

    public void loadInjectedEnchantments() {
        for (CustomEnchantment enchantment : customEnchantments.values()) {
            injector.inject(enchantment);
            plugin.getLogger().info("'" + enchantment.getName() + "' 인챈트가 성공적으로 주입되었습니다.");
        }
    }

    // 추가된 메서드
    public void onEnchant(ItemStack item, NamespacedKey key, int level) {
        CustomEnchantment enchantment = customEnchantments.get(key);
        if (enchantment != null) {
            enchantment.onEnchant(item, level);
        }
    }

    public void onUnenchant(ItemStack item, NamespacedKey key) {
        CustomEnchantment enchantment = customEnchantments.get(key);
        if (enchantment != null) {
            enchantment.onUnenchant(item);
        }
    }
}
