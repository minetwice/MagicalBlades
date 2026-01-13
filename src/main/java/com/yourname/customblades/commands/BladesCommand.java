package com.yourname.customblades.commands;

import com.yourname.customblades.CustomBlades;
import com.yourname.customblades.enums.BladeType;
import com.yourname.customblades.menus.RecipeListMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BladesCommand implements CommandExecutor, TabCompleter {
    
    private final CustomBlades plugin;
    
    public BladesCommand(CustomBlades plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customblades.command")) {
            sender.sendMessage("§c§l⚠ §cYou don't have permission to use this command!");
            return true;
        }
        
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "give":
                if (args.length < 2) {
                    sender.sendMessage("§c§lUsage: §e/blades give <blade_type> [player]");
                    return true;
                }
                handleGive(sender, args);
                break;
                
            case "giveall":
                handleGiveAll(sender, args);
                break;
                
            case "recipe":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cOnly players can use this command!");
                    return true;
                }
                RecipeListMenu.open((Player) sender);
                break;
                
            case "info":
                if (args.length < 2) {
                    sendAllBladesInfo(sender);
                } else {
                    sendBladeInfo(sender, args[1]);
                }
                break;
                
            default:
                sendHelpMessage(sender);
                break;
        }
        
        return true;
    }
    
    private void handleGive(CommandSender sender, String[] args) {
        BladeType bladeType = BladeType.fromId(args[1].toLowerCase());
        
        if (bladeType == null) {
            sender.sendMessage("§c§lInvalid blade type! §7Available: void_reaper, eternal_nightmare, celestial_annihilator");
            return;
        }
        
        Player target;
        if (args.length >= 3) {
            target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                sender.sendMessage("§c§lPlayer not found!");
                return;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cYou must specify a player!");
                return;
            }
            target = (Player) sender;
        }
        
        ItemStack blade = plugin.getBladeManager().createBlade(bladeType);
        target.getInventory().addItem(blade);
        
        target.sendMessage("§a§l✓ §2You received " + bladeType.getDisplayName() + "§2!");
        if (sender != target) {
            sender.sendMessage("§a§l✓ §2Given " + bladeType.getDisplayName() + " §2to §e" + target.getName());
        }
    }
    
    private void handleGiveAll(CommandSender sender, String[] args) {
        Player target;
        if (args.length >= 2) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§c§lPlayer not found!");
                return;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cYou must specify a player!");
                return;
            }
            target = (Player) sender;
        }
        
        for (BladeType type : BladeType.values()) {
            ItemStack blade = plugin.getBladeManager().createBlade(type);
            target.getInventory().addItem(blade);
        }
        
        target.sendMessage("§a§l✓ §2You received all legendary blades!");
        if (sender != target) {
            sender.sendMessage("§a§l✓ §2Given all blades to §e" + target.getName());
        }
    }
    
    private void sendBladeInfo(CommandSender sender, String bladeName) {
        BladeType bladeType = BladeType.fromId(bladeName.toLowerCase());
        
        if (bladeType == null) {
            sender.sendMessage("§c§lInvalid blade type!");
            return;
        }
        
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage(bladeType.getDisplayName());
        sender.sendMessage("");
        sender.sendMessage(bladeType.getLore());
        sender.sendMessage("");
        for (String ability : bladeType.getAbilities()) {
            sender.sendMessage(ability);
        }
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    private void sendAllBladesInfo(CommandSender sender) {
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("§6§lLEGENDARY BLADES");
        sender.sendMessage("");
        
        for (BladeType type : BladeType.values()) {
            sender.sendMessage(type.getDisplayName());
            sender.sendMessage("§7ID: §f" + type.getId());
            sender.sendMessage("");
        }
        
        sender.sendMessage("§7Use §e/blades info <blade_id> §7for details");
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("§6§lCustomBlades §7- Commands");
        sender.sendMessage("");
        sender.sendMessage("§e/blades give <type> [player] §7- Get a blade");
        sender.sendMessage("§e/blades giveall [player] §7- Get all blades");
        sender.sendMessage("§e/blades recipe §7- Open recipe menu");
        sender.sendMessage("§e/blades info [type] §7- View blade information");
        sender.sendMessage("");
        sender.sendMessage("§7Blade Types:");
        sender.sendMessage("§8 • §5void_reaper");
        sender.sendMessage("§8 • §4eternal_nightmare");
        sender.sendMessage("§8 • §bcelestial_annihilator");
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("give", "giveall", "recipe", "info"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("info")) {
                completions.addAll(Arrays.asList("void_reaper", "eternal_nightmare", "celestial_annihilator"));
            } else if (args[0].equalsIgnoreCase("giveall")) {
                return null; // Return player names
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return null; // Return player names
        }
        
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}
