package com.yourname.customblades.managers;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import com.yourname.customblades.objects.Ritual;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class RitualManager {
    
    private final CustomBlades plugin;
    private final Map<Location, Ritual> activeRituals;
    private int defaultRitualTime;
    
    public RitualManager(CustomBlades plugin) {
        this.plugin = plugin;
        this.activeRituals = new HashMap<>();
        this.defaultRitualTime = plugin.getConfig().getInt("ritual.default-time", 600); // 10 minutes
    }
    
    public void startRitual(Player player, BladeType bladeType, Location center) {
        if (isRitualActive(center)) {
            player.sendMessage("§c§l⚠ §cA ritual is already active at this location!");
            return;
        }
        
        Ritual ritual = new Ritual(plugin, player, bladeType, center, defaultRitualTime);
        activeRituals.put(center, ritual);
        ritual.start();
    }
    
    public boolean isRitualActive(Location location) {
        return activeRituals.containsKey(location);
    }
    
    public Ritual getRitual(Location location) {
        return activeRituals.get(location);
    }
    
    public void removeRitual(Location location) {
        activeRituals.remove(location);
    }
    
    public void cancelAllRituals() {
        for (Ritual ritual : new ArrayList<>(activeRituals.values())) {
            ritual.cancel();
        }
        activeRituals.clear();
    }
    
    public Collection<Ritual> getActiveRituals() {
        return activeRituals.values();
    }
    
    public void setDefaultRitualTime(int seconds) {
        this.defaultRitualTime = seconds;
        plugin.getConfig().set("ritual.default-time", seconds);
        plugin.saveConfig();
    }
    
    public int getDefaultRitualTime() {
        return defaultRitualTime;
    }
}
