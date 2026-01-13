package com.yourname.customblades.menus;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RecipeListMenu {
    
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§6§lBlade Recipes");
        
        int slot = 10;
        for (BladeType type : BladeType.values()) {
            ItemStack blade = CustomBlades.getInstance().getBladeManager().createBlade(type);
            ItemMeta meta = blade.getItemMeta();
            
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7Click to view/edit recipe");
            lore.add("");
            lore.addAll(meta.getLore());
            
            meta.setLore(lore);
            blade.setItemMeta(meta);
            
            inv.setItem(slot, blade);
            slot += 2;
        }
        
        // Add decoration
        ItemStack glass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        
        for (int i = 0; i < 27; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }
        
        player.openInventory(inv);
    }
}
