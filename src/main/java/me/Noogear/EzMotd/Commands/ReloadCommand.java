package me.Noogear.EzMotd.Commands;

import me.Noogear.EzMotd.EzMotd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    public ReloadCommand() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("ezmotd.reload")) {
            try{
                EzMotd.getInstance().reloadConfig();
                EzMotd.getInstance().loadConfig();
                sender.sendMessage("Reloaded Successfully:)");
            }catch (Exception e){
                sender.sendMessage(e.getMessage());
            }
        }else {
            sender.sendMessage("You have no permission.");
        }

        return true;
    }
}
