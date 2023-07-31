package net.paladins.item;

import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.item.trinket.SpellBooks;

import java.util.List;

public class PaladinBooks {
    public static void register() {
        var books = List.of("paladin", "priest");
        for (var name: books) {
            SpellBooks.createAndRegister(new Identifier(PaladinsMod.ID, name), Group.KEY);
        }
    }
}
