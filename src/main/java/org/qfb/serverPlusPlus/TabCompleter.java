package org.qfb.serverPlusPlus;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("gm")) {
            if (args.length == 1) {
                completions.add("creative");
                completions.add("survival");
                completions.add("adventure");
                completions.add("spectator");
            }
        }
        if (command.getName().equalsIgnoreCase("gamemode")) {
            if (args.length == 1) {
                completions.add("creative");
                completions.add("survival");
                completions.add("adventure");
                completions.add("spectator");
            }
        }
        if (command.getName().equalsIgnoreCase("gamemode")) {
            if (args.length == 2) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
        }
        if (command.getName().equalsIgnoreCase("gm")) {
            if (args.length == 2) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
        }
        if (command.getName().equalsIgnoreCase("fly")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        if (command.getName().equalsIgnoreCase("weather")) {
            if (args.length == 1) {
                completions.add("sun");
                completions.add("rain");
                completions.add("thunder");
            }
        }
        if (command.getName().equalsIgnoreCase("time")) {
            if (args.length == 1) {
                completions.add("set");
                completions.add("add");
            } else if (args.length == 2) {
                if ("set".equalsIgnoreCase(args[0])) {
                    completions.add("day");
                    completions.add("midnight");
                    completions.add("night");
                    completions.add("noon");
                } else if ("add".equalsIgnoreCase(args[0])) {
                    completions.add("1");
                    completions.add("10");
                    completions.add("100");
                    completions.add("1000");
                }
            }
        }
        if (command.getName().equalsIgnoreCase("fix")) {
            if (args.length == 1) {
                completions.add("all");
            }
        }
        if (command.getName().equalsIgnoreCase("repair")) {
            if (args.length == 1) {
                completions.add("all");
            }
        }
        if (command.getName().equalsIgnoreCase("spp")) {
            if (args.length == 1) {
                completions.add("help");
                completions.add("version");
                completions.add("reload");
            }
        }
        if (command.getName().equalsIgnoreCase("tpa")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        if (command.getName().equalsIgnoreCase("invsee")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        if (command.getName().equalsIgnoreCase("endersee")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
}
