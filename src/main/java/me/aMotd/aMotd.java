package me.aMotd;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class aMotd extends JavaPlugin{
    private static aMotd instance;
    private MiniMessage miniMessage;
    private FileConfiguration config;
    public static aMotd getInstance() {
        return instance;
    }
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.miniMessage = MiniMessage.miniMessage();
        Objects.requireNonNull(this.getCommand("amotd")).setExecutor(new aCommand());
        this.setMotd();
    }

    public void setMotd() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            this.config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "config.yml"));
            try {
                if (this.config.getBoolean("enabled")) {
                    String line1 = this.config.getString("motd.line1");
                    String line2 = this.config.getString("motd.line2");
                    String motd = line1 + System.lineSeparator() + line2;
                    String legacyMotd = LegacyComponentSerializer.legacySection().serialize(this.miniMessage.deserialize(motd));
                    this.getServer().setMotd(legacyMotd);
                }
            } catch (Exception e) {
                this.getLogger().severe("Failed to set Motd: " + e.getMessage());
            }
        });
    }
}
