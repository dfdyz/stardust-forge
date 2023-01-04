package cool.ender.stardust.turret.medium;

import cool.ender.stardust.Stardust;
import cool.ender.stardust.registry.TileRegistry;
import cool.ender.stardust.tube.TubeConnectable;
import cool.ender.stardust.turret.AbstractTurret;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.manager.AnimationData;

import static cool.ender.stardust.turret.medium.RailGun1Medium.Block.BARREL_FACING;
import static cool.ender.stardust.turret.medium.RailGun1Medium.Block.BASE_FACING;


public class RailGun1Medium extends AbstractTurret {


    public static class Block extends AbstractTurret.Block implements TubeConnectable {

        public static final DirectionProperty BASE_FACING = DirectionProperty.create("base_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
        public static final DirectionProperty BARREL_FACING = DirectionProperty.create("barrel_facing",Direction.NORTH,Direction.WEST,Direction.EAST,Direction.SOUTH, Direction.UP, Direction.DOWN);

        public Block() {
            super(Properties.of(Material.STONE).noOcclusion());
            this.registerDefaultState(this.stateDefinition.any().setValue(BASE_FACING, Direction.SOUTH)
                    .setValue(BARREL_FACING,Direction.SOUTH));
        }

        public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
            return new Tile(blockPos, blockState);
        }

        @Override
        public boolean getConnectable(Direction direction) {
            return true;
        }

        @Override
        public BlockState getStateForPlacement( BlockPlaceContext context) {
            return this.defaultBlockState().setValue(BARREL_FACING, context.getNearestLookingDirection().getOpposite())
                    .setValue(BASE_FACING, context.getClickedFace());
        }

        @Override
        protected void createBlockStateDefinition( StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> p_52719_) {
            p_52719_.add(BASE_FACING).add(BARREL_FACING);
        }

    }

    public static class Tile extends AbstractTurret.Tile {

        public Tile(BlockPos p_155229_, BlockState p_155230_) {
            super(TileRegistry.RAIL_GUN_1_MEDIUM_TILE.get(), p_155229_, p_155230_);
        }

        @Override
        public void registerControllers(AnimationData data) {

        }
    }

    public static class Model extends AbstractTurret.Model {

        @Override
        public ResourceLocation getModelLocation(AbstractTurret.Tile object) {
            return new ResourceLocation(Stardust.MOD_ID, "geo/rail_gun_4_medium.geo.json");
        }

        @Override
        public ResourceLocation getTextureLocation(AbstractTurret.Tile object) {
            return new ResourceLocation(Stardust.MOD_ID, "textures/block/rail_gun_4_medium.png");
        }

        @Override
        public ResourceLocation getAnimationFileLocation(AbstractTurret.Tile animatable) {
            return null;
        }

        public void setCustomAnimations(AbstractTurret.Tile animatable, int instanceId) {
            super.setCustomAnimations(animatable, instanceId);
            switch (animatable.getBlockState().getValue(BASE_FACING)) {
                case SOUTH -> {
                    this.getAnimationProcessor().getBone("base").setRotationY((float) (Math.PI));
                }

                case NORTH -> {
                }

                case WEST -> {
                    this.getAnimationProcessor().getBone("base").setRotationY((float) (Math.PI * 0.5));
                }

                case EAST -> {
                    this.getAnimationProcessor().getBone("base").setRotationY((float) (Math.PI * -0.5));
                }

                case UP -> {
                    this.getAnimationProcessor().getBone("base").setRotationZ((float) (Math.PI * -0.5));
                }

                case DOWN -> {
                    this.getAnimationProcessor().getBone("base").setRotationZ((float) (Math.PI * 0.5));
                }
            }
            switch (animatable.getBlockState().getValue(BARREL_FACING)) {
                case SOUTH -> {
                    this.getAnimationProcessor().getBone("barrel_moving_up_down").setRotationY((float) (Math.PI ));
                }

                case NORTH -> {
                }

                case WEST -> {
                    this.getAnimationProcessor().getBone("barrel_moving_up_down").setRotationY((float) (Math.PI * 0.5));
                }

                case EAST -> {
                    this.getAnimationProcessor().getBone("barrel_moving_up_down").setRotationY((float) (Math.PI * -0.5));
                }
                case UP -> {
                    this.getAnimationProcessor().getBone("barrel_moving_up_down").setRotationZ((float) (Math.PI * -0.5));
                }

                case DOWN -> {
                    this.getAnimationProcessor().getBone("barrel_moving_up_down").setRotationZ((float) (Math.PI * 0.5));
                }

            }
        }
    }

    public static class Renderer extends AbstractTurret.Renderer {

        public Renderer(BlockEntityRendererProvider.Context rendererProvider) {
            super(rendererProvider, new Model());
        }
    }

    public abstract static class Listener extends AbstractTurret.Listener {
        @Mod.EventBusSubscriber(modid = Stardust.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public abstract static class ClientListener extends AbstractTurret.Listener.ClientListener {
            @SubscribeEvent
            public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
                event.registerBlockEntityRenderer(TileRegistry.RAIL_GUN_1_MEDIUM_TILE.get(), RailGun1Medium.Renderer::new);
            }
        }
    }

    @Override
    public String getRegisterName() {
        return "rail_gun_1_medium";
    }
}
