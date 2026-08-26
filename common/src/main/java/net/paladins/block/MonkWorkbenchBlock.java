package net.paladins.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MonkWorkbenchBlock extends Block {
    public static final String NAME = "monk_workbench";
    public MonkWorkbenchBlock(Properties settings) {
        super(settings);
    }

    // `Block#appendTooltip` is gone since 1.21.5 — the hint is appended by the block item
    // (see PaladinBlocks.MONK_WORKBENCH_BLOCK).

    // MARK: Shape

    public static final VoxelShape TOP_SHAPE = Block.box(1, 12, 1, 15, 16, 15);
    public static final VoxelShape MIDDLE_SHAPE = Block.box(4, 3, 4, 12, 12, 12);
    public static final VoxelShape BOTTOM_SHAPE = Block.box(1, 0, 1, 15, 3, 15);
    private static final VoxelShape SHAPE = Shapes.or(TOP_SHAPE, MIDDLE_SHAPE, BOTTOM_SHAPE);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // MARK: Facing

    private static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // MARK: Partial transparency

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }
}
