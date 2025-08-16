package io.lumpq126.enchantAPI.nms.v1_21_R1;

import io.lumpq126.enchantAPI.utilities.Log;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.CustomEnchantment_v1_21_R3;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.EnchantmentInjector_v1_21_R3;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.RegistryLayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentRegister implements EnchantmentInjector_v1_21_R3 {

    @Override
    public void inject(CustomEnchantment_v1_21_R3 enchantment) {
        try {
            NamespacedKey bukkitKey = enchantment.getKey();

            // NMS ResourceLocation (팩토리 메서드)
            ResourceLocation nmsId = ResourceLocation.fromNamespaceAndPath(
                    bukkitKey.getNamespace(),
                    bukkitKey.getKey()
            );

            // 아이템 태그 -> HolderSet<Item>
            TagKey<Item> supportedTag = convertTargetToItemTag(enchantment.getEnchantmentTarget());
            HolderSet<Item> supportedItems = BuiltInRegistries.ITEM.getOrCreateTag(supportedTag);

            // 비용 정의
            Enchantment.Cost minCost = new Enchantment.Cost(
                    enchantment.getMinCostBase(),
                    enchantment.getMinCostPerLevel()
            );
            Enchantment.Cost maxCost = new Enchantment.Cost(
                    enchantment.getMaxCostBase(),
                    enchantment.getMaxCostPerLevel()
            );

            // 슬롯 그룹
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

            // ✅ 루트가 아니라 정적 접근자를 통해 ENCHANTMENT 레지스트리 획득
            Registry<Enchantment> enchantRegistry =
                    RegistryLayer.STATIC_ACCESS.registryOrThrow(Registries.ENCHANTMENT);

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
     * Bukkit 슬롯들을 NMS EquipmentSlotGroup으로 매핑합니다.
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

    /**
     * Bukkit EnchantmentTarget → NMS Item Tag 변환
     */
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
