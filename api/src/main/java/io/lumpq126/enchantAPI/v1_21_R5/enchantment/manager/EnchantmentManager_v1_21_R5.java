package io.lumpq126.enchantAPI.v1_21_R5.enchantment.manager;

import io.lumpq126.enchantAPI.v1_21_R5.api.EnchantAPI_v1_21_R5;
import io.lumpq126.enchantAPI.v1_21_R5.enchantment.CustomEnchantment_v1_21_R5;
import io.lumpq126.enchantAPI.v1_21_R5.enchantment.EnchantmentInjector_v1_21_R5;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnchantmentManager_v1_21_R5 extends EnchantAPI_v1_21_R5 {
    private final JavaPlugin plugin;
    private final Map<NamespacedKey, CustomEnchantment_v1_21_R5> customEnchantments = new ConcurrentHashMap<>();
    private final EnchantmentInjector_v1_21_R5 injector;

    public EnchantmentManager_v1_21_R5(JavaPlugin plugin) {
        this.plugin = plugin;
        String version = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            Class<?> injectorClass = Class.forName("io.lumpq126.enchantAPI.nms." + version + ".EnchantmentRegister");
            this.injector = (EnchantmentInjector_v1_21_R5) injectorClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("NMS 모듈을 로드할 수 없습니다: " + version, e);
        }
    }

    @Override
    public void registerEnchantment(CustomEnchantment_v1_21_R5 enchantment) {
        customEnchantments.put(enchantment.getKey(), enchantment);
    }

    public void loadInjectedEnchantments() {
        for (CustomEnchantment_v1_21_R5 enchantment : customEnchantments.values()) {
            injector.inject(enchantment);
            plugin.getLogger().info("'" + enchantment.getName() + "' 인챈트가 성공적으로 주입되었습니다.");
        }
    }

    // 추가된 메서드
    public void addEnchant(ItemStack item, NamespacedKey key, int level) {
        CustomEnchantment_v1_21_R5 enchantment = customEnchantments.get(key);
        if (enchantment != null) {
            enchantment.addEnchant(item, level);
        }
    }

    public void removeEnchant(ItemStack item, NamespacedKey key) {
        CustomEnchantment_v1_21_R5 enchantment = customEnchantments.get(key);
        if (enchantment != null) {
            enchantment.removeEnchant(item);
        }
    }
}
