package me.aMotd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import java.util.Objects;
import java.util.Random;

import static me.aMotd.aMotd.*;

public class aListener implements Listener {

    private int motdIndex;

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        String order = config.getString("motd.order");
        if(Objects.equals(order, "forwards")){forwards();}
        if(Objects.equals(order, "random")){random();}
    }

    public void random(){
        motdIndex = (motdIndex + 1) % (line1List.size());
        String line1 = line1List.get(motdIndex);
        String line2 = line2List.get(new Random().nextInt(line2List.size()));
        getInstance().setMotd(line1,line2);
    }

    public void forwards(){
        motdIndex = (motdIndex + 1) % (Math.max(line1List.size(), line2List.size()));
        String line1 = line1List.size() > motdIndex? line1List.get(motdIndex) : "";
        String line2 = line2List.size() > motdIndex? line2List.get(motdIndex) : "";
        getInstance().setMotd(line1,line2);
    }
}