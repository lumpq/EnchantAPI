package io.lumpq126.enchantAPI;

import io.lumpq126.enchantAPI.nms.NMSHandler;
import io.lumpq126.enchantAPI.nms.NMSHandlerFactory;
import io.lumpq126.enchantAPI.utilites.Mm;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class EnchantAPIPlugin extends JavaPlugin {
    private static NMSHandler nms;

    @Override
    public void onEnable() {

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

    public static NMSHandler getNms() {
        return nms;
    }
}
