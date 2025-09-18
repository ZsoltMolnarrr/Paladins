package net.paladins.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.paladins.item.armor.Armors;

import java.util.HashMap;

public class PaladinItems {
    public static final HashMap<String, Item> entries;
    static {
        entries = new HashMap<>();
        for(var weaponEntry: Weapons.entries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for(var entry: Armors.entries) {
            var set = entry.armorSet();
            for (var piece: set.pieces()) {
                var armorItem = (ArmorItem) piece;
                entries.put(set.idOf(armorItem).toString(), armorItem);
            }
        }
    }
}
