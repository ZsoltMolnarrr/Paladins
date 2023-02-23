package net.paladins.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.paladins.Platform;

import static net.paladins.Platform.Type.FABRIC;

public class PlatformImpl {
    public static Platform.Type getPlatformType() {
        return FABRIC;
    }

    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}
