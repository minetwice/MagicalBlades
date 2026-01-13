package com.yourname.customblades.managers;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class BladeManager {
    
    private final CustomBlades plugin;
    private final Map<String, Long> cooldowns;
    private final NamespacedKey bladeKey;
    
    public BladeManager(CustomBlades plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
        this.bladeKey = new NamespacedKey(plugin, "blade_type");
    }
    
    public ItemStack createBlade(BladeType type) {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(type.getDisplayName());
            
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(type.getLore());
            lore.add("");
            lore.add("§8§m━━━━━━━━━━━━━━━━━━━━━━━━");
            lore.add("");
            
            // Add abilities
            for (String ability : type.getAbilities()) {
                lore.add(ability);
            }
            
            lore.add("");
            lore.add("§8§m━━━━━━━━━━━━━━━━━━━━━━━━");
            lore.add("");
            lore.add("§6§l⚔ §eLegendary Weapon");
            lore.add("§7Crafted through ancient ritual");
            lore.add("");
            
            meta.setLore(lore);
            
            // Add enchantments
            meta.addEnchant(Enchantment.SHARPNESS, 7, true);
            meta.addEnchant(Enchantment.UNBREAKING, 5, true);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
            
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            
            // Make unbreakable
            meta.setUnbreakable(true);
            
            // Store blade type in NBT
            meta.getPersistentDataContainer().set(bladeKey, PersistentDataType.STRING, type.getId());
            
            blade.setItemMeta(meta);
        }
        
        return blade;
    }
    
    public BladeType getBladeType(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        
        ItemMeta meta = item.getItemMeta();
        String typeId = meta.getPersistentDataContainer().get(bladeKey, PersistentDataType.STRING);
        
        return typeId != null ? BladeType.fromId(typeId) : null;
    }
    
    public boolean isOnCooldown(UUID playerId, BladeType type) {
        String key = playerId.toString() + "_" + type.getId();
        if (!cooldowns.containsKey(key)) return false;
        
        long currentTime = System.currentTimeMillis();
        long cooldownEnd = cooldowns.get(key);
        
        if (currentTime >= cooldownEnd) {
            cooldowns.remove(key);
            return false;
        }
        
        return true;
    }
    
    public void setCooldown(UUID playerId, BladeType type, int seconds) {
        String key = playerId.toString() + "_" + type.getId();
        cooldowns.put(key, System.currentTimeMillis() + (seconds * 1000L));
    }
    
    public int getRemainingCooldown(UUID playerId, BladeType type) {
        String key = playerId.toString() + "_" + type.getId();
        if (!cooldowns.containsKey(key)) return 0;
        
        long remaining = cooldowns.get(key) - System.currentTimeMillis();
        return remaining > 0 ? (int) (remaining / 1000) : 0;
    }
    
    public List<BladeType> getAllBladeTypes() {
        return Arrays.asList(BladeType.values());
    }
}
