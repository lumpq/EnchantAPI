package io.lumpq126.enchantAPI.nms.v1_20_R3;

import io.lumpq126.enchantAPI.enchantment.CustomEnchantment;
import io.lumpq126.enchantAPI.enchantment.EnchantmentInjector;
import io.lumpq126.enchantAPI.utilities.Log;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

public class EnchantmentRegister implements EnchantmentInjector {

    private static class NMSCustomEnchantment extends Enchantment {
        private final CustomEnchantment wrapper;
        private final NamespacedKey key;

        protected NMSCustomEnchantment(CustomEnchantment wrapper, NamespacedKey key) {
            super(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{
                    EquipmentSlot.MAINHAND
            });
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
    @SuppressWarnings("unchecked") // unchecked cast 경고 억제
    public void inject(CustomEnchantment enchantment) {
        try {
            // 1. NMS 인스턴스 생성
            NamespacedKey bukkitKey = enchantment.getKey();
            ResourceLocation nmsId = new ResourceLocation(bukkitKey.getNamespace(), bukkitKey.getKey());

            NMSCustomEnchantment nmsEnchantment = new NMSCustomEnchantment(enchantment, bukkitKey);

            // 2. NMS 레지스트리에 등록
            net.minecraft.core.Registry.register(BuiltInRegistries.ENCHANTMENT, nmsId, nmsEnchantment);

            // 3. NMS 레지스트리 내부 Map에 수동으로 추가 (리플렉션 사용)
            Field field = BuiltInRegistries.ENCHANTMENT.getClass().getDeclaredField("byKey");
            field.setAccessible(true);

            // Unchecked cast 경고를 억제하고 안전하게 캐스팅
            Map<ResourceLocation, Enchantment> byKey = (Map<ResourceLocation, Enchantment>)
                    Objects.requireNonNull(field.get(BuiltInRegistries.ENCHANTMENT));

            byKey.put(nmsId, nmsEnchantment);

            Log.log("info", "Custom enchantment injected: " + bukkitKey.getKey(), null);

        } catch (Exception e) {
            JavaPlugin.getProvidingPlugin(this.getClass()).getLogger().severe(
                    "Failed to inject custom enchantment: " + enchantment.getKey().getKey()
            );
            Log.log("error", "", e);
        }
    }
}
