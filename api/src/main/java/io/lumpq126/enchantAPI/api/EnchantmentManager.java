package io.lumpq126.enchantAPI.api;

import io.lumpq126.enchantAPI.api.enchantment.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EnchantmentManager extends EnchantAPI {

    private final JavaPlugin plugin;
    private final CustomEnchantmentInjector injector;
    private static final Map<NamespacedKey, CustomEnchantment> registeredEnchantments = new ConcurrentHashMap<>();

    public EnchantmentManager(JavaPlugin plugin) {
        this.plugin = plugin;
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            Class<?> injectorClass = Class.forName("io.lumpq126.enchantAPI.nms." + version + ".EnchantmentRegister");
            this.injector = (CustomEnchantmentInjector) injectorClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("NMS EnchantmentRegister 클래스를 찾을 수 없습니다: " + version, e);
        } catch (Exception e) {
            throw new RuntimeException("NMS EnchantmentRegister 인스턴스를 생성할 수 없습니다: " + version, e);
        }
    }

    @Override
    public void registerEnchantment(CustomEnchantment enchantment) {
        registeredEnchantments.put(enchantment.getKey(), enchantment);
    }

    public void loadInjectedEnchantments() {
        for (CustomEnchantment enchantment : registeredEnchantments.values()) {
            injector.inject(enchantment);
            plugin.getLogger().info("'" + enchantment.getName() + "' 인챈트가 성공적으로 주입되었습니다.");
        }
    }

    public static void addEnchant(ItemStack item, NamespacedKey key, int level) {
        item.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(key, PersistentDataType.INTEGER, level);
        });
    }

    public static void removeEnchant(ItemStack item, NamespacedKey key) {
        item.editMeta(meta -> {
            meta.getPersistentDataContainer().remove(key);
        });
    }

    public static int getEnchantLevel(ItemStack item, NamespacedKey key) {
        if (!item.hasItemMeta()) return 0;
        PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
        Integer level = container.get(key, PersistentDataType.INTEGER);
        return level != null ? level : 0;
    }

    public static Set<NamespacedKey> getEnchants(ItemStack item) {
        if (!item.hasItemMeta()) return Set.of();
        PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
        return container.getKeys();
    }

    public static Enchantment asVanillaEnchantment(CustomEnchantment enchantment) {
        return Enchantment.getByKey(enchantment.getKey());
    }
}
