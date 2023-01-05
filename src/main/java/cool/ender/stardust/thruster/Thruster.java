package cool.ender.stardust.thruster;

import cool.ender.stardust.Stardust;
import cool.ender.stardust.registry.TileRegistry;
import cool.ender.stardust.tube.Tube;
import cool.ender.stardust.tube.TubeConnectable;
import cool.ender.stardust.turret.AbstractTurret;
import cool.ender.stardust.turret.small.RailGun1Small;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static cool.ender.stardust.turret.small.RailGun1Small.Block.CANNON_FACING;

public class Thruster {
    public static class Block extends BaseEntityBlock implements TubeConnectable {
        public static final DirectionProperty THRUSTER_FACING = DirectionProperty.create("thruster_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
        public static final BooleanProperty IGNITE = BooleanProperty.create("ignite");

        public Block() {
            super(Properties.of(Material.STONE).noOcclusion().explosionResistance(10));
            this.registerDefaultState(this.stateDefinition.any().setValue(THRUSTER_FACING, Direction.NORTH).setValue(IGNITE, Boolean.FALSE));

        }

        @Override
        public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                level.setBlock(blockPos, blockState.setValue(IGNITE, !blockState.getValue(IGNITE)), 2);
                return InteractionResult.CONSUME;
            }
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext p_52669_) {
            return this.defaultBlockState().setValue(THRUSTER_FACING, p_52669_.getNearestLookingDirection().getOpposite());
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> p_52719_) {
            p_52719_.add(THRUSTER_FACING).add(IGNITE);

        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {

            return new Tile(blockPos, blockState);
        }

        @Override
        public boolean getConnectable(Direction direction) {
            return true;
        }
    }

    public static class Tile extends BlockEntity implements IAnimatable {
        public AnimationFactory factory = GeckoLibUtil.createFactory(this);

        public Tile(BlockPos p_155229_, BlockState p_155230_) {
            super(TileRegistry.THRUSTER_TILE.get(), p_155229_, p_155230_);
        }

        @Override
        public void registerControllers(AnimationData data) {

        }

        @Override
        public AnimationFactory getFactory() {
            return factory;
        }
    }

    public static class Model extends AnimatedGeoModel<Tile> {

        @Override
        public ResourceLocation getModelLocation(Tile object) {

            return new ResourceLocation(Stardust.MOD_ID, "geo/thruster.geo.json");
        }

        @Override
        public ResourceLocation getTextureLocation(Tile object) {
            if (object.getBlockState().getValue(Block.IGNITE)) {
                return new ResourceLocation(Stardust.MOD_ID, "textures/block/thruster_on.png");
            } else {
                return new ResourceLocation(Stardust.MOD_ID, "textures/block/thruster_off.png");
            }
        }

        @Override
        public ResourceLocation getAnimationFileLocation(Tile animatable) {
            return null;
        }

        public void setCustomAnimations(Tile animatable, int instanceId) {
            super.setCustomAnimations(animatable, instanceId);
            switch (animatable.getBlockState().getValue(Block.THRUSTER_FACING)) {
                case SOUTH -> {
                    this.getAnimationProcessor().getBone("bone").setRotationY((float) (Math.PI));
                }

                case NORTH -> {
                }

                case WEST -> {
                    this.getAnimationProcessor().getBone("bone").setRotationY((float) (Math.PI * 0.5));
                }

                case EAST -> {
                    this.getAnimationProcessor().getBone("bone").setRotationY((float) (Math.PI * -0.5));
                }

                case UP -> {
                    this.getAnimationProcessor().getBone("bone").setRotationX((float) (Math.PI * 0.5));
                }

                case DOWN -> {
                    this.getAnimationProcessor().getBone("bone").setRotationX((float) (Math.PI * -0.5));
                }
            }
        }
    }

    public static class Renderer extends GeoBlockRenderer<Thruster.Tile> {
        public Renderer(BlockEntityRendererProvider.Context rendererProvider) {
            super(rendererProvider, new Thruster.Model());
        }
    }

    public abstract static class Listener {
        @Mod.EventBusSubscriber(modid = Stardust.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public abstract static class ClientListener {
            @SubscribeEvent
            public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
                event.registerBlockEntityRenderer(TileRegistry.THRUSTER_TILE.get(), Thruster.Renderer::new);
            }
        }
    }
}
