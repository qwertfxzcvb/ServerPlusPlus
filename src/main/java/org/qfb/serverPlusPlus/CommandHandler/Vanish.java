package org.qfb.serverPlusPlus.CommandHandler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.qfb.serverPlusPlus.ServerPlusPlus;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.*;

public class Vanish implements CommandExecutor {

    private final ServerPlusPlus plugin;
    private final String prefix;
    private final Set<UUID> vanishedPlayers = new HashSet<>();
    private Team vanishTeam;
    private ProtocolManager protocolManager;
    private final Map<UUID, BossBar> bossBars = new HashMap<>();

    public Vanish(ServerPlusPlus plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPrefix();
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        setupVanishTeam();
        registerProtocolHandlers();
    }

    private void setupVanishTeam() {
        vanishTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("vanish");
        if (vanishTeam == null) {
            vanishTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("vanish");
        }
        vanishTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        vanishTeam.setCanSeeFriendlyInvisibles(false);
        vanishTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }

    private void registerProtocolHandlers() {
        protocolManager.addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Server.ENTITY_EQUIPMENT,
                PacketType.Play.Server.ENTITY_METADATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player receiver = event.getPlayer();
                int entityId = event.getPacket().getIntegers().read(0);
                for (UUID uuid : vanishedPlayers) {
                    Player vanished = Bukkit.getPlayer(uuid);
                    if (vanished != null && vanished.getEntityId() == entityId && !receiver.equals(vanished)) {
                        if (!receiver.hasPermission("spp.vanish.see")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        });

        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                List<PlayerInfoData> dataList = event.getPacket().getPlayerInfoDataLists().readSafely(0);
                if (dataList == null || dataList.isEmpty()) return;

                for (UUID uuid : vanishedPlayers) {
                    Player vanished = Bukkit.getPlayer(uuid);
                    if (vanished == null) continue;
                    if (event.getPlayer().equals(vanished)) continue;
                    if (event.getPlayer().hasPermission("spp.vanish.see")) continue;

                    for (PlayerInfoData data : dataList) {
                        if (data == null || data.getProfile() == null) continue;
                        String profileName = data.getProfile().getName();
                        if (profileName != null && profileName.equalsIgnoreCase(vanished.getName())) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(prefix + plugin.getMessage("only_player_can_execute"));
            return true;
        }

        if (!sender.hasPermission("spp.admin") && !sender.hasPermission("spp.vanish") && !sender.isOp()) {
            sender.sendMessage(prefix + plugin.getMessage("nopermission"));
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (vanishedPlayers.contains(uuid)) {
            showPlayer(player);
            vanishedPlayers.remove(uuid);
            sender.sendMessage(prefix + plugin.getMessage("vanish_disabled"));
        } else {
            hidePlayer(player);
            vanishedPlayers.add(uuid);
            sender.sendMessage(prefix + plugin.getMessage("vanish_enabled"));
        }

        return true;
    }
    public void removeVanishOnQuit(Player player) {
        UUID uuid = player.getUniqueId();
        vanishedPlayers.remove(uuid);
        vanishTeam.removeEntry(player.getName());
        BossBar bar = bossBars.remove(uuid);
        if (bar != null) {
            bar.removeAll();
        }
    }
    private void hidePlayer(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.equals(player) && !p.hasPermission("spp.vanish.see")) {
                p.hidePlayer(plugin, player);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                BossBar bar = Bukkit.createBossBar(plugin.getMessage("vanish_bossbar"), BarColor.YELLOW, BarStyle.SOLID);
                bar.addPlayer(player);
                bar.setVisible(true);
                bossBars.put(player.getUniqueId(), bar);
            }
        }
        vanishTeam.addEntry(player.getName());
    }

    private void showPlayer(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(plugin, player);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            BossBar bar = bossBars.remove(player.getUniqueId());
            if (bar != null) {
                bar.removeAll();
            }
        }
        vanishTeam.removeEntry(player.getName());
    }
}
