package io.snowyblossom126.enchantapi.api.enchantments.manager;

import io.snowyblossom126.enchantapi.api.CustomEnchantmentInjector;
import io.snowyblossom126.enchantapi.api.EnchantAPI;
import io.snowyblossom126.enchantapi.api.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the registration and injection of custom enchantments.
 * <p>
 * Provides methods for storing enchantments, injecting them into NMS,
 * and applying them to {@link ItemStack}s via {@link PersistentDataContainer}.
 */
public class EnchantmentManager extends EnchantAPI {

    private final JavaPlugin plugin;
    private final CustomEnchantmentInjector injector;
    private static final Map<NamespacedKey, CustomEnchantment> registeredEnchantments = new ConcurrentHashMap<>();

    /**
     * Creates a new enchantment manager.
     * <p>
     * Dynamically loads the appropriate {@link CustomEnchantmentInjector}
     * implementation based on the current server version.
     *
     * @param plugin the owning plugin
     * @throws RuntimeException if the injector cannot be loaded
     */
    public EnchantmentManager(JavaPlugin plugin) {
        this.plugin = plugin;
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            Class<?> injectorClass = Class.forName("io.lumpq126.enchantAPI.nms." + version + ".EnchantmentRegister");
            this.injector = (CustomEnchantmentInjector) injectorClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find NMS EnchantmentRegister class: " + version, e);
        } catch (Exception e) {
            throw new RuntimeException("Could not instantiate NMS EnchantmentRegister: " + version, e);
        }
    }

    /**
     * Registers a custom enchantment internally.
     *
     * @param enchantment the enchantment to register
     */
    @Override
    public void registerEnchantment(CustomEnchantment enchantment) {
        registeredEnchantments.put(enchantment.getKey(), enchantment);
    }

    /**
     * Injects all registered enchantments into the server's registry.
     */
    public void loadInjectedEnchantments() {
        for (CustomEnchantment enchantment : registeredEnchantments.values()) {
            injector.inject(enchantment);
            plugin.getLogger().info("'" + enchantment.getName() + "' has been successfully injected.");
        }
    }

    /**
     * Adds a custom enchantment to an item.
     *
     * @param item the item to modify
     * @param enchantment the enchantment key
     * @param level the enchantment level
     */
    public static void addEnchant(ItemStack item, CustomEnchantment enchantment, int level) {
        item.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(enchantment.getKey(), PersistentDataType.INTEGER, level);
        });
    }

    /**
     * Removes a custom enchantment from an item.
     *
     * @param item the item to modify
     * @param enchantment  the enchantment key
     */
    public static void removeEnchant(ItemStack item, CustomEnchantment enchantment) {
        item.editMeta(meta -> {
            meta.getPersistentDataContainer().remove(enchantment.getKey());
        });
    }

    /**
     * Gets the level of a custom enchantment on an item.
     *
     * @param item the item
     * @param enchantment  the enchantment key
     * @return the enchantment level, or 0 if not present
     */
    public static int getEnchantLevel(ItemStack item, CustomEnchantment enchantment) {
        if (!item.hasItemMeta()) return 0;
        PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
        Integer level = container.get(enchantment.getKey(), PersistentDataType.INTEGER);
        return level != null ? level : 0;
    }

    /**
     * Gets all custom enchantments applied to an item.
     *
     * @param item the item
     * @return the set of enchantment keys
     */
    public static Set<NamespacedKey> getEnchants(ItemStack item) {
        if (!item.hasItemMeta()) return Set.of();
        PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
        return container.getKeys();
    }
}
