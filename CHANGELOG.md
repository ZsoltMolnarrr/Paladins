# 2.7.1

- Added cooldown for Holy Shock spell (to be counteracted by Skill Tree)
- Update Barrier skill to use new underlying API to provide immunity

# 2.7.0

DISCLAIMER: All spell books and spell scrolls will be reset, due to major API changes. Some (looted) weapons with custom spell containers become non-functional, and need to be re-obtained. Apologies for the inconvenience.

- Update to use Spell Engine 1.9.0
- Spell books now offer 3 spells only, to match other classes
- Holy Shock spell is now attached to Holy Staff variants
- Flash Heal spell is now a spell book choice
- Paladin melee weapons come with new skills

# 2.6.4

- Rebalance priest equipment attributes

# 2.6.3

- Update AzureLib Armor

# 2.6.2

- Add vanilla recipe book support (fully datagen recipes)
- Add spell book descriptions
- Add basic armor trim support
- Update paladin armor models
- Update translations
- Remove spell specific weaknesses

# 2.6.1

- Fix to have swamp village sanctuary spawn a villager #63

# 2.6.0

- Migrate to Architectury

# 2.5.3

- Improve snowy village structure

# 2.5.2

- Update visuals of Priest Absorption effect
- Built in compatibility with Repurposed Structures #61, thanks TelepathicGrunt

# 2.5.1

- Basic Heal spell targeting is now sticky

# 2.5.0

- Flash Heal targeting is now sticky

# 2.4.14

- Fix great hammer break particle texturing

# 2.4.13

- Fix Divine Protection scaling

# 2.4.12

- Rebalance: slightly reduce healing done by priest spells
- Rebalance: Divine Protection is now powered by Healing Power attribute (more power = more stacks)
- Rebalance: Circle of Healing has now longer absorption duration
- Update translations

# 2.4.11

- Add armor type tags
- Update translations

# 2.4.10

- AzureLib armor version requirement (in fabric.mod.json)

# 2.4.9

- Add spell scroll textures

# 2.4.8

- Fix trade advancements
- Wands now use different classification tag
- Change Flash Heal to `Primary` spell, increase its power coefficient by 20%
- Add spell casting advancements

# 2.4.7

- Fix some disassembly smelting recipes

# 2.4.6

- Add Sanctuary trade advancement
- Sanctuary chests now may contain Lapis Lazuli
- Change repair material for armor pieces
- Add smelting recipes for disassembling archer weapons and armor pieces

# 2.4.5

- Fix shields tags

# 2.4.4

- Update to Spell Engine 1.6

# 2.4.3

- Fix sound related issues, and crashes upon disconnects

# 2.4.0

- Support Spell Engine 1.5
- Rebalance basic Holy Wand healing spell
- Add category field for all crafting recipes

# 2.3.1

- Fix crash due to Battle Banner spell
- Improve visuals of Holy Light spell

# 2.3.0

- Support Spell Engine 1.4
- Support AzureLib Armor 3.X
- Healing spells no longer work on mechanical creatures (Iron Golem)
- Holy spells now always deal critical strikes against undead creatures #1
- Judgement spell now deals +50% damage against undead creatures
- Judgement stun no longer works any boss mobs

# 2.2.4

- Support Lithostitched v1.4

# 2.2.3

- Increase light output of Judgement spell
- Add spell scroll names

# 2.2.2

- Add support for Lithostitched village structure injection
- Update structure files for newer NBT version

# 2.2.1

- Add Aether specific mace: Sun's Mace
- Fix some texture issues

# 2.2.0

- Fix enchantability of shields
- Udpdate Russian translation, thanks to @Heimdallr
- Add Brazilian translation, thanks to @demorogabrtz
- Add new weapons, obtainable only as loot from Aether dungeons
  - Holy Claymore
  - Valkyrie Great Hammer
  - Silver Staff of the Valkyrie
  - Valkyrie Bulwark

# 2.1.1

- Fix Netherite Claymore recipe

# 2.1.0

- Add new armor set: Netherite Crusader Armor
- Add new armor set: Netherite Prior Robes
- Rebalance armor and weapon attributes

# 2.0.5

- Judgement spell json specifying target requirement
- Fireproof shields with Netherite or better material

# 2.0.4

- Fix Barrier not knocking back hostile players

# 2.0.3

- Fix breaking change with Spell Engine

# 2.0.2

- Update dependency declarations
- Allow running on 1.21

# 2.0.1

- Bump item config version, to avoid crashing on migrated worlds

# 2.0.0

- Update to Minecraft 1.21.1
- Update Paladin Helmet sprite :)

