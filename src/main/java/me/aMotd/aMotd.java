package me.aMotd;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class aMotd extends JavaPlugin{
    private static aMotd instance;
    private MiniMessage miniMessage;

    public static aMotd getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.miniMessage = MiniMessage.miniMessage();
        Objects.requireNonNull(this.getCommand("amotd")).setExecutor(new aCommand());
        this.loadMotd();
    }

    public void loadMotd() {

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                if (getConfig().getBoolean("enabled")) {
                    String rawMotd = getConfig().getString("motd");
                    Component motd = this.miniMessage.deserialize(rawMotd);
                    String legacyMotd = LegacyComponentSerializer.legacySection().serialize(motd);
                    this.getServer().setMotd(legacyMotd);
                }
            } catch (Exception e) {
                this.getLogger().severe("Failed to set Motd: " + e.getMessage());
            }

        });
    }
}
