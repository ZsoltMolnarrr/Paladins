package net.paladins.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class MonkWorkbenchBlock extends Block {
    public static final String NAME = "monk_workbench";
    public MonkWorkbenchBlock(Settings settings) {
        super(settings);
    }

    // `Block#appendTooltip` is gone since 1.21.5 — the hint is appended by the block item
    // (see PaladinBlocks.MONK_WORKBENCH_BLOCK).

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

    private static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // MARK: Partial transparency

    @Override
    protected boolean isTransparent(BlockState state) {
        return true;
    }
}
