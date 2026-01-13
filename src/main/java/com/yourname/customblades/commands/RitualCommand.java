package com.yourname.customblades.commands;

import com.yourname.customblades.CustomBlades;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RitualCommand implements CommandExecutor, TabCompleter {
    
    private final CustomBlades plugin;
    
    public RitualCommand(CustomBlades plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customblades.admin")) {
            sender.sendMessage("§c§l⚠ §cYou don't have permission to use this command!");
            return true;
        }
        
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "time":
                if (args.length < 2) {
                    sender.sendMessage("§c§lUsage: §e/ritual time <seconds>");
                    return true;
                }
                handleTimeChange(sender, args[1]);
                break;
                
            case "info":
                handleInfo(sender);
                break;
                
            default:
                sendHelpMessage(sender);
                break;
        }
        
        return true;
    }
    
    private void handleTimeChange(CommandSender sender, String timeStr) {
        try {
            int seconds = Integer.parseInt(timeStr);
            
            if (seconds < 10) {
                sender.sendMessage("§c§lMinimum ritual time is 10 seconds!");
                return;
            }
            
            if (seconds > 3600) {
                sender.sendMessage("§c§lMaximum ritual time is 3600 seconds (1 hour)!");
                return;
            }
            
            plugin.getRitualManager().setDefaultRitualTime(seconds);
            
            int minutes = seconds / 60;
            int secs = seconds % 60;
            String timeFormat = String.format("%02d:%02d", minutes, secs);
            
            sender.sendMessage("§a§l✓ §2Ritual time set to §e" + timeFormat + " §2(" + seconds + " seconds)");
        } catch (NumberFormatException e) {
            sender.sendMessage("§c§lInvalid number!");
        }
    }
    
    private void handleInfo(CommandSender sender) {
        int time = plugin.getRitualManager().getDefaultRitualTime();
        int activeRituals = plugin.getRitualManager().getActiveRituals().size();
        
        int minutes = time / 60;
        int secs = time % 60;
        String timeFormat = String.format("%02d:%02d", minutes, secs);
        
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("§d§lRitual Information");
        sender.sendMessage("");
        sender.sendMessage("§7Default Time: §e" + timeFormat);
        sender.sendMessage("§7Active Rituals: §e" + activeRituals);
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("§d§lRitual Commands");
        sender.sendMessage("");
        sender.sendMessage("§e/ritual time <seconds> §7- Set ritual duration");
        sender.sendMessage("§e/ritual info §7- View ritual information");
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("time", "info"));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("time")) {
            completions.addAll(Arrays.asList("60", "300", "600", "900"));
        }
        
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}
