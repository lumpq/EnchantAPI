package io.lumpq126.enchantAPI.nms.v1_20_R1;

import io.lumpq126.enchantAPI.enchantment.CustomEnchantment;
import io.lumpq126.enchantAPI.enchantment.EnchantmentInjector;
import io.lumpq126.enchantAPI.utilities.Log;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EnchantmentRegister implements EnchantmentInjector {

    // NMS Enchantment를 상속받는 커스텀 클래스
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
            // 1. BuiltInRegistries에서 Enchantment 레지스트리 가져오기
            Registry<Enchantment> enchantmentRegistry = BuiltInRegistries.ENCHANTMENT;

            // 2. NMS 인스턴스 생성

            // Bukkit NamespacedKey → NMS ResourceLocation
            NamespacedKey bukkitKey = enchantment.getKey();
            ResourceLocation nmsId = new ResourceLocation(bukkitKey.getNamespace(), bukkitKey.getKey());

            NMSCustomEnchantment nmsEnchantment = new NMSCustomEnchantment(enchantment, bukkitKey);

            // 3. 레지스트리에 등록 (WritableRegistry로 캐스팅 필요)
            Registry.register(enchantmentRegistry, nmsId, nmsEnchantment);

            // 4. Bukkit Enchantment 등록
            try {
                if (org.bukkit.enchantments.Enchantment.getByKey(bukkitKey) == null) {
                    EnchantmentWrapper wrapper = new EnchantmentWrapper(bukkitKey.getKey());
                    org.bukkit.enchantments.Enchantment.registerEnchantment(wrapper);
                }
            } catch (IllegalArgumentException ignored) {
                // 이미 등록된 경우 무시
            }

        } catch (Exception e) {
            JavaPlugin.getProvidingPlugin(this.getClass()).getLogger().severe(
                    "Failed to inject custom enchantment: " + enchantment.getKey().getKey()
            );
            Log.log("error", "", e);
        }
    }
}
