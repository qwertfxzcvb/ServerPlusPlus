package org.qfb.serverPlusPlus.CommandHandler;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.qfb.serverPlusPlus.ServerPlusPlus;

public class Weather implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;

    public Weather(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //限制玩家执行命令
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + plugin.getMessage("only_player_can_execute"));
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();

        if (!sender.hasPermission("spp.admin") && !sender.hasPermission("spp.weather") && !sender.isOp()) {
            //检查权限
            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "sun":
                player.sendMessage(prefix + plugin.getMessage("weather_set_sunny"));
                //设置晴天
                world.setStorm(false);
                world.setThundering(false);
                world.setWeatherDuration(100000);
                break;

            case "rain":
                player.sendMessage(prefix + plugin.getMessage("weather_set_rain"));
                //设置降雨
                world.setStorm(true);
                world.setWeatherDuration(100000);
                break;

            case "thunder":
                player.sendMessage(prefix + plugin.getMessage("weather_set_thunder"));
                //设置雷暴
                world.setStorm(true);
                world.setThundering(true);
                world.setWeatherDuration(100000);
                break;

            case "weather":
                if (args.length == 0) {
                    player.sendMessage(prefix + plugin.getMessage("weather_usage"));
                    //提示用法
                    return true;
                }
                switch (args[0].toLowerCase()) {
                    case "sun":
                        player.sendMessage(prefix + plugin.getMessage("weather_set_sunny"));
                        //设置晴天
                        world.setStorm(false);
                        world.setThundering(false);
                        world.setWeatherDuration(100000);
                        break;
                    case "rain":
                        player.sendMessage(prefix + plugin.getMessage("weather_set_rain"));
                        //设置降雨
                        world.setStorm(true);
                        world.setWeatherDuration(100000);
                        break;
                    case "thunder":
                        player.sendMessage(prefix + plugin.getMessage("weather_set_thunder"));
                        //设置雷暴
                        world.setStorm(true);
                        world.setThundering(true);
                        world.setWeatherDuration(100000);
                        break;
                    default:
                        player.sendMessage(prefix + plugin.getMessage("weather_usage"));
                        //提示用法
                        break;
                }
                break;

            default:
                return false;
        }
        return true;
    }
}
