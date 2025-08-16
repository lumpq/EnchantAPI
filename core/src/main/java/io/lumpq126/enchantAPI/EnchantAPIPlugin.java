package io.lumpq126.enchantAPI;

import io.lumpq126.enchantAPI.api.EnchantAPI;
import io.lumpq126.enchantAPI.enchantment.CustomEnchantment;
import io.lumpq126.enchantAPI.nms.EnchantmentInjector;
import io.lumpq126.enchantAPI.nms.NMSHandlerFactory;
import io.lumpq126.enchantAPI.utilities.Log;
import io.lumpq126.enchantAPI.utilities.Mm;
import io.lumpq126.enchantAPI.utilities.manager.EnchantmentManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class EnchantAPIPlugin extends JavaPlugin {
    private static EnchantmentInjector nms;
    private EnchantmentManager enchantmentManager;

    @Override
    public void onEnable() {

        enchantmentManager = new EnchantmentManager(this);
        EnchantAPI.setInstance(enchantmentManager);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            enchantmentManager.loadInjectedEnchantments();
        }, 1L);

        Log.init(this);
        CustomEnchantment.init(this);

        try {
            nms = NMSHandlerFactory.loadNMS();
            getComponentLogger().info(Mm.mm(
                    "<green>NMS 핸들러 활성화 성공! 서버 버전: " + getServer().getBukkitVersion() + ", NMS 버전: " + NMSHandlerFactory.getNMSVersion() + "</green>"));
        } catch (UnsupportedOperationException e) {
            getLogger().severe("NMS 핸들러 활성화 실패: " + e.getMessage());
            getLogger().log(Level.SEVERE, "Exception stacktrace: ", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static EnchantmentInjector getNms() {
        return nms;
    }
}
