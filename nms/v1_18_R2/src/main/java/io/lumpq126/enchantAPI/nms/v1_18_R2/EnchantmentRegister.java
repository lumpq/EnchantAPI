package io.lumpq126.enchantAPI.nms.v1_18_R2;

import io.lumpq126.enchantAPI.enchantment.CustomEnchantment;
import io.lumpq126.enchantAPI.enchantment.EnchantmentInjector;
import io.lumpq126.enchantAPI.utilities.Log;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EnchantmentRegister implements EnchantmentInjector {

    private static class NMSCustomEnchantment extends Enchantment {
        private final CustomEnchantment wrapper;
        private final NamespacedKey key;

        protected NMSCustomEnchantment(CustomEnchantment wrapper, NamespacedKey key) {
            super(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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
}
