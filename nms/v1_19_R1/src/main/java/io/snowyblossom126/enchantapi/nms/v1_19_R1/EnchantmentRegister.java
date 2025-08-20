package io.snowyblossom126.enchantapi.nms.v1_19_R1;

import io.snowyblossom126.enchantapi.api.CustomEnchantmentInjector;
import io.snowyblossom126.enchantapi.api.enchantments.CustomEnchantment;
import io.snowyblossom126.enchantapi.utilities.Log;
import io.snowyblossom126.enchantapi.api.enchantments.properties.Rarity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class EnchantmentRegister implements CustomEnchantmentInjector {

    private static class NMSCustomEnchantment extends Enchantment {
        private final CustomEnchantment wrapper;
        private final NamespacedKey key;

        protected NMSCustomEnchantment(CustomEnchantment wrapper, NamespacedKey key) {
            super(convertRarity(wrapper.getRarity()),
                    convertTarget(wrapper.getEnchantmentTarget()),
                    convertSlots(wrapper.getApplicableSlots()));
            this.wrapper = wrapper;
            this.key = key;
        }

        @Override
        public int getMinLevel() {
            return 1;
        }

        @Override
        public int getMaxLevel() {
            return this.wrapper.getMaxLevel();
        }

        @Override
        public int getMinCost(int level) {
            return this.wrapper.getAnvilCost();
        }

        @Override
        public int getMaxCost(int level) {
            return this.getMinCost(level) + 5;
        }

        @Override
        public boolean isTreasureOnly() {
            return wrapper.isTreasure();
        }

        @Override
        public boolean isCurse() {
            return wrapper.isCursed();
        }

        @Override
        public boolean isTradeable() {
            return wrapper.canTrade();
        }

        @Override
        public boolean isDiscoverable() {
            return wrapper.isDiscoverable();
        }

        // NMS ItemStack을 인자로 받도록 수정
        @Override
        public boolean canEnchant(@NotNull net.minecraft.world.item.ItemStack stack) {
            return this.wrapper.canEnchant(CraftItemStack.asBukkitCopy(stack));
        }

        @Override
        public @NotNull String getDescriptionId() {
            return "enchantment." + this.key.getNamespace() + "." + this.key.getKey();
        }
    }

    @Override
    public void inject(CustomEnchantment enchantment) {
        try {
            NamespacedKey bukkitKey = enchantment.getKey();
            ResourceLocation nmsId = new ResourceLocation(bukkitKey.getNamespace(), bukkitKey.getKey());

            NMSCustomEnchantment nmsEnchantment = new NMSCustomEnchantment(enchantment, bukkitKey);
            Registry.register(Registry.ENCHANTMENT, nmsId, nmsEnchantment);

            try {
                EnchantmentWrapper wrapper = new EnchantmentWrapper(bukkitKey.getKey());
                EnchantmentWrapper.registerEnchantment(wrapper);
            } catch (IllegalArgumentException ignored) {}

            Log.log("info", "Successfully injected custom enchantment: " + bukkitKey, null);
        } catch (Exception e) {
            JavaPlugin.getProvidingPlugin(this.getClass()).getLogger().severe("Failed to inject custom enchantment: " + enchantment.getKey().getKey());
            Log.log("error", "Failed to inject custom enchantment.", e);
        }
    }

    /**
     * API의 Rarity Enum을 NMS의 Enchantment.Rarity Enum으로 변환합니다.
     */
    private static Enchantment.Rarity convertRarity(Rarity rarity) {
        // 이름이 동일하므로 valueOf를 통해 간단히 변환할 수 있습니다.
        return Enchantment.Rarity.valueOf(rarity.name());
    }

    /**
     * Bukkit의 EnchantmentTarget Enum을 NMS의 EnchantmentCategory Enum으로 변환합니다.
     */
    private static EnchantmentCategory convertTarget(EnchantmentTarget target) {
        // Bukkit과 NMS의 Enum 이름이 일부 다르므로 switch문으로 직접 매핑합니다.
        return switch (target) {
            case ALL, BREAKABLE -> EnchantmentCategory.BREAKABLE;
            case ARMOR -> EnchantmentCategory.ARMOR;
            case ARMOR_FEET -> EnchantmentCategory.ARMOR_FEET;
            case ARMOR_LEGS -> EnchantmentCategory.ARMOR_LEGS;
            case ARMOR_HEAD -> EnchantmentCategory.ARMOR_HEAD;
            case ARMOR_TORSO -> EnchantmentCategory.ARMOR_CHEST;
            case WEAPON -> EnchantmentCategory.WEAPON;
            case TOOL -> EnchantmentCategory.DIGGER;
            case BOW -> EnchantmentCategory.BOW;
            case FISHING_ROD -> EnchantmentCategory.FISHING_ROD;
            case TRIDENT -> EnchantmentCategory.TRIDENT;
            case WEARABLE -> EnchantmentCategory.WEARABLE;
            case CROSSBOW -> EnchantmentCategory.CROSSBOW;
            case VANISHABLE -> EnchantmentCategory.VANISHABLE;
        };
    }

    /**
     * Bukkit의 EquipmentSlot 배열을 NMS의 EquipmentSlot 배열로 변환합니다.
     */
    private static EquipmentSlot[] convertSlots(org.bukkit.inventory.EquipmentSlot[] slots) {
        if (slots == null) {
            return new EquipmentSlot[0];
        }
        return Arrays.stream(slots)
                .map(slot -> EquipmentSlot.valueOf(slot.name()))
                .toArray(EquipmentSlot[]::new);
    }
}
