package io.lumpq126.enchantAPI.nms.v1_18_R1;

import io.lumpq126.enchantAPI.v1_20_R3.enchantment.CustomEnchantment_v1_20_R3;
import io.lumpq126.enchantAPI.v1_20_R3.enchantment.EnchantmentInjector_v1_20_R3;
import io.lumpq126.enchantAPI.v1_20_R3.enchantment.properties.Rarity_v1_20_R3;
import io.lumpq126.enchantAPI.utilities.Log;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class EnchantmentRegister implements EnchantmentInjector_v1_20_R3 {

    private static class NMSCustomEnchantment extends Enchantment {
        private final CustomEnchantment_v1_20_R3 wrapper;
        private final NamespacedKey key;

        protected NMSCustomEnchantment(CustomEnchantment_v1_20_R3 wrapper, NamespacedKey key) {
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

        @Override
        public boolean canEnchant(@NotNull ItemStack stack) {
            return this.wrapper.canEnchant(CraftItemStack.asBukkitCopy(stack));
        }

        @Override
        public @NotNull String getDescriptionId() {
            return "enchantment." + this.key.getNamespace() + "." + this.key.getKey();
        }
    }

    @Override
    public void inject(CustomEnchantment_v1_20_R3 enchantment) {
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
    private static Enchantment.Rarity convertRarity(Rarity_v1_20_R3 rarity) {
        // 이름이 동일하므로 valueOf를 통해 간단히 변환할 수 있습니다.
        return Enchantment.Rarity.valueOf(rarity.name());
    }

    /**
     * Bukkit의 EnchantmentTarget Enum을 NMS의 EnchantmentCategory Enum으로 변환합니다.
     */
    private static EnchantmentCategory convertTarget(EnchantmentTarget target) {
        // Bukkit과 NMS의 Enum 이름이 일부 다르므로 switch문으로 직접 매핑합니다.
        return switch (target) {
            case ALL -> EnchantmentCategory.BREAKABLE;
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
            case BREAKABLE -> EnchantmentCategory.BREAKABLE;
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