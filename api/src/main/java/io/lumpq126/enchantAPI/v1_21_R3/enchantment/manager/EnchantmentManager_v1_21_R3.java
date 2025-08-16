package io.lumpq126.enchantAPI.v1_21_R3.enchantment.manager;

import io.lumpq126.enchantAPI.v1_21_R3.api.EnchantAPI_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.CustomEnchantment_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.EnchantmentInjector_v1_21_R3;
import io.lumpq126.enchantAPI.utilities.Log;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EnchantmentManager_v1_21_R3 extends EnchantAPI_v1_21_R3 {

    private final JavaPlugin plugin;
    private final EnchantmentInjector_v1_21_R3 injector;

    // Map 대신 PersistentDataContainer를 사용하도록 변경할 예정
    // 하지만 현재 인챈트 등록을 위해 임시로 사용
    private static final Map<NamespacedKey, CustomEnchantment_v1_21_R3> registeredEnchantments = new ConcurrentHashMap<>();

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
        registeredEnchantments.put(enchantment.getKey(), enchantment);
    }

    public void loadInjectedEnchantments() {
        // injector.inject() 메서드가 NMS 코드를 호출하며, 이 코드를 수정해야 합니다.
        for (CustomEnchantment_v1_21_R3 enchantment : registeredEnchantments.values()) {
            injector.inject(enchantment);
            plugin.getLogger().info("'" + enchantment.getName() + "' 인챈트가 성공적으로 주입되었습니다.");
        }
    }

    // --- PersistentDataContainer를 사용하는 로직으로 변경 ---

    public static void addEnchant(ItemStack item, NamespacedKey key, int level) {
        CustomEnchantment_v1_21_R3 enchantment = registeredEnchantments.get(key);
        if (enchantment != null) {
            // 인챈트 레벨을 item의 PersistentDataContainer에 저장
            item.editMeta(meta -> {
                PersistentDataContainer container = meta.getPersistentDataContainer();
                container.set(key, PersistentDataType.INTEGER, level);
            });
            Log.log("info", "아이템에 '" + enchantment.getName() + "' 인챈트 추가됨 (레벨: " + level + ")", null);
        }
    }

    public static void removeEnchant(ItemStack item, NamespacedKey key) {
        CustomEnchantment_v1_21_R3 enchantment = registeredEnchantments.get(key);
        if (enchantment != null) {
            // PersistentDataContainer에서 인챈트 제거
            item.editMeta(meta -> {
                meta.getPersistentDataContainer().remove(key);
            });
            Log.log("info", "아이템에서 '" + enchantment.getName() + "' 인챈트 제거됨", null);
        }
    }

    public static int getEnchantLevel(ItemStack item, NamespacedKey key) {
        if (item.hasItemMeta()) {
            PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
            Integer level = container.get(key, PersistentDataType.INTEGER);
            return level != null ? level : 0;
        }
        return 0;
    }

    public static Set<NamespacedKey> getEnchants(ItemStack item) {
        if (item.hasItemMeta()) {
            PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
            // 모든 커스텀 인챈트 키를 반환
            return container.getKeys();
        }
        return Set.of();
    }
}