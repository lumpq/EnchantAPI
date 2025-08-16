package io.lumpq126.enchantAPI.v1_21_R3.enchantment.manager;

import io.lumpq126.enchantAPI.v1_21_R3.api.EnchantAPI_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.CustomEnchantment_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.EnchantmentInjector_v1_21_R3;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnchantmentManager_v1_21_R3 extends EnchantAPI_v1_21_R3 {

    private final JavaPlugin plugin;
    private final EnchantmentInjector_v1_21_R3 injector;
    private static final Map<NamespacedKey, CustomEnchantment_v1_21_R3> customEnchantments = new ConcurrentHashMap<>();

    public EnchantmentManager_v1_21_R3(JavaPlugin plugin) {
        this.plugin = plugin;

        try {
            Class<?> injectorClass = Class.forName("io.lumpq126.enchantAPI.nms.v1_21_R3.EnchantmentRegister");
            this.injector = (EnchantmentInjector_v1_21_R3) injectorClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("NMS EnchantmentRegister 클래스를 찾을 수 없습니다: v1_21_R3", e);
        } catch (Exception e) {
            throw new RuntimeException("NMS EnchantmentRegister 인스턴스를 생성할 수 없습니다: v1_21_R3", e);
        }
    }

    @Override
    public void registerEnchantment(CustomEnchantment_v1_21_R3 enchantment) {
        customEnchantments.put(enchantment.getKey(), enchantment);
    }

    public void loadInjectedEnchantments() {
        for (CustomEnchantment_v1_21_R3 enchantment : customEnchantments.values()) {
            injector.inject(enchantment);
            plugin.getLogger().info("'" + enchantment.getName() + "' 인챈트가 성공적으로 주입되었습니다.");
        }
    }

    public static void addEnchant(ItemStack item, NamespacedKey key, int level) {
        CustomEnchantment_v1_21_R3 enchantment = customEnchantments.get(key);
        if (enchantment != null) {
            enchantment.addEnchant(item, level);
        }
    }

    public static void removeEnchant(ItemStack item, NamespacedKey key) {
        CustomEnchantment_v1_21_R3 enchantment = customEnchantments.get(key);
        if (enchantment != null) {
            enchantment.removeEnchant(item);
        }
    }
}
