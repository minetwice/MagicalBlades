package com.yourname.customblades.listeners;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import com.yourname.customblades.menus.RecipeEditorMenu;
import com.yourname.customblades.menus.RecipeListMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class RecipeMenuListener implements Listener {
    
    private final CustomBlades plugin;
    
    public RecipeMenuListener(CustomBlades plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        
        // Check if it's a recipe menu
        if (event.getView().getTitle().contains("Blade Recipes") || 
            event.getView().getTitle().contains("Recipe Editor")) {
            event.setCancelled(true);
            
            if (event.getCurrentItem() == null) return;
            
            // Handle recipe list menu
            if (event.getView().getTitle().contains("Blade Recipes")) {
                handleRecipeListClick(player, event);
            }
            // Handle recipe editor menu
            else if (event.getView().getTitle().contains("Recipe Editor")) {
                handleRecipeEditorClick(player, event);
            }
        }
    }
    
    private void handleRecipeListClick(Player player, InventoryClickEvent event) {
        String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
        
        for (BladeType type : BladeType.values()) {
            if (itemName.contains(type.getDisplayName())) {
                RecipeEditorMenu.open(player, type);
                return;
            }
        }
    }
    
    private void handleRecipeEditorClick(Player player, InventoryClickEvent event) {
        // Recipe editor logic handled in RecipeEditorMenu class
    }
}
