package org.qfb.serverPlusPlus.CommandHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.qfb.serverPlusPlus.ServerPlusPlus;

public class Suicide implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;

    public Suicide(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("suicide")) {
            if (sender.hasPermission("spp.admin") || sender.hasPermission("spp.suicide") || sender.isOp()) {
                player.setHealth(0);
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(prefix + player.getName() + " " + plugin.getMessage("player_suicide"));
                }

            } else {
                sender.sendMessage(prefix + plugin.getMessage("nopermission"));
                return true;
            }
        }
        return true;
    }
}