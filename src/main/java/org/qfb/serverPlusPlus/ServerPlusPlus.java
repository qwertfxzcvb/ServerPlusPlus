package org.qfb.serverPlusPlus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.qfb.serverPlusPlus.CommandHandler.*;
import org.qfb.serverPlusPlus.Event.VanishEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class ServerPlusPlus extends JavaPlugin implements Listener {
    private static ServerPlusPlus instance;
    private String prefix;
    private YamlConfiguration config;
    private final Map<String, String> messages = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();

        Bukkit.getPluginManager().registerEvents(this, this);

        Vanish vanish = new Vanish(this);
        new VanishEvent(this, vanish);

        registerCommands();
        TabCompleter();

        getLogger().info("插件已加载");
    }


    public static ServerPlusPlus getInstance() {
        return instance;
    }


    public String getMessage(String key) {
        String message = messages.get(key);
        if (message == null) {
            getLogger().warning("找不到消息 key: " + key);
            return ChatColor.RED + "[缺失: " + key + "]";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }



    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    @SuppressWarnings("DataFlowIssue")
    private void registerCommands() {
        getCommand("spp").setExecutor(new SPP(this));
        getCommand("gm").setExecutor(new Gamemode(this));
        getCommand("gamemode").setExecutor(new Gamemode(this));
        getCommand("fly").setExecutor(new Fly(this));
        getCommand("weather").setExecutor(new Weather(this));
        getCommand("sun").setExecutor(new Weather(this));
        getCommand("rain").setExecutor(new Weather(this));
        getCommand("thunder").setExecutor(new Weather(this));
        getCommand("time").setExecutor(new Time(this));
        getCommand("day").setExecutor(new Time(this));
        getCommand("night").setExecutor(new Time(this));
        getCommand("midnight").setExecutor(new Time(this));
        getCommand("noon").setExecutor(new Time(this));
        getCommand("suicide").setExecutor(new Suicide(this));
        getCommand("repair").setExecutor(new Repair(this));
        getCommand("fix").setExecutor(new Repair(this));
        getCommand("gc").setExecutor(new GC(this));
        getCommand("tps").setExecutor(new GC(this));
        getCommand("serverinfo").setExecutor(new GC(this));
        getCommand("tpa").setExecutor(new TPA(this));
        getCommand("tpaccept").setExecutor(new TPA(this));
        getCommand("tpdeny").setExecutor(new TPA(this));
        getCommand("invsee").setExecutor(new Invsee(this));
        getCommand("endersee").setExecutor(new Invsee(this));
        getCommand("vanish").setExecutor(new Vanish(this));
    }
    @SuppressWarnings("DataFlowIssue")
    private void TabCompleter() {
        getCommand("spp").setTabCompleter(new TabCompleter());
        getCommand("gm").setTabCompleter(new TabCompleter());
        getCommand("gamemode").setTabCompleter(new TabCompleter());
        getCommand("fly").setTabCompleter(new TabCompleter());
        getCommand("weather").setTabCompleter(new TabCompleter());
        getCommand("time").setTabCompleter(new TabCompleter());
        getCommand("repair").setTabCompleter(new TabCompleter());
        getCommand("fix").setTabCompleter(new TabCompleter());
        getCommand("tpa").setTabCompleter(new TabCompleter());
        getCommand("invsee").setTabCompleter(new TabCompleter());
        getCommand("endersee").setTabCompleter(new TabCompleter());
    }
    private void loadConfig() {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists() || file.length() == 0) {
            saveResource("config.yml", true);
            getLogger().info("未检测到配置文件，已创建");
        }

        reloadConfig();
        getLogger().info("配置文件已加载");

        FileConfiguration cfg = getConfig();
        prefix = cfg.getString("prefix", "&f[ServerPlusPlus]&r ");
        if (cfg.isConfigurationSection("messages")) {
            for (String key : cfg.getConfigurationSection("messages").getKeys(false)) {
                String value = cfg.getString("messages." + key);
                if (value != null) {
                    messages.put(key, value);
                } else {
                    getLogger().warning("messages." + key + " 的值是 null，已跳过");
                }
            }
        } else {
            getLogger().warning("未找到消息节点，请检查 config.yml！");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("插件已卸载");
    }
}
