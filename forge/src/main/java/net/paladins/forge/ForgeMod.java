package net.paladins.forge;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.paladins.PaladinsMod;
import net.minecraftforge.fml.common.Mod;

@Mod(PaladinsMod.ID)
public class ForgeMod {
    public ForgeMod() {
        // Submit our event bus to let architectury register our content on the right time
        PaladinsMod.init();
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        // These don't seem to do anything :D
    }
}