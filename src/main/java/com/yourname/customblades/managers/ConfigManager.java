package com.yourname.customblades.managers;

import com.yourname.customblades.CustomBlades;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    
    private final CustomBlades plugin;
    private final FileConfiguration config;
    
    public ConfigManager(CustomBlades plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        setupDefaultConfig();
    }
    
    private void setupDefaultConfig() {
        config.addDefault("ritual.default-time", 600); // 10 minutes
        config.addDefault("ritual.allow-time-change", true);
        config.addDefault("abilities.void-reaper.launch-height", 4.0);
        config.addDefault("abilities.void-reaper.chicken-count", 5);
        config.addDefault("abilities.void-reaper.cooldown", 15);
        config.addDefault("abilities.eternal-nightmare.lifesteal-percent", 40.0);
        config.addDefault("abilities.celestial-annihilator.cooldown", 20);
        config.addDefault("particles.enabled", true);
        config.addDefault("particles.density", 1.0);
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    public int getRitualTime() {
        return config.getInt("ritual.default-time", 600);
    }
    
    public boolean canChangeRitualTime() {
        return config.getBoolean("ritual.allow-time-change", true);
    }
    
    public double getLaunchHeight() {
        return config.getDouble("abilities.void-reaper.launch-height", 4.0);
    }
    
    public int getChickenCount() {
        return config.getInt("abilities.void-reaper.chicken-count", 5);
    }
    
    public int getVoidReaperCooldown() {
        return config.getInt("abilities.void-reaper.cooldown", 15);
    }
    
    public double getLifestealPercent() {
        return config.getDouble("abilities.eternal-nightmare.lifesteal-percent", 40.0);
    }
    
    public int getCelestialCooldown() {
        return config.getInt("abilities.celestial-annihilator.cooldown", 20);
    }
    
    public boolean areParticlesEnabled() {
        return config.getBoolean("particles.enabled", true);
    }
}
