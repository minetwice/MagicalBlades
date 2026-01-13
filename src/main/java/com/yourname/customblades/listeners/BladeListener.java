package com.yourname.customblades.listeners;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class BladeListener implements Listener {
    
    private final CustomBlades plugin;
    
    public BladeListener(CustomBlades plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        
        Player player = (Player) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        BladeType bladeType = plugin.getBladeManager().getBladeType(item);
        if (bladeType == null) return;
        
        switch (bladeType) {
            case VOID_REAPER:
                handleVoidReaperPassive(player, victim);
                break;
            case ETERNAL_NIGHTMARE:
                handleEternalNightmarePassive(player, victim, event.getDamage());
                break;
            case CELESTIAL_ANNIHILATOR:
                handleCelestialAnnihilatorPassive(victim);
                break;
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && 
            event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        BladeType bladeType = plugin.getBladeManager().getBladeType(item);
        if (bladeType == null) return;
        
        if (!player.hasPermission("customblades.use")) {
            player.sendMessage("§c§l⚠ §cYou don't have permission to use blade abilities!");
            return;
        }
        
        // Handle active abilities
        switch (bladeType) {
            case VOID_REAPER:
                if (!plugin.getBladeManager().isOnCooldown(player.getUniqueId(), bladeType)) {
                    handleVoidReaperActive(player);
                    plugin.getBladeManager().setCooldown(player.getUniqueId(), bladeType, 
                        plugin.getConfigManager().getVoidReaperCooldown());
                } else {
                    int remaining = plugin.getBladeManager().getRemainingCooldown(player.getUniqueId(), bladeType);
                    player.sendMessage("§c§l⏱ §cAbility on cooldown! §7Wait §e" + remaining + "s");
                }
                break;
                
            case CELESTIAL_ANNIHILATOR:
                if (!plugin.getBladeManager().isOnCooldown(player.getUniqueId(), bladeType)) {
                    handleCelestialAnnihilatorActive(player);
                    plugin.getBladeManager().setCooldown(player.getUniqueId(), bladeType, 
                        plugin.getConfigManager().getCelestialCooldown());
                } else {
                    int remaining = plugin.getBladeManager().getRemainingCooldown(player.getUniqueId(), bladeType);
                    player.sendMessage("§c§l⏱ §cAbility on cooldown! §7Wait §e" + remaining + "s");
                }
                break;
        }
    }
    
    // VOID REAPER PASSIVE - Launch 4 blocks up
    private void handleVoidReaperPassive(Player player, LivingEntity victim) {
        double launchHeight = plugin.getConfigManager().getLaunchHeight();
        Vector launch = new Vector(0, launchHeight * 0.25, 0);
        victim.setVelocity(launch);
        
        // Effects
        victim.getWorld().spawnParticle(Particle.REVERSE_PORTAL, 
            victim.getLocation().add(0, 1, 0), 30, 0.3, 0.5, 0.3, 0.1);
        victim.getWorld().spawnParticle(Particle.WITCH, 
            victim.getLocation().add(0, 1, 0), 20, 0.3, 0.5, 0.3, 0.05);
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.7f);
        
        player.sendMessage("§5§l⚡ §dGravity Shattered!");
    }
    
    // VOID REAPER ACTIVE - Explosive Chickens
    private void handleVoidReaperActive(Player player) {
        Location playerLoc = player.getLocation();
        int chickenCount = plugin.getConfigManager().getChickenCount();
        
        player.sendMessage("§c§l💥 §4CATACLYSM RAIN UNLEASHED!");
        player.playSound(playerLoc, Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 0.5f);
        
        new BukkitRunnable() {
            int spawned = 0;
            
            @Override
            public void run() {
                if (spawned >= chickenCount) {
                    cancel();
                    return;
                }
                
                // Random position around player
                double angle = Math.random() * 2 * Math.PI;
                double radius = 3 + Math.random() * 5;
                double x = playerLoc.getX() + radius * Math.cos(angle);
                double z = playerLoc.getZ() + radius * Math.sin(angle);
                Location spawnLoc = new Location(playerLoc.getWorld(), x, playerLoc.getY() + 15, z);
                
                // Spawn chicken with TNT
                Chicken chicken = playerLoc.getWorld().spawn(spawnLoc, Chicken.class, c -> {
                    c.setBaby();
                    c.setGlowing(true);
                    c.setInvulnerable(false);
                });
                
                // Spawn TNT on chicken
                TNTPrimed tnt = playerLoc.getWorld().spawn(spawnLoc.add(0, 1, 0), TNTPrimed.class, t -> {
                    t.setFuseTicks(60); // 3 seconds
                    t.setYield(3.0f);
                    t.setIsIncendiary(true);
                });
                
                // Make TNT ride chicken
                chicken.addPassenger(tnt);
                
                // Particles
                spawnLoc.getWorld().spawnParticle(Particle.EXPLOSION, spawnLoc, 5, 0.5, 0.5, 0.5, 0);
                spawnLoc.getWorld().playSound(spawnLoc, Sound.ENTITY_CHICKEN_AMBIENT, 1.0f, 0.5f);
                
                spawned++;
            }
        }.runTaskTimer(plugin, 0L, 10L); // Spawn every 0.5 seconds
    }
    
    // ETERNAL NIGHTMARE PASSIVE - Lifesteal
    private void handleEternalNightmarePassive(Player player, LivingEntity victim, double damage) {
        double lifestealPercent = plugin.getConfigManager().getLifestealPercent() / 100.0;
        double healAmount = damage * lifestealPercent;
        double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        double newHealth = Math.min(player.getHealth() + healAmount, maxHealth);
        player.setHealth(newHealth);
        
        // Apply wither
        victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
        
        // Effects
        player.spawnParticle(Particle.HEART, player.getLocation().add(0, 2, 0), 5, 0.5, 0.5, 0.5, 0);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 0.5f, 1.5f);
        
        victim.getWorld().spawnParticle(Particle.CRIMSON_SPORE, 
            victim.getLocation().add(0, 1, 0), 25, 0.5, 0.5, 0.5, 0.1);
        victim.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, 
            victim.getLocation().add(0, 1, 0), 15, 0.3, 0.5, 0.3, 0);
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PHANTOM_HURT, 1.0f, 0.5f);
        
        player.sendMessage("§4§l❤ §cDrained §e" + String.format("%.1f", healAmount) + " §cHP");
    }
    
    // CELESTIAL ANNIHILATOR PASSIVE - Slow
    private void handleCelestialAnnihilatorPassive(LivingEntity victim) {
        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 3));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80, 2));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 80, 2));
        
        victim.getWorld().spawnParticle(Particle.END_ROD, 
            victim.getLocation().add(0, 1, 0), 25, 0.3, 0.5, 0.3, 0.05);
        victim.getWorld().spawnParticle(Particle.GLOW, 
            victim.getLocation().add(0, 1, 0), 15, 0.3, 0.5, 0.3, 0);
        victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_BELL_USE, 1.0f, 1.5f);
    }
    
    // CELESTIAL ANNIHILATOR ACTIVE - Divine Storm
    private void handleCelestialAnnihilatorActive(Player player) {
        Location loc = player.getLocation();
        
        player.sendMessage("§e§l⚡ §bDIVINE STORM ACTIVATED!");
        player.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2.0f, 1.2f);
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks >= 40) {
                    cancel();
                    return;
                }
                
                double radius = 1 + (ticks * 0.15);
                
                // Circular explosion effect
                for (int i = 0; i < 360; i += 5) {
                    double angle = Math.toRadians(i);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    Location particleLoc = loc.clone().add(x, 0.5, z);
                    particleLoc.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 3, 0.1, 0.1, 0.1, 0);
                    particleLoc.getWorld().spawnParticle(Particle.GLOW, particleLoc, 2, 0.1, 0.1, 0.1, 0);
                    
                    if (ticks % 5 == 0) {
                        particleLoc.getWorld().spawnParticle(Particle.FIREWORK, particleLoc, 1, 0, 0, 0, 0.1);
                    }
                }
                
                // Lightning effects
                if (ticks % 10 == 0) {
                    loc.getWorld().strikeLightningEffect(loc.clone().add(
                        (Math.random() - 0.5) * radius * 2,
                        0,
                        (Math.random() - 0.5) * radius * 2
                    ));
                }
                
                // Damage entities
                Collection<Entity> nearbyEntities = loc.getWorld().getNearbyEntities(loc, radius, 5, radius);
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity && entity != player) {
                        LivingEntity living = (LivingEntity) entity;
                        living.damage(8.0, player);
                        living.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 4));
                        living.setFreezeTicks(140);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}
