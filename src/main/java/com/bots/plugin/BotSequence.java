package com.bots.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BotSequence {

    private JavaPlugin plugin;
    private NpcBot bot1;
    private NpcBot bot2;

    public BotSequence(JavaPlugin plugin, NpcBot bot1, NpcBot bot2) {
        this.plugin = plugin;
        this.bot1 = bot1;
        this.bot2 = bot2;
    }

    public void startSequence() {
        new BukkitRunnable() {
            int cycle = 0;

            @Override
            public void run() {
                if (cycle == 0) {
                    bot2.walkForward(1, 1000);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            bot2.lookDown();
                        }
                    }.runTaskLater(plugin, 20);
                }

                if (cycle == 1) {
                    bot2.lookBack();
                }

                if (cycle == 2) {
                    bot1.lookBack();
                }

                if (cycle == 3) {
                    // Ждём
                }

                if (cycle == 4) {
                    bot1.lookForward();
                }

                if (cycle == 5) {
                    bot2.walkForward(1, 1000);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            bot2.lookBack();
                        }
                    }.runTaskLater(plugin, 20);
                }

                if (cycle == 6) {
                    bot1.lookAtBot(bot2);
                }

                if (cycle == 7) {
                    bot1.spawnParticles();
                    bot2.spawnParticles();
                }

                if (cycle >= 8) {
                    cycle = -1;
                }

                cycle++;
            }
        }.runTaskTimer(plugin, 0, 30);
    }
}