package org.qfb.serverPlusPlus.CommandHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.qfb.serverPlusPlus.Event.TPAEvent;
import org.qfb.serverPlusPlus.ServerPlusPlus;

public class TPA implements CommandExecutor {
    private final ServerPlusPlus plugin;
    private final String prefix;

    public TPA(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
    }

    static HashMap<UUID, UUID> targetMap = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + plugin.getMessage("only_player_can_execute"));
            return true;
        }

        if (command.getName().equals("tpa")) {
            handleTpaCommand(sender, args);
            return true;
        }

        if (command.getName().equals("tpaccept")) {
            handleTpaAcceptCommad(sender);
            return true;
        }

        if (command.getName().equals("tpdeny")) {
            handleTpaDenyCommand(sender);
            return true;
        }

        return false;
    }

    private void handleTpaDenyCommand(CommandSender sender) {
        if (!(sender.hasPermission("spp.admin") || sender.hasPermission("spp.tpa.deny") || sender.isOp())) {
            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
            return;
        }

        final Player senderP = (Player) sender;

        if (!targetMap.containsValue(senderP.getUniqueId())) {
            sender.sendMessage(prefix + plugin.getMessage("tpa_no_pending_request"));
            return;
        }

        for (Map.Entry<UUID, UUID> entry : targetMap.entrySet()) {
            if (entry.getValue().equals(senderP.getUniqueId())) {
                targetMap.remove(entry.getKey());
                Player originalSender = Bukkit.getPlayer(entry.getKey());
                originalSender.sendMessage(prefix + plugin.getMessage("tpa_request_Rejected"));
                sender.sendMessage(prefix + plugin.getMessage("tpa_rejected"));
                break;
            }
        }
    }

    private void handleTpaAcceptCommad(CommandSender sender) {
        if (!(sender.hasPermission("spp.admin") || sender.hasPermission("spp.tpa.accept") || sender.isOp())) {
            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
            return;
        }

        final Player senderP = (Player) sender;

        if (!targetMap.containsValue(senderP.getUniqueId())) {
            sender.sendMessage(prefix + plugin.getMessage("tpa_no_pending_request"));
            return;
        }

        sender.sendMessage(prefix + plugin.getMessage("tpa_request_accepted"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }



        for (Map.Entry<UUID, UUID> entry : targetMap.entrySet()) {
            if (entry.getValue().equals(senderP.getUniqueId())) {
                Player tpRequester = Bukkit.getPlayer(entry.getKey());

                TPAEvent event = new TPAEvent(tpRequester, tpRequester.getLocation());
                Bukkit.getPluginManager().callEvent(event);

                tpRequester.teleport(senderP);

                targetMap.remove(entry.getKey());
                break;
            }
        }
    }

    private void handleTpaCommand(CommandSender sender, String[] args) {

        if (!(sender.hasPermission("spp.admin") || sender.hasPermission("spp.tpa.tpa") || sender.isOp())) {
            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
            return;
        }

        if (args.length != 1) {
            sender.sendMessage(prefix + plugin.getMessage("tpa_usage"));
            return;
        }

        if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
            sender.sendMessage(prefix + plugin.getMessage("tpa_player_not_online"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        final Player senderP = (Player) sender;

        if (target.getUniqueId().equals(senderP.getUniqueId())) {
            sender.sendMessage(prefix + plugin.getMessage("tpa_cannot_target_self"));
            return;
        }

        if (targetMap.containsKey(senderP.getUniqueId())) {
            sender.sendMessage(prefix + plugin.getMessage("tpa_request_pending"));
            return;
        }

        //消息处理
        TextComponent tpa_tpaccept = new TextComponent(plugin.getMessage("tpa_tpaccept"));
        TextComponent tpa_tpdeny = new TextComponent(plugin.getMessage("tpa_tpdeny"));
        tpa_tpaccept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        tpa_tpdeny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
        tpa_tpaccept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(plugin.getMessage("tpa_tpaccept_hover"))));
        tpa_tpdeny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(plugin.getMessage("tpa_tpdeny_hover"))));
        TextComponent finalMessage = new TextComponent(prefix + " ");
        finalMessage.addExtra(tpa_tpaccept);
        finalMessage.addExtra("      ");
        finalMessage.addExtra(tpa_tpdeny);
        String receive_tpa_request = plugin.getMessage("receive_tpa_request").replace("{player}", senderP.getName());
        String Send_tpa_request = plugin.getMessage("Send_tpa_request").replace("{player}", target.getName());
        //发送消息
        senderP.sendMessage(prefix + Send_tpa_request);
        target.sendMessage(prefix + receive_tpa_request);
        target.sendMessage(prefix + plugin.getMessage("tpa_request_decision"));
        target.sendMessage(prefix + plugin.getMessage("tpa_request_time_limit"));
        target.sendMessage(" ");
        target.spigot().sendMessage(finalMessage);
        target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
        targetMap.put(senderP.getUniqueId(), target.getUniqueId());

        (new BukkitRunnable() {
            public void run() {
                TPA.targetMap.remove(senderP.getUniqueId());
            }
        }).runTaskLaterAsynchronously(this.plugin, 6000L);
    }
}