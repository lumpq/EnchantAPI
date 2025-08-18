package io.lumpq126.enchantAPI.nms.v1_21_R3;

import io.lumpq126.enchantAPI.api.CustomEnchantmentInjector;
import io.lumpq126.enchantAPI.api.enchantments.CustomEnchantment;
import io.lumpq126.enchantAPI.utilities.Log;
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
// ★ 버전에 맞게

import java.util.ArrayList;
import java.util.List;

public class EnchantmentRegister implements CustomEnchantmentInjector {

    @Override
    public void inject(CustomEnchantment enchantment) {
        try {
            NamespacedKey bukkitKey = enchantment.getKey();

            // NMS id
            ResourceLocation nmsId = ResourceLocation.fromNamespaceAndPath(
                    bukkitKey.getNamespace(),
                    bukkitKey.getKey()
            );

            // 지원 아이템 태그 -> HolderSet
            TagKey<Item> supportedTag = convertTargetToItemTag(enchantment.getEnchantmentTarget());
            HolderSet<Item> supportedItems = BuiltInRegistries.ITEM.getOrThrow(supportedTag);

            // 비용
            Enchantment.Cost minCost = new Enchantment.Cost(
                    enchantment.getMinCostBase(),
                    enchantment.getMinCostPerLevel()
            );
            Enchantment.Cost maxCost = new Enchantment.Cost(
                    enchantment.getMaxCostBase(),
                    enchantment.getMaxCostPerLevel()
            );

            // 슬롯
            EquipmentSlotGroup[] slotGroups = convertSlots(enchantment.getApplicableSlots());

            // 정의 + 인스턴스
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

            // ★★★ 핵심: 서버의 reloadable RegistryAccess에서 ENCHANTMENT 레지스트리 꺼내기
            Registry<Enchantment> enchantRegistry = RegistryLayer.STATIC_ACCESS.lookupOrThrow(Registries.ENCHANTMENT);

            // 등록
            Registry.register(enchantRegistry, nmsId, nmsEnchant);

            Log.log("info", "Custom enchantment injected: " + bukkitKey.getKey(), null);

        } catch (IllegalStateException frozen) {
            // 레지스트리가 이미 freeze 된 경우 여기로 옴
            JavaPlugin.getProvidingPlugin(this.getClass()).getLogger().severe(
                    "[EnchantAPI] Registry frozen while injecting '" + enchantment.getKey().getKey() + "'. " +
                            "인젝션 타이밍을 onLoad() 또는 (Paper면) RegistryFreeze/리소스 리로드 이전으로 옮기세요."
            );
        } catch (Exception e) {
            JavaPlugin.getProvidingPlugin(this.getClass()).getLogger().severe(
                    "Failed to inject custom enchantment: " + enchantment.getKey().getKey()
            );
            Log.log("error", "", e);
        }
    }

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
                default -> {}
            }
        }

        List<EquipmentSlotGroup> groups = new ArrayList<>();
        if (anyHand) groups.add(EquipmentSlotGroup.HAND);
        if (anyArmor) groups.add(EquipmentSlotGroup.ARMOR);
        if (groups.isEmpty()) groups.add(EquipmentSlotGroup.ANY);

        return groups.toArray(new EquipmentSlotGroup[0]);
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
