package com.yourname.customblades;

import com.yourname.customblades.commands.BladesCommand;
import com.yourname.customblades.commands.RitualCommand;
import com.yourname.customblades.listeners.*;
import com.yourname.customblades.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomBlades extends JavaPlugin {
    
    private static CustomBlades instance;
    private BladeManager bladeManager;
    private RitualManager ritualManager;
    private RecipeManager recipeManager;
    private ParticleManager particleManager;
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize managers
        configManager = new ConfigManager(this);
        bladeManager = new BladeManager(this);
        ritualManager = new RitualManager(this);
        recipeManager = new RecipeManager(this);
        particleManager = new ParticleManager(this);
        
        // Register recipes
        recipeManager.registerAllRecipes();
        
        // Register commands
        getCommand("blades").setExecutor(new BladesCommand(this));
        getCommand("ritual").setExecutor(new RitualCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new BladeListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
        getServer().getPluginManager().registerEvents(new RecipeMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new ParticleListener(this), this);
        
        // Start particle task
        particleManager.startParticleTask();
        
        getLogger().info("╔════════════════════════════════╗");
        getLogger().info("║  CustomBlades v2.0.0 Enabled  ║");
        getLogger().info("║  Advanced Ritual System Ready  ║");
        getLogger().info("╚════════════════════════════════╝");
    }
    
    @Override
    public void onDisable() {
        // Cancel all active rituals
        if (ritualManager != null) {
            ritualManager.cancelAllRituals();
        }
        
        // Stop particle task
        if (particleManager != null) {
            particleManager.stopParticleTask();
        }
        
        getLogger().info("CustomBlades plugin has been disabled!");
    }
    
    public static CustomBlades getInstance() {
        return instance;
    }
    
    public BladeManager getBladeManager() {
        return bladeManager;
    }
    
    public RitualManager getRitualManager() {
        return ritualManager;
    }
    
    public RecipeManager getRecipeManager() {
        return recipeManager;
    }
    
    public ParticleManager getParticleManager() {
        return particleManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
