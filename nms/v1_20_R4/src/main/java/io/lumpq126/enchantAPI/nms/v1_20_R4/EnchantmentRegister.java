package io.lumpq126.enchantAPI.nms.v1_20_R4;

import io.lumpq126.enchantAPI.v1_21_R3.enchantment.CustomEnchantment_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.EnchantmentInjector_v1_21_R3;
import io.lumpq126.enchantAPI.utilities.Log;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class EnchantmentRegister implements EnchantmentInjector_v1_21_R3 {

    private static class NMSCustomEnchantment extends Enchantment {
        private final CustomEnchantment_v1_21_R3 wrapper;
        private final NamespacedKey key;

        protected NMSCustomEnchantment(CustomEnchantment_v1_21_R3 wrapper, NamespacedKey key) {
            super(new EnchantmentDefinition(
                    convertTargetToItemTag(wrapper.getEnchantmentTarget()),
                    java.util.Optional.empty(),
                    wrapper.getWeight(),
                    wrapper.getMaxLevel(),
                    new Cost(wrapper.getMinCostBase(), wrapper.getMinCostPerLevel()),
                    new Cost(wrapper.getMaxCostBase(), wrapper.getMaxCostPerLevel()),
                    wrapper.getAnvilCost(),
                    net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS,
                    convertSlots(wrapper.getApplicableSlots())
            ));
            this.wrapper = wrapper;
            this.key = key;
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
    @SuppressWarnings("unchecked")
    public void inject(CustomEnchantment_v1_21_R3 enchantment) {
        try {
            NamespacedKey bukkitKey = enchantment.getKey();
            ResourceLocation nmsId = new ResourceLocation(bukkitKey.getNamespace(), bukkitKey.getKey());

            NMSCustomEnchantment nmsEnchantment = new NMSCustomEnchantment(enchantment, bukkitKey);

            net.minecraft.core.Registry.register(BuiltInRegistries.ENCHANTMENT, nmsId, nmsEnchantment);

            Field field = BuiltInRegistries.ENCHANTMENT.getClass().getDeclaredField("byKey");
            field.setAccessible(true);

            Map<ResourceLocation, Enchantment> byKey =
                    (Map<ResourceLocation, Enchantment>) Objects.requireNonNull(field.get(BuiltInRegistries.ENCHANTMENT));

            byKey.put(nmsId, nmsEnchantment);

            Log.log("info", "Custom enchantment injected: " + bukkitKey.getKey(), null);

        } catch (Exception e) {
            JavaPlugin.getProvidingPlugin(this.getClass()).getLogger().severe(
                    "Failed to inject custom enchantment: " + enchantment.getKey().getKey()
            );
            Log.log("error", "", e);
        }
    }

    private static EquipmentSlot[] convertSlots(org.bukkit.inventory.EquipmentSlot[] slots) {
        if (slots == null) {
            return new EquipmentSlot[0];
        }
        return Arrays.stream(slots)
                .map(slot -> EquipmentSlot.valueOf(slot.name()))
                .toArray(EquipmentSlot[]::new);
    }

    private static TagKey<Item> convertTargetToItemTag(EnchantmentTarget target) {
        return switch (target) {
            case ARMOR -> ItemTags.ARMOR_ENCHANTABLE;
            case WEAPON -> ItemTags.SWORD_ENCHANTABLE;
            case TOOL -> ItemTags.MINING_ENCHANTABLE;
            case BOW -> ItemTags.BOW_ENCHANTABLE;
            case CROSSBOW -> ItemTags.CROSSBOW_ENCHANTABLE;
            case TRIDENT -> ItemTags.TRIDENT_ENCHANTABLE;
            case FISHING_ROD -> ItemTags.FISHING_ENCHANTABLE;
            default -> ItemTags.VANISHING_ENCHANTABLE;
        };
    }
}
