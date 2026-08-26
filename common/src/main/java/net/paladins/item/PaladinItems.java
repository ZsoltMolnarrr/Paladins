package net.paladins.item;

import net.minecraft.world.item.Item;
import net.paladins.item.armor.Armors;
import net.spell_engine.rpg_series.item.Armor;

import java.util.HashMap;

public class PaladinItems {
    public static final HashMap<String, Item> entries;
    static {
        entries = new HashMap<>();
        for(var weaponEntry: PaladinWeapons.entries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for(var entry: Armors.entries) {
            var set = entry.armorSet();
            for (var piece: set.pieces()) {
                var armorItem = (Armor.CustomItem) piece;
                entries.put(set.idOf(armorItem).toString(), armorItem);
            }
        }
    }
}
