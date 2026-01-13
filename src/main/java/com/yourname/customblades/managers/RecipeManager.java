package com.yourname.customblades.managers;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RecipeManager {
    
    private final CustomBlades plugin;
    private final File recipesFile;
    private FileConfiguration recipesConfig;
    private final Map<BladeType, ShapedRecipe> recipes;
    
    public RecipeManager(CustomBlades plugin) {
        this.plugin = plugin;
        this.recipes = new HashMap<>();
        this.recipesFile = new File(plugin.getDataFolder(), "recipes.yml");
        loadRecipesConfig();
    }
    
    private void loadRecipesConfig() {
        if (!recipesFile.exists()) {
            plugin.saveResource("recipes.yml", false);
        }
        recipesConfig = YamlConfiguration.loadConfiguration(recipesFile);
    }
    
    public void saveRecipesConfig() {
        try {
            recipesConfig.save(recipesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save recipes.yml: " + e.getMessage());
        }
    }
    
    public void registerAllRecipes() {
        for (BladeType type : BladeType.values()) {
            registerRecipe(type);
        }
    }
    
    private void registerRecipe(BladeType type) {
        String path = "blades." + type.getId();
        
        // Load recipe from config or use default
        if (!recipesConfig.contains(path)) {
            setDefaultRecipe(type);
        }
        
        List<String> shape = recipesConfig.getStringList(path + ".shape");
        Map<String, String> ingredients = new HashMap<>();
        
        for (String key : recipesConfig.getConfigurationSection(path + ".ingredients").getKeys(false)) {
            ingredients.put(key, recipesConfig.getString(path + ".ingredients." + key));
        }
        
        createRecipe(type, shape, ingredients);
    }
    
    private void setDefaultRecipe(BladeType type) {
        String path = "blades." + type.getId();
        
        switch (type) {
            case VOID_REAPER:
                recipesConfig.set(path + ".shape", Arrays.asList("EEE", "ESE", " N "));
                recipesConfig.set(path + ".ingredients.E", "ENDER_EYE");
                recipesConfig.set(path + ".ingredients.S", "NETHERITE_SWORD");
                recipesConfig.set(path + ".ingredients.N", "NETHER_STAR");
                break;
                
            case ETERNAL_NIGHTMARE:
                recipesConfig.set(path + ".shape", Arrays.asList("DDD", "WSW", " O "));
                recipesConfig.set(path + ".ingredients.D", "DIAMOND_BLOCK");
                recipesConfig.set(path + ".ingredients.W", "WITHER_SKELETON_SKULL");
                recipesConfig.set(path + ".ingredients.S", "NETHERITE_SWORD");
                recipesConfig.set(path + ".ingredients.O", "OBSIDIAN");
                break;
                
            case CELESTIAL_ANNIHILATOR:
                recipesConfig.set(path + ".shape", Arrays.asList("BBB", "CSC", " E "));
                recipesConfig.set(path + ".ingredients.B", "BEACON");
                recipesConfig.set(path + ".ingredients.C", "END_CRYSTAL");
                recipesConfig.set(path + ".ingredients.S", "NETHERITE_SWORD");
                recipesConfig.set(path + ".ingredients.E", "ELYTRA");
                break;
        }
        
        saveRecipesConfig();
    }
    
    private void createRecipe(BladeType type, List<String> shape, Map<String, String> ingredients) {
        NamespacedKey key = new NamespacedKey(plugin, "blade_" + type.getId());
        
        // Remove existing recipe if present
        Bukkit.removeRecipe(key);
        
        ItemStack result = plugin.getBladeManager().createBlade(type);
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        
        recipe.shape(shape.get(0), shape.get(1), shape.get(2));
        
        for (Map.Entry<String, String> entry : ingredients.entrySet()) {
            Material material = Material.valueOf(entry.getValue());
            recipe.setIngredient(entry.getKey().charAt(0), material);
        }
        
        Bukkit.addRecipe(recipe);
        recipes.put(type, recipe);
    }
    
    public void updateRecipe(BladeType type, List<String> shape, Map<String, String> ingredients) {
        String path = "blades." + type.getId();
        
        recipesConfig.set(path + ".shape", shape);
        recipesConfig.set(path + ".ingredients", null); // Clear old ingredients
        
        for (Map.Entry<String, String> entry : ingredients.entrySet()) {
            recipesConfig.set(path + ".ingredients." + entry.getKey(), entry.getValue());
        }
        
        saveRecipesConfig();
        createRecipe(type, shape, ingredients);
    }
    
    public List<String> getRecipeShape(BladeType type) {
        String path = "blades." + type.getId() + ".shape";
        return recipesConfig.getStringList(path);
    }
    
    public Map<String, String> getRecipeIngredients(BladeType type) {
        String path = "blades." + type.getId() + ".ingredients";
        Map<String, String> ingredients = new HashMap<>();
        
        if (recipesConfig.contains(path)) {
            for (String key : recipesConfig.getConfigurationSection(path).getKeys(false)) {
                ingredients.put(key, recipesConfig.getString(path + "." + key));
            }
        }
        
        return ingredients;
    }
    
    public ShapedRecipe getRecipe(BladeType type) {
        return recipes.get(type);
    }
}
