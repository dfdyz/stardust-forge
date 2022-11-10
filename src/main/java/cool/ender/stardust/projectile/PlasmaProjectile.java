package cool.ender.stardust.projectile;

import cool.ender.stardust.Stardust;
import cool.ender.stardust.projectile.explosion.AbstractExplosion;
import cool.ender.stardust.projectile.explosion.PlasmaExplosion;
import cool.ender.stardust.registry.EntityRegistry;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.manager.AnimationData;

public class PlasmaProjectile extends AbstractProjectile{

    public static class Entity extends AbstractProjectile.Entity {
        public Entity(EntityType<? extends AbstractProjectile.Entity> entityType, Level level) {
            super(entityType, level);

        }

        @Override
        public void registerControllers(AnimationData data) {

        }

        @Override
        protected void onHit(HitResult p_37260_) {
            super.onHit(p_37260_);
            if (!this.level.isClientSide()) {
                this.getExplosion().doDamage(p_37260_.getLocation());
                this.remove(RemovalReason.DISCARDED);
            }
        }

        @Override
        protected @NotNull ParticleOptions getTrailParticle() {
            return ParticleTypes.DRAGON_BREATH;
        }

        @Override
        AbstractExplosion getExplosion() {
            return new PlasmaExplosion(0f, 10f, 100f, this.level);
        }
    }

    public static class Model extends AbstractProjectile.Model {

        @Override
        public ResourceLocation getModelLocation(AbstractProjectile.Entity object) {
            return null;
        }

        @Override
        public ResourceLocation getTextureLocation(AbstractProjectile.Entity object) {
            return null;
        }

        @Override
        public ResourceLocation getAnimationFileLocation(AbstractProjectile.Entity animatable) {
            return null;
        }
    }

    public static class Renderer extends AbstractProjectile.Renderer {

        public Renderer(EntityRendererProvider.Context renderManager) {
            super(renderManager, null);
        }

        @Override
        public boolean shouldRender(AbstractProjectile.@NotNull Entity p_114491_, @NotNull Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
            return false;
        }
    }

    public static class Listener extends AbstractProjectile.Listener {
        @Mod.EventBusSubscriber(modid = Stardust.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public static class ClientListener extends AbstractProjectile.Listener.ClientListener {
            @SubscribeEvent
            public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
                event.registerEntityRenderer(EntityRegistry.PLASMA_PROJECTILE_ENTITY.get(), Renderer::new);

            }
        }
    }
}
