package com.yourname.customblades.objects;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Ritual {
    
    private final CustomBlades plugin;
    private final Player player;
    private final BladeType bladeType;
    private final Location center;
    private final int totalTime;
    private int remainingTime;
    private BukkitTask ritualTask;
    private ItemDisplay bladeDisplay;
    private List<Block> ritualBlocks;
    private boolean cancelled;
    
    public Ritual(CustomBlades plugin, Player player, BladeType bladeType, Location center, int totalTime) {
        this.plugin = plugin;
        this.player = player;
        this.bladeType = bladeType;
        this.center = center;
        this.totalTime = totalTime;
        this.remainingTime = totalTime;
        this.ritualBlocks = new ArrayList<>();
        this.cancelled = false;
    }
    
    public void start() {
        // Create 6x6 ritual platform
        createRitualPlatform();
        
        // Spawn floating blade display
        spawnBladeDisplay();
        
        // Start ritual timer
        startRitualTimer();
        
        // Play start sound
        center.getWorld().playSound(center, Sound.ENTITY_WARDEN_SONIC_BOOM, 2.0f, 0.5f);
        center.getWorld().playSound(center, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 2.0f, 1.0f);
        
        player.sendMessage("§d§l⚡ §5Ritual Started for " + bladeType.getDisplayName());
        player.sendMessage("§7Duration: §e" + formatTime(totalTime));
    }
    
    private void createRitualPlatform() {
        World world = center.getWorld();
        int centerX = center.getBlockX();
        int centerY = center.getBlockY();
        int centerZ = center.getBlockZ();
        
        // Create 6x6 platform
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                Block block = world.getBlockAt(centerX + x, centerY, centerZ + z);
                ritualBlocks.add(block);
                block.setType(bladeType.getRitualBlock());
            }
        }
    }
    
    private void spawnBladeDisplay() {
        Location displayLoc = center.clone().add(0.5, 2.5, 0.5);
        ItemStack blade = plugin.getBladeManager().createBlade(bladeType);
        
        bladeDisplay = center.getWorld().spawn(displayLoc, ItemDisplay.class, display -> {
            display.setItemStack(blade);
            display.setBillboard(Display.Billboard.VERTICAL);
            display.setDisplayHeight(1.5f);
            display.setDisplayWidth(1.5f);
            display.setGlowing(true);
            display.setPersistent(false);
            
            // Set transformation for better visibility
            Transformation trans = display.getTransformation();
            trans.getScale().set(2.0f, 2.0f, 2.0f);
            display.setTransformation(trans);
            
            // Set team color based on blade
            display.setGlowColorOverride(bladeType.getParticleColor());
        });
        
        // Rotate blade display
        new BukkitRunnable() {
            float angle = 0;
            
            @Override
            public void run() {
                if (cancelled || bladeDisplay == null || !bladeDisplay.isValid()) {
                    cancel();
                    return;
                }
                
                angle += 5;
                if (angle >= 360) angle = 0;
                
                Transformation trans = bladeDisplay.getTransformation();
                trans.getLeftRotation().set(new AxisAngle4f((float) Math.toRadians(angle), 0, 1, 0));
                bladeDisplay.setTransformation(trans);
                bladeDisplay.setInterpolationDuration(1);
                bladeDisplay.setInterpolationDelay(0);
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void startRitualTimer() {
        ritualTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (cancelled) {
                    cancel();
                    return;
                }
                
                remainingTime--;
                
                // Spawn particles
                spawnRitualParticles();
                
                // Show progress to nearby players
                showProgress();
                
                // Play periodic sounds
                if (remainingTime % 60 == 0) {
                    center.getWorld().playSound(center, Sound.BLOCK_BEACON_AMBIENT, 1.0f, 1.5f);
                }
                
                if (remainingTime <= 0) {
                    complete();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    private void spawnRitualParticles() {
        World world = center.getWorld();
        
        // Circular particle ring
        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i + (System.currentTimeMillis() / 50.0) % 360);
            double radius = 3.5;
            
            double x = center.getX() + 0.5 + radius * Math.cos(angle);
            double z = center.getZ() + 0.5 + radius * Math.sin(angle);
            double y = center.getY() + 0.1;
            
            Location particleLoc = new Location(world, x, y, z);
            world.spawnParticle(bladeType.getPrimaryParticle(), particleLoc, 1, 0, 0, 0, 0);
        }
        
        // Vertical spiral
        double height = (totalTime - remainingTime) / (double) totalTime * 2.0;
        for (int i = 0; i < 20; i++) {
            double angle = Math.toRadians(i * 18 + (System.currentTimeMillis() / 30.0) % 360);
            double radius = 1.5;
            
            double x = center.getX() + 0.5 + radius * Math.cos(angle);
            double z = center.getZ() + 0.5 + radius * Math.sin(angle);
            double y = center.getY() + height;
            
            Location particleLoc = new Location(world, x, y, z);
            world.spawnParticle(bladeType.getSecondaryParticle(), particleLoc, 1, 0, 0, 0, 0);
        }
        
        // Pillar particles
        for (int i = 0; i < 5; i++) {
            Location pillarLoc = center.clone().add(0.5, i * 0.5, 0.5);
            world.spawnParticle(Particle.ENCHANT, pillarLoc, 10, 0.3, 0.3, 0.3, 0.5);
        }
    }
    
    private void showProgress() {
        double progress = (totalTime - remainingTime) / (double) totalTime;
        int barLength = 20;
        int filled = (int) (progress * barLength);
        
        StringBuilder bar = new StringBuilder("§8[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                bar.append("§a■");
            } else {
                bar.append("§7■");
            }
        }
        bar.append("§8]");
        
        String timeLeft = formatTime(remainingTime);
        String message = bar.toString() + " §e" + timeLeft;
        
        // Show to all players nearby
        for (Player p : center.getWorld().getPlayers()) {
            if (p.getLocation().distance(center) <= 50) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
                    new TextComponent("§d§lRitual Progress: " + message));
            }
        }
    }
    
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
    
    private void complete() {
        // Remove blade display
        if (bladeDisplay != null) {
            bladeDisplay.remove();
        }
        
        // Drop the blade
        ItemStack blade = plugin.getBladeManager().createBlade(bladeType);
        center.getWorld().dropItemNaturally(center.clone().add(0.5, 1, 0.5), blade);
        
        // Epic completion effects
        World world = center.getWorld();
        world.playSound(center, Sound.ENTITY_ENDER_DRAGON_DEATH, 2.0f, 1.2f);
        world.playSound(center, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f, 1.0f);
        world.playSound(center, Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 1.5f);
        
        // Explosion particles
        for (int i = 0; i < 100; i++) {
            world.spawnParticle(bladeType.getPrimaryParticle(), 
                center.clone().add(0.5, 1.5, 0.5), 50, 2, 2, 2, 0.5);
            world.spawnParticle(Particle.FIREWORK, 
                center.clone().add(0.5, 1.5, 0.5), 30, 1, 1, 1, 0.3);
        }
        
        player.sendMessage("§a§l✓ §2Ritual Complete! " + bladeType.getDisplayName() + " §2has been forged!");
        
        // Remove ritual blocks
        removeRitualBlocks();
        
        // Remove from manager
        plugin.getRitualManager().removeRitual(center);
    }
    
    public void cancel() {
        cancelled = true;
        
        if (ritualTask != null) {
            ritualTask.cancel();
        }
        
        if (bladeDisplay != null) {
            bladeDisplay.remove();
        }
        
        removeRitualBlocks();
        
        center.getWorld().playSound(center, Sound.ENTITY_WITHER_BREAK_BLOCK, 1.0f, 0.5f);
        
        plugin.getRitualManager().removeRitual(center);
    }
    
    private void removeRitualBlocks() {
        for (Block block : ritualBlocks) {
            block.setType(Material.AIR);
        }
        ritualBlocks.clear();
    }
    
    public int getRemainingTime() {
        return remainingTime;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
                   }
