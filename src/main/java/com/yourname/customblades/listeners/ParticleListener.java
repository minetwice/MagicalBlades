package com.yourname.customblades.listeners;

import com.yourname.customblades.CustomBlades;
import org.bukkit.event.Listener;

public class ParticleListener implements Listener {
    
    private final CustomBlades plugin;
    
    public ParticleListener(CustomBlades plugin) {
        this.plugin = plugin;
    }
    
    // Particle effects are handled by ParticleManager
    // This listener can be extended for future particle-related events
}
