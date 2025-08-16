package io.lumpq126.enchantAPI.listeners;

import io.lumpq126.enchantAPI.EnchantAPIPlugin;
import io.lumpq126.enchantAPI.utilities.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class EnchantmentLoadListener implements Listener {

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        try {
            EnchantAPIPlugin.getInstance().getEnchantmentManager_v1_20_R3().loadInjectedEnchantments();
        } catch (Exception e) {
            EnchantAPIPlugin.getInstance().getLogger().severe("Failed to inject v1_20_R3 enchantments");
            Log.log("error", "", e);
        }

        try {
            EnchantAPIPlugin.getInstance().getEnchantmentManager_v1_21_R3().loadInjectedEnchantments();
        } catch (Exception e) {
            EnchantAPIPlugin.getInstance().getLogger().severe("Failed to inject v1_21_R3 enchantments");
            Log.log("error", "", e);
        }

        try {
            EnchantAPIPlugin.getInstance().getEnchantmentManager_v1_21_R5().loadInjectedEnchantments();
        } catch (Exception e) {
            EnchantAPIPlugin.getInstance().getLogger().severe("Failed to inject v1_21_R5 enchantments");
            Log.log("error", "", e);
        }

        // 이 이벤트가 한 번만 실행되도록 리스너를 비활성화할 수 있습니다.
        // HandlerList.unregisterAll(this);
    }
}
