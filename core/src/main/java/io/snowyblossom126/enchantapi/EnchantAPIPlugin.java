package io.snowyblossom126.enchantapi;

import io.snowyblossom126.enchantapi.api.EnchantAPI;
import io.snowyblossom126.enchantapi.api.enchantments.manager.EnchantmentManager;
import io.snowyblossom126.enchantapi.api.enchantments.CustomEnchantment;
import io.snowyblossom126.enchantapi.utilities.Log;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Log.init(this);
    }

    @Override
    public void onLoad() {
        // 1. CustomEnchantment 초기화
        CustomEnchantment.init(this);

        // 2. EnchantmentManager 생성 + 싱글톤 등록
        EnchantmentManager manager = new EnchantmentManager(this);
        EnchantAPI.setInstance(manager);

        manager.loadInjectedEnchantments();
    }
}
