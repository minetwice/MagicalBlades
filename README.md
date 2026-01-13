# MagicalBlades

⚔️ CustomBlades - Advanced Minecraft Plugin
�
�
�
Load image
Load image
Load image
An advanced Minecraft plugin featuring three legendary blades with unique abilities and an epic ritual crafting system.
🗡️ The Three Legendary Blades
🌑 Void Reaper
The blade that defies gravity itself
Passive: Gravity Shatter - Launch enemies 4 blocks into the air with each strike
Active: Cataclysm Rain - Summon 5 explosive TNT chickens from the sky (15s cooldown)
Perfect for crowd control and aerial dominance
💀 Eternal Nightmare
Forged in the depths of eternal darkness
Passive: Blood Drain - Steal 40% of damage dealt as health + apply withering curse
No Active Ability - Pure destructive passive power
Ultimate lifesteal weapon for sustained combat
✨ Celestial Annihilator
Blessed by the stars, feared by mortals
Passive: Starfall Curse - Slow enemies and drain their strength
Active: Divine Storm - Create a devastating area explosion with lightning (20s cooldown)
Divine judgment for area control
✨ Features
🔮 Epic Ritual Crafting System
6x6 Ritual Platform made of Reinforced Deepslate
Floating Blade Display that rotates above the platform
Live Progress Bar with countdown timer
Spectacular Particle Effects unique to each blade
Customizable Duration (default: 10 minutes, adjustable by admins)
🎨 Blade Holder Particle Effects
Each blade emits unique particles around the player's body
Different particle types for each blade
Trail effects when moving
Automatically activates when holding a blade
⚙️ In-Game Recipe Editor
Open with /blades recipe
Visual GUI menu for all blades
Click to view/edit recipes
Save/Cancel options
Persist across server restarts
🎯 Five Additional Features
Tab Completion - Smart auto-complete for all commands
Permission System - Fine-grained control over who can craft/use blades
Config Customization - Adjust cooldowns, damage, particle density
Unbreakable Blades - Legendary weapons never break
Sound Effects - Epic audio feedback for all abilities and rituals
📋 Commands
Player Commands
/blades give <type> [player]    - Get a specific blade
/blades giveall [player]        - Get all legendary blades
/blades recipe                  - Open recipe customization menu
/blades info [type]             - View blade information
Admin Commands
/ritual time <seconds>          - Set ritual duration
/ritual info                    - View ritual information
Blade Types
void_reaper - Void Reaper
eternal_nightmare - Eternal Nightmare
celestial_annihilator - Celestial Annihilator
🔑 Permissions
Permission
Description
Default
customblades.command
Use blade commands
op
customblades.admin
Admin commands
op
customblades.craft
Craft blades
true
customblades.use
Use blade abilities
true
🛠️ Installation
Download the latest .jar from Releases
Place in your server's plugins folder
Restart your server
Enjoy the legendary blades!
🔨 Building from Source
Prerequisites
JDK 21 or higher
Maven 3.6+
Git
Build Steps
# Clone repository
git clone https://github.com/yourusername/CustomBlades.git
cd CustomBlades

particles:
  enabled: true
  density: 1.0
recipes.yml
Automatically generated and editable in-game via /blades recipe
🎮 Usage Examples
Crafting a Blade
Gather required materials
Place items in crafting table according to recipe
Ritual automatically starts at your location
Wait for ritual completion (default: 10 minutes)
Blade drops with epic effects!
Using Abilities
Passive: Simply hit enemies with the blade
Active: Right-click while holding the blade
Watch for cooldown messages
Customizing Recipes
Run /blades recipe
Click on blade to edit
View current recipe and abilities
Modify in recipes.yml or request admin changes
Recipes persist across restarts
🐛 Support
For issues and feature requests:
Open an issue on GitHub Issues
Join our Discord Server (if available)
📜 License
This project is licensed under the MIT License - see the LICENSE file for details.
🙏 Credits
Developer: YourName
Minecraft Version: 1.21 - 1.21.10
Server Platform: Paper
Made with ❤️ for the Minecraft community
Note: This plugin requires Java 21 and Paper server 1.21-1.21.10
