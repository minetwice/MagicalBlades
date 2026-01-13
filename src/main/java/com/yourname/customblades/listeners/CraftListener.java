package com.yourname.customblades.listeners;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {
    
    private final CustomBlades plugin;
    
    public CraftListener(CustomBlades plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        BladeType bladeType = plugin.getBladeManager().getBladeType(result);
        
        if (bladeType == null) return;
        
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        if (!player.hasPermission("customblades.craft")) {
            event.setCancelled(true);
            player.sendMessage("§c§l⚠ §cYou don't have permission to craft blades!");
            return;
        }
        
        // Cancel normal crafting
        event.setCancelled(true);
        
        // Start ritual instead
        Location ritualCenter = player.getLocation().getBlock().getLocation();
        plugin.getRitualManager().startRitual(player, bladeType, ritualCenter);
        
        player.sendMessage("§d§l✦ §5Ritual initiated for " + bladeType.getDisplayName() + "§5!");
        player.sendMessage("§7The ancient powers are gathering...");
    }
}
