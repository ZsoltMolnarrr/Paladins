package net.paladins.item.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.item.armor.Armor;

public class PriestArmor extends CustomArmor {

    public static final Identifier equipSoundId = new Identifier(PaladinsMod.ID, "paladin_armor_equip");
    public static final SoundEvent equipSound = new SoundEvent(equipSoundId);

    public PriestArmor(Armor.CustomMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }
}
