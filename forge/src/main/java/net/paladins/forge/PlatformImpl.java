package net.paladins.forge;

import net.minecraftforge.fml.ModList;
import net.paladins.Platform;

import static net.paladins.Platform.Type.FORGE;

public class PlatformImpl {
    public static Platform.Type getPlatformType() {
        return FORGE;
    }

    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
