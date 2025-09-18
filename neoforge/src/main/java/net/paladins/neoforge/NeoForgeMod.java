package net.paladins.neoforge;

import net.neoforged.fml.common.Mod;

import net.paladins.PaladinsMod;

@Mod(PaladinsMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod() {
        // Run our common setup.
        PaladinsMod.init();
    }
}
