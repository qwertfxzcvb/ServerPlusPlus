package org.qfb.serverPlusPlus.CommandHandler;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.qfb.serverPlusPlus.ServerPlusPlus;

public class Gamemode implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;

    public Gamemode(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("gm") || command.getName().equalsIgnoreCase("gamemode")) {
            if(sender.hasPermission("spp.admin") || sender.hasPermission("spp.gamemode") || sender.isOp()){
            } else{
                sender.sendMessage(prefix + plugin.getMessage("nopermission"));
                return true;
            }
            Player player = (Player) sender;
            Player target = player;

            if (args.length == 0) {
                player.sendMessage(prefix + plugin.getMessage("gamemode_usage"));
                return true;
            }

            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "1":
                    case "creative":
                    case "c":
                        player.sendMessage(prefix + plugin.getMessage("gamemode_set_creative"));
                        player.setGameMode(GameMode.CREATIVE);
                        break;
                    case "0":
                    case "survival":
                    case "s":
                        player.sendMessage(prefix + plugin.getMessage("gamemode_set_survival"));
                        player.setGameMode(GameMode.SURVIVAL);
                        break;
                    case "2":
                    case "adventure":
                    case "a":
                        player.sendMessage(prefix + plugin.getMessage("gamemode_set_adventure"));
                        player.setGameMode(GameMode.ADVENTURE);
                        break;
                    case "3":
                    case "spectator":
                    case "sp":
                        player.sendMessage(prefix + plugin.getMessage("gamemode_set_spectator"));
                        player.setGameMode(GameMode.SPECTATOR);
                        break;
                    default:
                        player.sendMessage(prefix + plugin.getMessage("gamemode_invalid"));
                        break;
                }
                return true;
            }

            if (args.length == 2) {
                target = player.getServer().getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(prefix + plugin.getMessage("player_label") + " " + args[1] + " " + plugin.getMessage("player_not_found"));
                    return true;
                }

                switch (args[0].toLowerCase()) {
                    case "1":
                    case "creative":
                    case "c":
                        if (!target.getName().equalsIgnoreCase(player.getName())){
                        target.sendMessage(prefix + player.getName() + plugin.getMessage("gamemode_set_creative_self"));
                        }
                        target.setGameMode(GameMode.CREATIVE);
                        player.sendMessage(prefix + plugin.getMessage("action_for") + " " + target.getName() + " " + plugin.getMessage("gamemode_set_creative_target"));
                        break;
                    case "0":
                    case "survival":
                    case "s":
                        if (!target.getName().equalsIgnoreCase(player.getName())) {
                            target.sendMessage(prefix + player.getName() + plugin.getMessage("gamemode_set_survival_self"));
                        }
                        target.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage(prefix + plugin.getMessage("action_for") + " " + target.getName() + " " + plugin.getMessage("gamemode_set_survival_target"));
                        break;
                    case "2":
                    case "adventure":
                    case "a":
                        if (!target.getName().equalsIgnoreCase(player.getName())) {
                            target.sendMessage(prefix + player.getName() + plugin.getMessage("gamemode_set_adventure_self"));
                        }
                        target.setGameMode(GameMode.ADVENTURE);
                        player.sendMessage(prefix + plugin.getMessage("action_for") + " " + target.getName() + " " + plugin.getMessage("gamemode_set_adventure_target"));
                        break;
                    case "3":
                    case "spectator":
                    case "sp":
                        if (!target.getName().equalsIgnoreCase(player.getName())){
                            target.sendMessage(prefix + player.getName() + plugin.getMessage("gamemode_set_spectator_self"));
                        }
                        target.setGameMode(GameMode.SPECTATOR);
                        player.sendMessage(prefix + plugin.getMessage("action_for") + " " + target.getName() + " " + plugin.getMessage("gamemode_set_spectator_target"));
                        break;
                    default:
                        player.sendMessage(prefix + plugin.getMessage("gamemode_usage"));
                        break;
                }
                return true;
            }
            player.sendMessage(prefix + plugin.getMessage("gamemode_usage"));
            return true;
        }
        return false;
    }
}
