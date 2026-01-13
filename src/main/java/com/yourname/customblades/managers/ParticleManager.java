package com.yourname.customblades.managers;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ParticleManager {
    
    private final CustomBlades plugin;
    private final Set<UUID> playersWithBlades;
    private BukkitTask particleTask;
    
    public ParticleManager(CustomBlades plugin) {
        this.plugin = plugin;
        this.playersWithBlades = new HashSet<>();
    }
    
    public void startParticleTask() {
        particleTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ItemStack mainHand = player.getInventory().getItemInMainHand();
                BladeType mainType = plugin.getBladeManager().getBladeType(mainHand);
                
                if (mainType != null) {
                    spawnBladeParticles(player, mainType);
                    playersWithBlades.add(player.getUniqueId());
                } else {
                    playersWithBlades.remove(player.getUniqueId());
                }
            }
        }, 0L, 5L); // Every 5 ticks
    }
    
    public void stopParticleTask() {
        if (particleTask != null) {
            particleTask.cancel();
        }
    }
    
    private void spawnBladeParticles(Player player, BladeType type) {
        // Particles around player's body
        double radius = 0.8;
        int points = 8;
        
        for (int i = 0; i < points; i++) {
            double angle = (2 * Math.PI / points) * i + (System.currentTimeMillis() / 500.0);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                type.getPrimaryParticle(),
                player.getLocation().add(x, 1.0, z),
                1, 0, 0, 0, 0
            );
        }
        
        // Trail particles
        if (player.getVelocity().lengthSquared() > 0.01) {
            player.getWorld().spawnParticle(
                type.getSecondaryParticle(),
                player.getLocation().add(0, 1, 0),
                3, 0.3, 0.3, 0.3, 0
            );
        }
    }
    
    public boolean hasBladeParticles(UUID playerId) {
        return playersWithBlades.contains(playerId);
    }
}
