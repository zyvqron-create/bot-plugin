package com.bots.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BotPlugin extends JavaPlugin {

    private BotManager botManager;

    @Override
    public void onEnable() {
        botManager = new BotManager(this);
        getLogger().info("§a[BotPlugin] Плагин загружен!");
    }

    @Override
    public void onDisable() {
        if (botManager != null) {
            botManager.removeAllBots();
        }
        getLogger().info("§c[BotPlugin] Плагин выключен!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawnbots")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cЭта команда только для игроков!");
                return true;
            }
            Player player = (Player) sender;
            botManager.spawnBots(player);
            player.sendMessage("§a[BotPlugin] Боты созданы!");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("removebots")) {
            botManager.removeAllBots();
            sender.sendMessage("§a[BotPlugin] Боты удалены!");
            return true;
        }

        return false;
    }
}