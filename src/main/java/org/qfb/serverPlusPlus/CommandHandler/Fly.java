package org.qfb.serverPlusPlus.CommandHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.qfb.serverPlusPlus.ServerPlusPlus;

public class Fly implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;
    public Fly(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("spp.admin") || sender.hasPermission("spp.fly") || sender.isOp()){
        } else{
            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
            return true;
        }
        if (command.getName().equalsIgnoreCase("fly")){
            Player player = (Player) sender;
            if(args.length == 0){
                if (player.getAllowFlight()) {
                    player.sendMessage(prefix + plugin.getMessage("action_for") + player.getName() + plugin.getMessage("fly_disabled"));
                    player.setFlying(false);
                    player.setAllowFlight(false);
                } else {
                    player.sendMessage(prefix + plugin.getMessage("action_for") + player.getName() + plugin.getMessage("fly_enabled"));
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
                return true;
            }
            if(args.length == 1){
                Player target = player;
                target = player.getServer().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(prefix + plugin.getMessage("player_label") + " " + args[0] + plugin.getMessage("player_not_found"));
                    return true;
                }
                if (args[0].toLowerCase().equalsIgnoreCase(target.getName())){
                    if(player.getAllowFlight()){
                        player.sendMessage(prefix + plugin.getMessage("action_for") + target.getName() + plugin.getMessage("fly_enabled"));
                        target.sendMessage(prefix + player.getName() + plugin.getMessage("fly_enabled_self"));
                        player.setFlying(false);
                        player.setAllowFlight(false);
                    } else {
                        player.sendMessage(prefix + plugin.getMessage("action_for") + target.getName() + plugin.getMessage("fly_disabled"));
                        target.sendMessage(prefix + player.getName() + plugin.getMessage("fly_disabled_self"));
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    }
                }
                return true;
            }
        }
        return false;
    }
}
