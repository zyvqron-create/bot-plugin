package com.bots.plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BotManager {

    private JavaPlugin plugin;
    private List<NpcBot> bots = new ArrayList<>();

    public BotManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void spawnBots(org.bukkit.entity.Player player) {
        World world = player.getWorld();

        // Первый бот (смотрит вперед)
        Location loc1 = new Location(world, 310, 64, -9);
        NpcBot bot1 = new NpcBot(plugin, "xyz1", loc1, true);

        // Второй бот (смотрит назад)
        Location loc2 = new Location(world, 301, 64, -9);
        NpcBot bot2 = new NpcBot(plugin, "xyz2", loc2, false);

        bots.add(bot1);
        bots.add(bot2);

        // Синхронизация ботов
        new BotSequence(plugin, bot1, bot2).startSequence();
    }

    public void removeAllBots() {
        for (NpcBot bot : bots) {
            bot.remove();
        }
        bots.clear();
    }
}