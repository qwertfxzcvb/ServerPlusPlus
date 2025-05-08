package org.qfb.serverPlusPlus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.qfb.serverPlusPlus.ServerPlusPlus;

import java.io.File;

public class SPP implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;

    public SPP(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spp")) {
            if (args.length == 0) {
                if (sender.hasPermission("spp.admin") || sender.hasPermission("spp.help") || sender.isOp()) {
                    Player player = (Player) sender;
                    player.sendMessage(prefix + "ServerPlusPlus");
                    player.sendMessage("/spp reload - 重载SPP");
                    player.sendMessage("/spp help - 查看帮助");
                    player.sendMessage("/spp version - 查看插件版本");
                } else {
                    sender.sendMessage(prefix + plugin.getMessage("nopermission"));
                }
            }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        if (sender.hasPermission("spp.admin") || sender.hasPermission("spp.help") || sender.isOp()) {
                            Player player = (Player) sender;
                            player.sendMessage(prefix + "帮助");
                            player.sendMessage(ChatColor.GOLD + "/gamemode " + ChatColor.WHITE + " 切换游戏模式");
                            player.sendMessage(ChatColor.GOLD + "/fly " + ChatColor.WHITE + " 开启飞行");
                            player.sendMessage(ChatColor.GOLD + "/weather " + ChatColor.WHITE + "切换天气");
                            player.sendMessage(ChatColor.GOLD + "/time " + ChatColor.WHITE + " 设置时间");
                            player.sendMessage(ChatColor.GOLD + "/suicide " + ChatColor.WHITE + " 自杀");
                            player.sendMessage(ChatColor.GOLD + "/repair " + ChatColor.WHITE + " 修复物品");
                            player.sendMessage(ChatColor.GOLD + "/gc " + ChatColor.WHITE + " 查看服务器状态");
                            player.sendMessage(ChatColor.GOLD + "/tpa " + ChatColor.WHITE + " 请求传送到玩家");
                            player.sendMessage(ChatColor.GOLD + "/vanish " + ChatColor.WHITE + " 隐身");
                        } else {
                            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
                        }
                    }
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (sender.hasPermission("spp.admin") || sender.hasPermission("spp.reload") || sender.isOp()) {
                            Player player = (Player) sender;
                            plugin.reloadConfig();
                            player.sendMessage(prefix + "插件已重载");
                        } else {
                            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
                        }
                    }
                    if (args[0].equalsIgnoreCase("version")) {
                        if (sender.hasPermission("spp.admin") || sender.hasPermission("spp.version") || sender.isOp()) {
                            Player player = (Player) sender;
                            player.sendMessage(prefix + "版本: ServerPlusPlus v1.0 25.4.2");
                            player.sendMessage(prefix + "作者: qwertfxzcvb");
                            player.sendMessage(prefix + "QQ：1444001949");
                        } else {
                            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
                        }
                    }
                }
            }
            return true;
        }
    }
