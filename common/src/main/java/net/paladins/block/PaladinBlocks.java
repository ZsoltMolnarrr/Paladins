package net.paladins.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.paladins.PaladinsMod;

import java.util.function.Consumer;

public class PaladinBlocks {
    public static final Identifier MONK_WORKBENCH_ID = Identifier.fromNamespaceAndPath(PaladinsMod.ID, MonkWorkbenchBlock.NAME);

    public static final MonkWorkbenchBlock MONK_WORKBENCH = new MonkWorkbenchBlock(BlockBehaviour.Properties.of()
            .setId(ResourceKey.create(Registries.BLOCK, MONK_WORKBENCH_ID))
            .destroyTime(2)
            .noOcclusion());

    // 1.21.5+: tooltips are appended by the item, not the block.
    public static final BlockItem MONK_WORKBENCH_BLOCK = new BlockItem(MONK_WORKBENCH, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, MONK_WORKBENCH_ID))
            .useBlockDescriptionPrefix()) {
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent,
                                  Consumer<Component> textConsumer, TooltipFlag type) {
            super.appendHoverText(stack, context, displayComponent, textConsumer, type);
            textConsumer.accept(Component.translatable("block." + PaladinsMod.ID + "." + MonkWorkbenchBlock.NAME + ".hint")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    };

    public static void register() {
        Registry.register(BuiltInRegistries.BLOCK, MONK_WORKBENCH_ID, MONK_WORKBENCH);
        Registry.register(BuiltInRegistries.ITEM, MONK_WORKBENCH_ID, MONK_WORKBENCH_BLOCK);
        // Creative-tab placement of the monk workbench (into the Paladins group) is registered per-platform
        // from each loader's entrypoint (Fabric: ItemGroupEvents; NeoForge: BuildCreativeModeTabContentsEvent).
    }
}
