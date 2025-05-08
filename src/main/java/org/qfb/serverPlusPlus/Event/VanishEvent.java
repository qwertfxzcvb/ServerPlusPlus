package org.qfb.serverPlusPlus.Event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.qfb.serverPlusPlus.CommandHandler.Vanish;
import org.qfb.serverPlusPlus.ServerPlusPlus;

public class VanishEvent implements Listener {

    private final ServerPlusPlus plugin;
    private final Vanish vanishHandler;

    public VanishEvent(ServerPlusPlus plugin, Vanish vanishHandler) {
        this.plugin = plugin;
        this.vanishHandler = vanishHandler;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        vanishHandler.removeVanishOnQuit(player);
    }
}