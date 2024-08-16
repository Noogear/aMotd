package me.aMotd;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class aMotd extends JavaPlugin{

    private static aMotd instance;
    private MiniMessage miniMessage;
    public static FileConfiguration config;
    public static List<String> line1List = new ArrayList<>();
    public static List<String> line2List = new ArrayList<>();

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

    public void setMotd(String line1,String line2) {
            try {
                Component motd = this.miniMessage.deserialize(line1 + System.lineSeparator() + line2);
                String legacyMotd = LegacyComponentSerializer.legacySection().serialize(motd);
                this.getServer().setMotd(legacyMotd);
            } catch (Exception e) {
                this.getLogger().severe("Failed to set Motd: " + e.getMessage());
            }
    }

    public void loadMotd() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                try{
                    config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "config.yml"));
                    if (config.getBoolean("enabled")) {
                        line1List = config.getStringList("motd.line1");
                        line2List = config.getStringList("motd.line2");
                    }
                }catch (Exception e){
                    this.getLogger().severe("Failed to load Motd: " + e.getMessage());
                }
                this.setMotd(line1List.getFirst(),line2List.getFirst());
        });
    }

}