# 1.3.1

Update priest Barrier spell:
- Increased range
- Improved shader compatibility
- Added continuous knockback against hostile entities

# 1.3.0

- Add new material tiered shields, providing attribute bonuses and high durability, custom visuals
  - Iron Kite Shield
  - Golden Kite Shield
  - Diamond Kite Shield
  - Netherite Kite Shield
  - Aeternium Kite Shield (BetterEnd exclusive)
  - Ruby Kite Shield (BetterNether exclusive)

# 1.2.6

- Fix some item tags

# 1.2.4

- Add enchantment condition tags 

# 1.2.3

 - Update armor attribute defaults
 - Update Russian translation, thanks to Alexander317

# 1.2.2

- Update Crusader Armor texture
- Update Italian translation, thanks to @Zano1999

# 1.2.1

- Update claymore textures
- Add new Ruby weapons: Claymore, Great Hammer, Mace
- Add new Aeternium Mace
- Restructure advancements
- Rebalance melee weapon attributes
- Add French translation, thanks to valentin56610
- Update Fabric Loader to 15+ for embedded MixinExtras

# 1.2.0

- Migrate to latest Spell Power Attributes API
- Battle Banner now also provides ranged attack haste (configurable)

# 1.1.3

- Fix Battle Banner not rendering properly
- Improve Battle Banner spell tooltip, to include range
- Update Italian translation, thanks to @Zano1999

# 1.1.2

- Fix Barrier entity causing server side launch crash

# 1.1.1

- Fix Aeternium claymore texture gaps
- Improve Battle Banner render effect
- Battle Banner range affected by healing power

# 1.1.0

Paladin changes:
- Add new spell: Battle Banner
- Reduce cooldown of Flash Heal spell from 8 to 6 seconds
- Update all spell icons

Priest changes:
- Add new spell: Barrier
- Update visual effects for Circle of Healing spell 

Other changes:
- Add equipment tier item tags in `rpg_series` scope
- Add Diamond Holy Wand
- Retexture Aeternium Claymore
- Staves and Wands now all come with tier 0 spell, all of them are now able to cast from spell books
- Fix spanish translation
- Remove loot config, replaced by `config/rpg_series/loot.json`

# 1.0.5

- Add compatibility for `c:wood_sticks` in recipes #16
- Add Spanish translation, thanks to @SirColor
- Update Chinese translation, thanks to @sillymoon
- Fix weapon tags not loading, when BetterX mods are not present

# 1.0.4

- Update creative tab icon
- Add effect descriptions
- Add Russian translation, thanks to skel39eek66 #12
- Add basic item tags, to include all weapons
  - `paladins:claymores`
  - `paladins:great_hammers`
  - `paladins:maces`
  - `paladins:staves`
  - `paladins:wands`

# 1.0.3

- Update name for Mod Menu
- Retexture Ruby Holy Staff
- Add hint tooltip for Monk Workbench block

# 1.0.2

- Limit Sanctuary spawns to 1 per village (configurable), thanks to the Structure Pool API
- Retexture great hammers and maces, thanks to Jaam!
- Fix some default loot table configurations
- Fix some player skin cases clipping through armor #10
- Tweak villager trade offers
- Update to latest Spell Engine API
- Add Simplified Chinese translation, thanks to @sillymoon/zh_cn
- Add Italian translation, thanks to @Zano1999

# 1.0.1

- Update mod menu settings link

# 1.0.0

Now works with Minecraft Forge, via Connector.

- Migrate to Azure Armor Lib
- Add Ukrainian translation thanks to unroman #4

# 0.9.10

- Add new staff: Diamond Holy Staff
- Add particle effects on Divine Protection pop
- Add Ruby Holy Staff emissive texture
- Update render effect API usage

# 0.9.9

- Add new spell for Holy Wands: Heal
- Reduce range of Flash Heal
- Rework Divine Protection spell
- Fix warnings recipes with absent materials
- Update JSON API usage

# 0.9.8

- Fix netherite weapon upgrade recipes

# 0.9.7

- Support Minecraft 1.20.1

# 0.9.6

- Remove netherite weapon crafting recipes

# 0.9.5

- Add smithing table upgrade recipes to upgrade to netherite weapons 

# 0.9.4

- Add Paladin and Priest spell books

# 0.9.3

- Fix Ruby Holy Staff repair recipe

# 0.9.2

- Add monk villager with village structures 

# 0.9.1

Initial Release!

- 2 new playable classes (a total of 6 new spells)
- New weapons Claymores, Great Hammers, Maces (for Paladins)
- New weapons Staves, Wands (for Priests)
- Some advancements

#
