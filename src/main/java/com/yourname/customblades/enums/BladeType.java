package com.yourname.customblades.enums;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;

public enum BladeType {
    VOID_REAPER(
        "§5§lVOID REAPER",
        "void_reaper",
        "§8The blade that defies gravity itself",
        new String[]{
            "§7Passive: §d§lGRAVITY SHATTER",
            "§8Launch enemies 4 blocks into the air",
            "§8with each devastating strike",
            "",
            "§7Active: §c§lCATACLYSM RAIN",
            "§8Summon explosive chickens from the sky",
            "§8Right-click to unleash chaos",
            "§8Cooldown: 15 seconds"
        },
        Particle.REVERSE_PORTAL,
        Particle.WITCH,
        Color.fromRGB(138, 43, 226)
    ),
    
    ETERNAL_NIGHTMARE(
        "§4§lETERNAL NIGHTMARE",
        "eternal_nightmare",
        "§8Forged in the depths of eternal darkness",
        new String[]{
            "§7Passive: §c§lBLOOD DRAIN",
            "§8Steal 40% of damage as health",
            "§8Apply withering curse to victims",
            "",
            "§7No Active Ability",
            "§8Pure destructive passive power"
        },
        Particle.CRIMSON_SPORE,
        Particle.DAMAGE_INDICATOR,
        Color.fromRGB(139, 0, 0)
    ),
    
    CELESTIAL_ANNIHILATOR(
        "§b§lCELESTIAL ANNIHILATOR",
        "celestial_annihilator",
        "§8Blessed by the stars, feared by mortals",
        new String[]{
            "§7Passive: §b§lSTARFALL CURSE",
            "§8Slow enemies and drain their strength",
            "§8Apply celestial weakness",
            "",
            "§7Active: §e§lDIVINE STORM",
            "§8Create a devastating area explosion",
            "§8Right-click to call divine judgment",
            "§8Cooldown: 20 seconds"
        },
        Particle.END_ROD,
        Particle.GLOW,
        Color.fromRGB(135, 206, 250)
    );
    
    private final String displayName;
    private final String id;
    private final String lore;
    private final String[] abilities;
    private final Particle primaryParticle;
    private final Particle secondaryParticle;
    private final Color particleColor;
    
    BladeType(String displayName, String id, String lore, String[] abilities, 
              Particle primaryParticle, Particle secondaryParticle, Color particleColor) {
        this.displayName = displayName;
        this.id = id;
        this.lore = lore;
        this.abilities = abilities;
        this.primaryParticle = primaryParticle;
        this.secondaryParticle = secondaryParticle;
        this.particleColor = particleColor;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getId() {
        return id;
    }
    
    public String getLore() {
        return lore;
    }
    
    public String[] getAbilities() {
        return abilities;
    }
    
    public Particle getPrimaryParticle() {
        return primaryParticle;
    }
    
    public Particle getSecondaryParticle() {
        return secondaryParticle;
    }
    
    public Color getParticleColor() {
        return particleColor;
    }
    
    public static BladeType fromId(String id) {
        for (BladeType type : values()) {
            if (type.getId().equalsIgnoreCase(id)) {
                return type;
            }
        }
        return null;
    }
    
    public Material getRitualBlock() {
        return Material.REINFORCED_DEEPSLATE;
    }
}
