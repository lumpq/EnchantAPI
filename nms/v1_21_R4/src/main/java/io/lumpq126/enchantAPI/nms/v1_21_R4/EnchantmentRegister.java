package io.lumpq126.enchantAPI.nms.v1_21_R4;

import io.lumpq126.enchantAPI.v1_21_R5.enchantment.CustomEnchantment_v1_21_R5;
import io.lumpq126.enchantAPI.utilities.Log;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.RegistryLayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentRegister {

    public void inject(CustomEnchantment_v1_21_R5 enchantment) {
        try {
            NamespacedKey bukkitKey = enchantment.getKey();

            // NMS ResourceLocation
            ResourceLocation nmsId = ResourceLocation.fromNamespaceAndPath(
                    bukkitKey.getNamespace(),
                    bukkitKey.getKey()
            );

            // v1_21_R4: enchantmentTarget 삭제됨 → CustomEnchantment에 직접 지정된 TagKey<Item> 사용
            TagKey<Item> supportedTag = enchantment.getTargetTag();
            HolderSet<Item> supportedItems = BuiltInRegistries.ITEM.getOrThrow(supportedTag);

            // 비용 정의
            Enchantment.Cost minCost = new Enchantment.Cost(
                    enchantment.getMinCostBase(),
                    enchantment.getMinCostPerLevel()
            );
            Enchantment.Cost maxCost = new Enchantment.Cost(
                    enchantment.getMaxCostBase(),
                    enchantment.getMaxCostPerLevel()
            );

            // 슬롯 그룹 변환
            EquipmentSlotGroup[] slotGroups = convertSlots(enchantment.getApplicableSlots());

            // 정의 + 인스턴스 생성
            Enchantment.EnchantmentDefinition definition = Enchantment.definition(
                    supportedItems,
                    enchantment.getWeight(),
                    enchantment.getMaxLevel(),
                    minCost,
                    maxCost,
                    enchantment.getAnvilCost(),
                    slotGroups
            );
            Enchantment nmsEnchant = Enchantment.enchantment(definition).build(nmsId);

            // v1_21_R4: 정적 레지스트리 접근
            Registry<Enchantment> enchantRegistry =
                    RegistryLayer.STATIC_ACCESS.lookupOrThrow(Registries.ENCHANTMENT);

            // 등록
            Registry.register(enchantRegistry, nmsId, nmsEnchant);

            Log.log("info", "Custom enchantment injected: " + bukkitKey.getKey(), null);

        } catch (Exception e) {
            JavaPlugin.getProvidingPlugin(this.getClass()).getLogger().severe(
                    "Failed to inject custom enchantment: " + enchantment.getKey().getKey()
            );
            Log.log("error", "", e);
        }
    }

    /**
     * Bukkit 슬롯들을 NMS EquipmentSlotGroup으로 매핑
     */
    private static EquipmentSlotGroup[] convertSlots(org.bukkit.inventory.EquipmentSlot[] slots) {
        if (slots == null || slots.length == 0) {
            return new EquipmentSlotGroup[]{ EquipmentSlotGroup.ANY };
        }

        boolean anyHand = false;
        boolean anyArmor = false;

        for (org.bukkit.inventory.EquipmentSlot s : slots) {
            switch (s) {
                case HAND, OFF_HAND -> anyHand = true;
                case HEAD, CHEST, LEGS, FEET -> anyArmor = true;
                default -> { /* ignore */ }
            }
        }

        List<EquipmentSlotGroup> groups = new ArrayList<>();
        if (anyHand) groups.add(EquipmentSlotGroup.HAND);
        if (anyArmor) groups.add(EquipmentSlotGroup.ARMOR);
        if (groups.isEmpty()) groups.add(EquipmentSlotGroup.ANY);

        return groups.toArray(new EquipmentSlotGroup[0]);
    }
}
