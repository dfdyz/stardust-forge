package cool.ender.stardust.tube;

import cool.ender.stardust.Stardust;
import cool.ender.stardust.registry.TileRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Tube {

    public static class Block extends BaseEntityBlock {

        public static final BooleanProperty NORTH = BooleanProperty.create("north");
        public static final BooleanProperty EAST = BooleanProperty.create("east");
        public static final BooleanProperty SOUTH = BooleanProperty.create("south");
        public static final BooleanProperty WEST = BooleanProperty.create("west");
        public static final BooleanProperty UP = BooleanProperty.create("up");
        public static final BooleanProperty DOWN = BooleanProperty.create("down");

        public Block () {
            super(Properties.of(Material.STONE).noOcclusion());
            this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false));

        }

        public BlockEntity newBlockEntity ( @NotNull BlockPos blockPos, @NotNull BlockState blockState ) {
            return new Tile(blockPos, blockState);
        }

        public void neighborChanged ( BlockState blockState, Level level, BlockPos selfBlock, net.minecraft.world.level.block.Block block, BlockPos neighborBlock, boolean p_62514_ ) {
            if (level.getBlockState(neighborBlock).getBlock() instanceof Block) {
                if (selfBlock.north().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(NORTH, true), 2);
                }
                if (selfBlock.south().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(SOUTH, true), 2);
                }
                if (selfBlock.east().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(EAST, true), 2);
                }
                if (selfBlock.west().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(WEST, true), 2);
                }
                if (selfBlock.above().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(UP, true), 2);
                }
                if (selfBlock.below().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(DOWN, true), 2);
                }
            } else {
                if (selfBlock.north().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(NORTH, false), 2);
                }
                if (selfBlock.south().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(SOUTH, false), 2);
                }
                if (selfBlock.east().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(EAST, false), 2);
                }
                if (selfBlock.west().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(WEST, false), 2);
                }
                if (selfBlock.above().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(UP, false), 2);
                }
                if (selfBlock.below().equals(neighborBlock)) {
                    level.setBlock(selfBlock, blockState.setValue(DOWN, false), 2);
                }
            }
        }

        @Nullable
        @Override
        public BlockState getStateForPlacement ( BlockPlaceContext context ) {
            BlockPos placePos = context.getClickedPos();
            BlockState defaultState = this.defaultBlockState();
            if (context.getLevel().getBlockState(placePos.north()).getBlock() instanceof Tube.Block) {
                defaultState = defaultState.setValue(NORTH, true);
            }
            if (context.getLevel().getBlockState(placePos.south()).getBlock() instanceof Tube.Block) {
                defaultState = defaultState.setValue(SOUTH, true);
            }
            if (context.getLevel().getBlockState(placePos.east()).getBlock() instanceof Tube.Block) {
                defaultState = defaultState.setValue(EAST, true);
            }
            if (context.getLevel().getBlockState(placePos.west()).getBlock() instanceof Tube.Block) {
                defaultState = defaultState.setValue(WEST, true);
            }
            if (context.getLevel().getBlockState(placePos.above()).getBlock() instanceof Tube.Block) {
                defaultState = defaultState.setValue(UP, true);
            }
            if (context.getLevel().getBlockState(placePos.below()).getBlock() instanceof Tube.Block) {
                defaultState = defaultState.setValue(DOWN, true);
            }

            return defaultState;
        }

        @Override
        protected void createBlockStateDefinition ( StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> p_49915_ ) {
            p_49915_.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
        }
    }


    public static class Tile extends BlockEntity implements IAnimatable {

        public AnimationFactory factory = GeckoLibUtil.createFactory(this);

        public Tile ( BlockPos p_155229_, BlockState p_155232_ ) {
            super(TileRegistry.TUBE_TILE.get(), p_155229_, p_155232_);
        }

        public void registerControllers ( AnimationData data ) {

        }

        @Override
        public AnimationFactory getFactory () {
            return factory;
        }
    }

    public static class Model extends AnimatedGeoModel<Tile> {
        public ResourceLocation getModelLocation ( Tile object ) {
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "geo/tube_1_1.geo.json");
            }
            //single
            if (object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "geo/tube_2_1.geo.json");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "geo/tube_3_1.geo.json");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "geo/tube_4_1.geo.json");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "geo/tube_5_1.geo.json");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "geo/tube_6_1.geo.json");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "geo/tube_7_1.geo.json");
            }

            return new ResourceLocation(Stardust.MOD_ID, "geo/tube_1_1.geo.json");
        }

        public ResourceLocation getTextureLocation ( Tile object ) {
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "textures/block/tube_1_1.png");
            }
            //single
            if (object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "textures/block/tube_2_1.png");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "textures/block/tube_3_1.png");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "textures/block/tube_4_1.png");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "textures/block/tube_5_1.png");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    object.getBlockState().getValue(Block.UP) &&
                    !object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "textures/block/tube_6_1.png");
            }
            if (!object.getBlockState().getValue(Block.NORTH) &&
                    !object.getBlockState().getValue(Block.SOUTH) &&
                    !object.getBlockState().getValue(Block.WEST) &&
                    !object.getBlockState().getValue(Block.EAST) &&
                    !object.getBlockState().getValue(Block.UP) &&
                    object.getBlockState().getValue(Block.DOWN)) {
                return new ResourceLocation(Stardust.MOD_ID, "textures/block/tube_7_1.png");
            }
            return new ResourceLocation(Stardust.MOD_ID, "textures/block/tube_1_1.png");
        }

        public ResourceLocation getAnimationFileLocation ( Tile animatable ) {
            return null;
        }

        @Override
        public void setLivingAnimations ( Tile animatable, Integer instanceId ) {
            super.setLivingAnimations(animatable, instanceId);
            this.getAnimationProcessor().getBone("bone").setRotationZ((float) (Math.PI));
        }
    }

    public static class Renderer extends GeoBlockRenderer<Tile> {
        public Renderer ( BlockEntityRendererProvider.Context rendererProvider ) {
            super(rendererProvider, new Model());
        }
    }

    public abstract static class Listener {
        @Mod.EventBusSubscriber(modid = Stardust.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public abstract static class ClientListener {
            @SubscribeEvent
            public static void registerRenderers ( final EntityRenderersEvent.RegisterRenderers event ) {
                event.registerBlockEntityRenderer(TileRegistry.TUBE_TILE.get(), Renderer::new);
            }
        }
    }

    public String getRegisterName () {
        return "tube";
    }
}
