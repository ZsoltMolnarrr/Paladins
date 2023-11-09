package net.paladins.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.paladins.PaladinsMod;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MonkWorkbenchBlock extends Block {
    public static final String NAME = "monk_workbench";
    public MonkWorkbenchBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        tooltip.add(Text.translatable("block." + PaladinsMod.ID + "." + NAME +".hint").formatted(Formatting.GRAY, Formatting.ITALIC));
    }

    // MARK: Shape

    public static final VoxelShape TOP_SHAPE = Block.createCuboidShape(1, 12, 1, 15, 16, 15);
    public static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(4, 3, 4, 12, 12, 12);
    public static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(1, 0, 1, 15, 3, 15);
    private static final VoxelShape SHAPE = VoxelShapes.union(TOP_SHAPE, MIDDLE_SHAPE, BOTTOM_SHAPE);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    // MARK: Facing

    private static DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        FACING = Properties.HORIZONTAL_FACING;
        builder.add(FACING);
    }

    // MARK: Partial transparency

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}
