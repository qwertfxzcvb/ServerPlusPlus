package org.qfb.serverPlusPlus.CommandHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.qfb.serverPlusPlus.ServerPlusPlus;

public class Invsee implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;

    public Invsee(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("invsee")) {
            if (sender.hasPermission("spp.admin") || sender.hasPermission("spp.invsee.invsee") || sender.isOp()) {
                if (args.length == 1) {
                    Player target = player;
                    target = player.getServer().getPlayer(args[0]);
                    if (target == null) {
                        player.sendMessage(prefix + plugin.getMessage("player_label") + " " + args[0] + plugin.getMessage("player_not_found"));
                        return true;
                    }
                    if (args[0].toLowerCase().equalsIgnoreCase(target.getName())){
                        player.openInventory(target.getInventory());
                        String invsee_invsee = plugin.getMessage("invsee_invsee").replace("{player}", target.getName());
                        player.sendMessage(prefix + invsee_invsee);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }
                }
                if (args.length == 0) {
                    player.sendMessage(prefix + plugin.getMessage("invsee_invsee_usage"));
                }
            } else {
                sender.sendMessage(prefix + plugin.getMessage("nopermission"));
                return true;
            }
        }

        if (command.getName().equalsIgnoreCase("endersee")) {
            if (sender.hasPermission("spp.admin") || sender.hasPermission("spp.invsee.endersee") || sender.isOp()) {
                if (args.length == 1) {
                    Player target = player;
                    target = player.getServer().getPlayer(args[0]);
                    if (target == null) {
                        player.sendMessage(prefix + plugin.getMessage("player_label") + " " + args[0] + plugin.getMessage("player_not_found"));
                        return true;
                    }
                    if (args[0].toLowerCase().equalsIgnoreCase(target.getName())){
                        player.openInventory(target.getEnderChest());
                        String invsee_endersee = plugin.getMessage("invsee_endersee").replace("{player}", target.getName());
                        player.sendMessage(prefix + invsee_endersee);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }
                }
                if (args.length == 0) {
                    player.sendMessage(prefix + plugin.getMessage("invsee_endersee_usage"));
                }
            } else {
                sender.sendMessage(prefix + plugin.getMessage("nopermission"));
                return true;
            }
        }
        return true;
    }
}