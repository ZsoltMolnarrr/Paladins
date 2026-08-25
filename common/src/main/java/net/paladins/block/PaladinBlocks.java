package net.paladins.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;

import java.util.function.Consumer;

public class PaladinBlocks {
    public static final Identifier MONK_WORKBENCH_ID = Identifier.of(PaladinsMod.ID, MonkWorkbenchBlock.NAME);

    public static final MonkWorkbenchBlock MONK_WORKBENCH = new MonkWorkbenchBlock(AbstractBlock.Settings.create()
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, MONK_WORKBENCH_ID))
            .hardness(2)
            .nonOpaque());

    // 1.21.5+: tooltips are appended by the item, not the block.
    public static final BlockItem MONK_WORKBENCH_BLOCK = new BlockItem(MONK_WORKBENCH, new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, MONK_WORKBENCH_ID))
            .useBlockPrefixedTranslationKey()) {
        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent,
                                  Consumer<Text> textConsumer, TooltipType type) {
            super.appendTooltip(stack, context, displayComponent, textConsumer, type);
            textConsumer.accept(Text.translatable("block." + PaladinsMod.ID + "." + MonkWorkbenchBlock.NAME + ".hint")
                    .formatted(Formatting.GRAY, Formatting.ITALIC));
        }
    };

    public static void register() {
        Registry.register(Registries.BLOCK, MONK_WORKBENCH_ID, MONK_WORKBENCH);
        Registry.register(Registries.ITEM, MONK_WORKBENCH_ID, MONK_WORKBENCH_BLOCK);
        // Creative-tab placement of the monk workbench (into the Paladins group) is registered per-platform
        // from each loader's entrypoint (Fabric: ItemGroupEvents; NeoForge: BuildCreativeModeTabContentsEvent).
    }
}
