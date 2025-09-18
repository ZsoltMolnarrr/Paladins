package net.paladins.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.item.Group;

public class PaladinBlocks {
    public static final MonkWorkbenchBlock MONK_WORKBENCH = new MonkWorkbenchBlock(FabricBlockSettings.create().hardness(2).nonOpaque());
    public static final BlockItem MONK_WORKBENCH_BLOCK = new BlockItem(MONK_WORKBENCH, new Item.Settings());

    public static void register() {
        Registry.register(Registries.BLOCK, Identifier.of(PaladinsMod.ID, MonkWorkbenchBlock.NAME), MONK_WORKBENCH);
        Registry.register(Registries.ITEM, Identifier.of(PaladinsMod.ID, MonkWorkbenchBlock.NAME), MONK_WORKBENCH_BLOCK);
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
            content.add(MONK_WORKBENCH_BLOCK);
        });
    }
}
