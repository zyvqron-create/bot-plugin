package com.bots.plugin;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;

public class NpcBot {

    private JavaPlugin plugin;
    private String name;
    private Location location;
    private EntityLiving entity;
    private boolean isFirstBot;

    public NpcBot(JavaPlugin plugin, String name, Location location, boolean isFirstBot) {
        this.plugin = plugin;
        this.name = name;
        this.location = location;
        this.isFirstBot = isFirstBot;
        createEntity();
    }

    private void createEntity() {
        ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
        stand.setVisible(false);
        stand.setGravity(true);
        stand.setCustomName(name);
        stand.setCustomNameVisible(true);

        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack(
            org.bukkit.Material.SKULL_ITEM, 1, (short) 3);
        stand.getEquipment().setHelmet(head);

        entity = ((CraftEntity) stand).getHandle();
    }

    public void walkForward(double distance, long duration) {
        new org.bukkit.scheduler.BukkitRunnable() {
            private long startTime = System.currentTimeMillis();
            private Location startLoc = location.clone();

            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed >= duration) {
                    location = location.add(0, 0, distance);
                    entity.setPositionRotation(location.getX(), location.getY(), location.getZ(),
                        entity.yaw, entity.pitch);
                    cancel();
                    return;
                }

                double progress = (double) elapsed / duration;
                Location newLoc = startLoc.clone();
                newLoc.add(0, 0, distance * progress);
                entity.setPositionRotation(newLoc.getX(), newLoc.getY(), newLoc.getZ(),
                    entity.yaw, entity.pitch);
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void lookDown() {
        entity.pitch = 60;
        sendHeadRotation();
    }

    public void lookBack() {
        entity.yaw += 180;
        sendHeadRotation();
    }

    public void lookForward() {
        entity.yaw -= 180;
        sendHeadRotation();
    }

    public void lookAtBot(NpcBot other) {
        Location myLoc = entity.getBukkitEntity().getLocation();
        Location theirLoc = other.getLocation();

        double dx = theirLoc.getX() - myLoc.getX();
        double dz = theirLoc.getZ() - myLoc.getZ();

        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
        entity.yaw = yaw;
        sendHeadRotation();
    }

    private void sendHeadRotation() {
        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation(entity,
            (byte) (entity.yaw * 256 / 360));

        for (org.bukkit.entity.Player player : plugin.getServer().getOnlinePlayers()) {
            ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) player)
                .getHandle().playerConnection.sendPacket(packet);
        }
    }

    public Location getLocation() {
        return entity.getBukkitEntity().getLocation();
    }

    public void spawnParticles() {
        Location loc = getLocation();
        loc.getWorld().spawnParticle(
            org.bukkit.Particle.HEART,
            loc.getX(),
            loc.getY() + 1.5,
            loc.getZ(),
            15,
            0.5, 0.5, 0.5,
            0.1
        );
    }

    public void remove() {
        entity.getBukkitEntity().remove();
    }

    public boolean isFirstBot() {
        return isFirstBot;
    }
}