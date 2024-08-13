package me.aMotd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class aCommand implements CommandExecutor {

    public aCommand() {}

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage("use [/amotd reload] to reload this plugin.");
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("amotd.reload")) {
                    try {
                        aMotd.getInstance().reloadConfig();
                        aMotd.getInstance().loadMotd();
                        sender.sendMessage("Reloaded Successfully:)");
                    } catch (Exception e) {
                        sender.sendMessage(e.getMessage());
                    }
                } else {
                    sender.sendMessage("You have no permission.");
                }
            }else {
                sender.sendMessage("Usage: /amotd reload");
            }
        }
        return false;
    }
}
