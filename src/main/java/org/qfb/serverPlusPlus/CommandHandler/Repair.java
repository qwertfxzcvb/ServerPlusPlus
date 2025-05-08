package org.qfb.serverPlusPlus.CommandHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.qfb.serverPlusPlus.ServerPlusPlus;

public class Repair implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;

    public Repair(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("repair")) {
            repair(sender, args, player);
        }
        if (command.getName().equalsIgnoreCase("fix")) {
            repair(sender, args, player);
        }
        return true;
    }

    private void repair(CommandSender sender, String[] args, Player player) {
        if (args.length == 0) {
            if (sender.hasPermission("spp.admin") || sender.hasPermission("spp.repair") || sender.isOp()) {
                player.getItemInHand().setDurability((short) 0);
                sender.sendMessage(prefix + ChatColor.GREEN + plugin.getMessage("item_repaired"));
            } else {
                sender.sendMessage(prefix + plugin.getMessage("nopermission"));
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("all")) {
                if (sender.hasPermission("spp.admin") || sender.hasPermission("spp.repair") || sender.isOp()) {
                    ItemStack[] inventory = player.getInventory().getContents();
                    for (ItemStack item : inventory) {
                        if (item != null) {
                            item.setDurability((short) 0);
                        }
                    }
                    player.updateInventory();
                    sender.sendMessage(prefix + ChatColor.GREEN + plugin.getMessage("inventory_item_repaired"));
                } else {
                    sender.sendMessage(prefix + plugin.getMessage("nopermission"));
                }
            }
        }
    }
}
