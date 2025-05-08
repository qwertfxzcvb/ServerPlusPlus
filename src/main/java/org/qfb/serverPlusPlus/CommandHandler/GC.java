package org.qfb.serverPlusPlus.CommandHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.qfb.serverPlusPlus.ServerPlusPlus;

import java.lang.management.ManagementFactory;

public class GC implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;

    public GC(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("gc") &&
                !command.getName().equalsIgnoreCase("tps") &&
                !command.getName().equalsIgnoreCase("server_info")) {
            return false;
        }

        if (!sender.hasPermission("spp.admin") && !sender.hasPermission("spp.gc") && !sender.isOp()) {
            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
            return true;
        }

        boolean isplayer = sender instanceof Player;
        Player player = isplayer ? (Player) sender : null;

        long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        long uptime = System.currentTimeMillis() - jvmStartTime;
        long hours = uptime / (1000 * 60 * 60);
        long minutes = (uptime / (1000 * 60)) % 60;
        long seconds = (uptime / 1000) % 60;

        double tps = Bukkit.getServer().getTPS()[0];

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long allocatedMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;

        String message = prefix + plugin.getMessage("server_info") + "\n" +
                plugin.getMessage("server_uptime") + ChatColor.WHITE + hours + plugin.getMessage("time_hour") + ChatColor.WHITE + minutes + plugin.getMessage("time_minute") + ChatColor.WHITE +seconds + plugin.getMessage("time_second") + "\n" +
                plugin.getMessage("server_tps") + ChatColor.WHITE + tps + "\n" +
                plugin.getMessage("server_max_memory") + ChatColor.WHITE + maxMemory + "MB\n" +
                plugin.getMessage("server_allocated_memory") + ChatColor.WHITE + allocatedMemory + "MB\n" +
                plugin.getMessage("server_free_memory") + ChatColor.WHITE + freeMemory + "MB";
        if (isplayer) {
            player.sendMessage(message);
        } else {
            sender.sendMessage(ChatColor.stripColor(message));
        }

        for (World world : Bukkit.getWorlds()) {
            int chunks = world.getLoadedChunks().length;
            int entities = world.getEntities().size();
            int tileEntities = getTileEntityCount(world);

            String worldInfo = ChatColor.GOLD + plugin.getMessage("world_label") + ChatColor.WHITE + world.getName() + "\": " + chunks + plugin.getMessage("world_chunk") + entities + plugin.getMessage("world_entity") + tileEntities + plugin.getMessage("world_tile_entity");
            if (isplayer) {
                player.sendMessage(worldInfo);
            } else {
                sender.sendMessage(ChatColor.stripColor(worldInfo));
            }
        }
        return true;
    }

    private int getTileEntityCount(World world) {
        int count = 0;
        for (Chunk chunk : world.getLoadedChunks()) {
            for (BlockState blockState : chunk.getTileEntities()) {
                count++;
            }
        }
        return count;
    }
}
