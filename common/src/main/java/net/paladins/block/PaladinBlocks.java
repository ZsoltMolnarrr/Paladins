package net.paladins.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;

public class PaladinBlocks {
    public static final MonkWorkbenchBlock MONK_WORKBENCH = new MonkWorkbenchBlock(AbstractBlock.Settings.create().hardness(2).nonOpaque());
    public static final BlockItem MONK_WORKBENCH_BLOCK = new BlockItem(MONK_WORKBENCH, new Item.Settings());

    /// Forge 47 unfreezes exactly one registry per `RegisterEvent` window, so the block and its block
    /// item must be registered from separate windows (see `ForgeMod.register`).
    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(PaladinsMod.ID, MonkWorkbenchBlock.NAME), MONK_WORKBENCH);
    }

    public static void registerBlockItems() {
        Registry.register(Registries.ITEM, new Identifier(PaladinsMod.ID, MonkWorkbenchBlock.NAME), MONK_WORKBENCH_BLOCK);
        // Creative-tab placement of the monk workbench (into the Paladins group) goes through SpellEngine's
        // `PlatformEvents.onItemGroupModify` from PaladinsMod.registerItems().
    }
}
