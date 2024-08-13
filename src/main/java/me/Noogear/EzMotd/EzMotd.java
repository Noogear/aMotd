package me.Noogear.EzMotd;

import me.Noogear.EzMotd.Commands.ReloadCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class EzMotd extends JavaPlugin implements Listener {

    private static EzMotd instance;
    private MiniMessage miniMessage;
    private int motdIndex = 0;
    private int iconIndex = 0;
    private boolean randomMotdEnabled;
    private boolean randomIconEnabled;
    public List<String> motdCache;
    public List<BufferedImage> iconCache;

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.miniMessage = MiniMessage.miniMessage();
        Bukkit.getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(this.getCommand("reload")).setExecutor(new ReloadCommand());
        this.loadConfig();

    }
    public static EzMotd getInstance() {
        return instance;
    }
    public void loadConfig() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            File iconsFolder = new File(this.getDataFolder(), "icons");
            if (!iconsFolder.exists()) {
                iconsFolder.mkdirs();
            }
            File configFile = new File(this.getDataFolder(), "config.yml");

            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            randomMotdEnabled = this.getConfig().getBoolean("random_motd_enabled");
            randomIconEnabled = this.getConfig().getBoolean("random_icon_enabled");
            List<String> motdList = config.getStringList("motds");
            List<String> iconPaths = config.getStringList("icons");

            motdCache.addAll(motdList);
            for (String iconPath : iconPaths) {
                try {
                    BufferedImage image = ImageIO.read(new File(this.getDataFolder(), iconPath));
                    iconCache.add(image);
                } catch (IOException e) {
                    this.getLogger().severe("Error reading icon: " + e.getMessage());
                }
            }
        });
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        if (randomMotdEnabled) {
            try {
                String rawMotd = this.getNextMotd();
                if (rawMotd == null || rawMotd.isEmpty()) {
                    throw new IllegalStateException("MOTD is null or empty");
                }
                Component motd = this.miniMessage.deserialize(rawMotd);
                String legacyMotd = LegacyComponentSerializer.legacySection().serialize(motd);
                event.setMotd(legacyMotd);
            } catch (Exception e) {
                this.getLogger().severe("Failed to set Motd: " + e.getMessage());
            }
        }
        if (randomIconEnabled) {
            try {
                BufferedImage image = this.getNextIcon();
                if (image!= null) {
                    CachedServerIcon icon = Bukkit.loadServerIcon(image);
                    event.setServerIcon(icon);
                }
            } catch (Exception e) {
                this.getLogger().severe("Failed to set Icon: " + e.getMessage());
            }
        }
    }

    private String getNextMotd() {
        if (motdCache.isEmpty()) {
            return null;
        }
        String motd = motdCache.get(motdIndex);
        motdIndex = (motdIndex + 1) % motdCache.size();
        return motd;
    }

    private BufferedImage getNextIcon() {
        if (iconCache.isEmpty()) {
            return null;
        }
        BufferedImage image = iconCache.get(iconIndex);
        iconIndex = (iconIndex + 1) % iconCache.size();
        return image;
    }
}