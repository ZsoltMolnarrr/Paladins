package net.paladins.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;

public class PaladinBlocks {
    /// Shared by the block and its block item — the same path in two different registries.
    public static final Identifier MONK_WORKBENCH_ID = new Identifier(PaladinsMod.ID, MonkWorkbenchBlock.NAME);

    public static final MonkWorkbenchBlock MONK_WORKBENCH = new MonkWorkbenchBlock(AbstractBlock.Settings.create().hardness(2).nonOpaque());
    public static final BlockItem MONK_WORKBENCH_BLOCK = new BlockItem(MONK_WORKBENCH, new Item.Settings());

    /// Forge 47 unfreezes exactly one registry per `RegisterEvent` window, so the block and its block
    /// item must be registered from separate windows (see `ForgeMod.register`). The block item is created
    /// by this class's `<clinit>`, i.e. inside the BLOCK window — that is fine: only *registration* is
    /// confined to its own window, construction merely has to fall inside the `RegisterEvent` sequence.
    /// It is handed out by `PaladinsMod.blockItemsToRegister()` and registered from the ITEM window.
    ///
    /// Creative-tab placement of the monk workbench (into the Paladins group) goes through SpellEngine's
    /// `PlatformEvents.onItemGroupModify` from `PaladinsMod.itemsToRegister()`.
    public static void register() {
        Registry.register(Registries.BLOCK, MONK_WORKBENCH_ID, MONK_WORKBENCH);
    }
}
