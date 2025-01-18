package net.paladins.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.api.item.armor.Armor;

public class PriestArmor extends Armor.CustomItem {
    public PriestArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, settings);
    }
}
