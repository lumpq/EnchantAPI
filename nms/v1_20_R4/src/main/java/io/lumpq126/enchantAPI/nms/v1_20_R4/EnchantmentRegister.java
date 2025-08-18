package io.lumpq126.enchantAPI.nms.v1_20_R4;

import io.lumpq126.enchantAPI.api.CustomEnchantmentInjector;
import io.lumpq126.enchantAPI.api.enchantments.CustomEnchantment;
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

import java.util.Arrays;
import java.util.Optional;

public class EnchantmentRegister implements CustomEnchantmentInjector {

    private static class NMSCustomEnchantment extends Enchantment {
        private final CustomEnchantment wrapper;
        private final NamespacedKey key;

        protected NMSCustomEnchantment(CustomEnchantment wrapper, NamespacedKey key) {
            super(new Enchantment.EnchantmentDefinition(
                    convertTargetToItemTag(wrapper.getEnchantmentTarget()),
                    Optional.empty(),
                    wrapper.getWeight(),
                    wrapper.getMaxLevel(),
                    new Enchantment.Cost(wrapper.getMinCostBase(), wrapper.getMinCostPerLevel()),
                    new Enchantment.Cost(wrapper.getMaxCostBase(), wrapper.getMaxCostPerLevel()),
                    wrapper.getAnvilCost(),
                    net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS,
                    convertSlots(wrapper.getApplicableSlots())
            ));
            this.wrapper = wrapper;
            this.key = key;
        }

        @Override
        public boolean isTreasureOnly() {
            return this.wrapper.isTreasure();
        }

        @Override
        public boolean isCurse() {
            return this.wrapper.isCursed();
        }

        @Override
        public boolean isTradeable() {
            return this.wrapper.canTrade();
        }

        @Override
        public boolean isDiscoverable() {
            return this.wrapper.isDiscoverable();
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
    public void inject(CustomEnchantment enchantment) {
        try {
            NamespacedKey bukkitKey = enchantment.getKey();
            ResourceLocation nmsId = new ResourceLocation(bukkitKey.getNamespace(), bukkitKey.getKey());

            NMSCustomEnchantment nmsEnchantment = new NMSCustomEnchantment(enchantment, bukkitKey);

            // BuiltInRegistries.ENCHANTMENT에 직접 등록하는 방식으로 변경
            // 이는 Reflection을 사용하는 기존 코드보다 안전하고 NMS 버전 변화에 강함
            net.minecraft.core.Registry.register(BuiltInRegistries.ENCHANTMENT, nmsId, nmsEnchantment);

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