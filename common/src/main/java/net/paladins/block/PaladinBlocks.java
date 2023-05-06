package net.paladins.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.paladins.PaladinsMod;

public class PaladinBlocks {
    public static final MonkWorkbenchBlock MONK_WORKBENCH = new MonkWorkbenchBlock(FabricBlockSettings.of(Material.STONE).hardness(2).nonOpaque());

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier(PaladinsMod.ID, MonkWorkbenchBlock.NAME), MONK_WORKBENCH);
        Registry.register(Registry.ITEM, new Identifier(PaladinsMod.ID, MonkWorkbenchBlock.NAME), new BlockItem(MONK_WORKBENCH, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
    }
}
