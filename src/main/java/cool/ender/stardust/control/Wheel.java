package cool.ender.stardust.control;

import cool.ender.stardust.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.impl.datastructures.DenseBlockPosSet;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;
import org.valkyrienskies.mod.common.assembly.ShipAssemblyKt;

public class Wheel {
    public static class Block extends BaseEntityBlock {

        public Block() {
            super(Properties.of(Material.STONE));
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
            return null;
        }

        @Override
        public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                DenseBlockPosSet blocks = new DenseBlockPosSet();
                blocks.add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                ServerShip ship = ShipAssemblyKt.createNewShipWithBlocks(blockPos, blocks, (ServerLevel) level);
                return InteractionResult.CONSUME;
            }
        }
    }

    public static class Tile extends BlockEntity {
        public Tile(BlockEntityType<?> tile, BlockPos blockPos, BlockState blockState) {
            super(tile, blockPos, blockState);
        }

        public Tile(BlockPos blockPos, BlockState blockState) {
            this(TileRegistry.WHEEL_TILE.get(), blockPos, blockState);
        }
    }


}