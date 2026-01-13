package com.yourname.customblades.menus;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import com.yourname.customblades.managers.RecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeEditorMenu {
    
    public static void open(Player player, BladeType bladeType) {
        Inventory inv = Bukkit.createInventory(null, 54, "§6Recipe Editor: " + bladeType.getDisplayName());
        
        RecipeManager recipeManager = CustomBlades.getInstance().getRecipeManager();
        List<String> shape = recipeManager.getRecipeShape(bladeType);
        Map<String, String> ingredients = recipeManager.getRecipeIngredients(bladeType);
        
        // Display current recipe (slots 10-12, 19-21, 28-30)
        int[] recipeSlots = {10, 11, 12, 19, 20, 21, 28, 29, 30};
        
        for (int row = 0; row < 3; row++) {
            String rowPattern = shape.get(row);
            for (int col = 0; col < 3; col++) {
                char c = rowPattern.charAt(col);
                if (c != ' ') {
                    String materialName = ingredients.get(String.valueOf(c));
                    if (materialName != null) {
                        Material material = Material.valueOf(materialName);
                        ItemStack item = new ItemStack(material);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName("§e" + material.name());
                        List<String> lore = new ArrayList<>();
                        lore.add("§7Key: §f" + c);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        inv.setItem(recipeSlots[row * 3 + col], item);
                    }
                }
            }
        }
        
        // Result (slot 24)
        ItemStack result = CustomBlades.getInstance().getBladeManager().createBlade(bladeType);
        ItemMeta resultMeta = result.getItemMeta();
        List<String> resultLore = new ArrayList<>(resultMeta.getLore());
        resultLore.add(0, "§a§lRESULT");
        resultLore.add(1, "");
        resultMeta.setLore(resultLore);
        result.setItemMeta(resultMeta);
        inv.setItem(24, result);
        
        // Abilities info (slot 16)
        ItemStack abilitiesInfo = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta abilitiesMeta = abilitiesInfo.getItemMeta();
        abilitiesMeta.setDisplayName("§d§lAbilities");
        List<String> abilitiesLore = new ArrayList<>();
        abilitiesLore.add("");
        for (String ability : bladeType.getAbilities()) {
            abilitiesLore.add(ability);
        }
        abilitiesMeta.setLore(abilitiesLore);
        abilitiesInfo.setItemMeta(abilitiesMeta);
        inv.setItem(16, abilitiesInfo);
        
        // Save button (slot 48)
        ItemStack saveButton = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta saveMeta = saveButton.getItemMeta();
        saveMeta.setDisplayName("§a§lSAVE");
        List<String> saveLore = new ArrayList<>();
        saveLore.add("§7Click to save changes");
        saveMeta.setLore(saveLore);
        saveButton.setItemMeta(saveMeta);
        inv.setItem(48, saveButton);
        
        // Cancel button (slot 50)
        ItemStack cancelButton = new ItemStack(Material.RED_CONCRETE);
        ItemMeta cancelMeta = cancelButton.getItemMeta();
        cancelMeta.setDisplayName("§c§lCANCEL");
        List<String> cancelLore = new ArrayList<>();
        cancelLore.add("§7Click to cancel and go back");
        cancelMeta.setLore(cancelLore);
        cancelButton.setItemMeta(cancelMeta);
        inv.setItem(50, cancelButton);
        
        // Decoration
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        
        for (int i = 0; i < 54; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }
        
        player.openInventory(inv);
    }
}
