package net.paladins.neoforge;

import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.paladins.PaladinsMod;

@Mod(PaladinsMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        PaladinsMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            PaladinsMod.registerSounds();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            PaladinsMod.registerItems();
        });
        event.register(RegistryKeys.BLOCK, reg -> {
            PaladinsMod.registerBlocks();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            PaladinsMod.registerEffects();
        });
        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, reg -> {
            // Not sure why errors are thrown, but this seems to fix it.
            try {
                PaladinsMod.registerPOI();
            } catch (Exception e) { }
        });
        event.register(RegistryKeys.VILLAGER_PROFESSION, reg -> {
            PaladinsMod.registerVillagers();
        });
    }
}
