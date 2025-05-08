package org.qfb.serverPlusPlus.CommandHandler;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.qfb.serverPlusPlus.ServerPlusPlus;

import java.util.HashMap;
import java.util.Map;

public class Time implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;

    private static final Map<String, Long> time_settings = new HashMap<>();

    static {
        time_settings.put("day", 1000L);
        time_settings.put("midnight", 18000L);
        time_settings.put("night", 13000L);
        time_settings.put("noon", 6000L);
    }

    public Time(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }

    public boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //设置时间
    private void setTime(Player player, World world, String timeName) {
        if (time_settings.containsKey(timeName)) {
            long time = time_settings.get(timeName);
            player.sendMessage(prefix + plugin.getMessage("time_set") + timeName + " (" + time + ")");
            world.setTime(time);
        } else {
            player.sendMessage(prefix + plugin.getMessage("time_usage_set"));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + plugin.getMessage("only_player_can_execute"));
            return true;
        }
        Player player = (Player) sender;
        World world = player.getWorld();

        if (!(sender.hasPermission("spp.admin") || sender.hasPermission("spp.time") || sender.isOp())) {
            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
            return true;
        }

        if (command.getName().equalsIgnoreCase("day")) {
            setTime(player, world, "day");
        } else if (command.getName().equalsIgnoreCase("midnight")) {
            setTime(player, world, "midnight");
        } else if (command.getName().equalsIgnoreCase("night")) {
            setTime(player, world, "night");
        } else if (command.getName().equalsIgnoreCase("noon")) {
            setTime(player, world, "noon");
        } else if (command.getName().equalsIgnoreCase("time")) {
            if (args.length == 0) {
                player.sendMessage(prefix + plugin.getMessage("time_usage_general"));
                return true;
            }

            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "set":
                        player.sendMessage(prefix + plugin.getMessage("time_usage_set"));
                        break;
                    case "add":
                        player.sendMessage(prefix + plugin.getMessage("time_usage_add"));
                        break;
                    default:
                        player.sendMessage(prefix + plugin.getMessage("time_usage_general"));
                        break;
                }
                return true;
            }

            if (args.length == 2) {
                if ("set".equalsIgnoreCase(args[0])) {
                    setTime(player, world, args[1].toLowerCase());
                } else if ("add".equalsIgnoreCase(args[0])) {
                    if (isNumeric(args[1])) {
                        long ticksToAdd = Long.parseLong(args[1]);
                        world.setTime(world.getTime() + ticksToAdd);
                        player.sendMessage(prefix + plugin.getMessage("time_added") + world.getTime());
                    } else {
                        player.sendMessage(prefix + plugin.getMessage("time_invalid_number"));
                    }
                }
            }
        }

        return true;
    }
}
