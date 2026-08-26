package net.paladins.item.armor;

import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.spell_engine.rpg_series.item.Armor;

public class PaladinArmor extends Armor.CustomItem {
    public PaladinArmor(ArmorMaterial material, ArmorType slot, Properties settings) {
        super(material, slot, settings);
    }
}
