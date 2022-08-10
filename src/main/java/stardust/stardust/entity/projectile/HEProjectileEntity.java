package stardust.stardust.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import stardust.stardust.registry.EntityTypeRegistry;

import javax.annotation.Nonnull;

public class HEProjectileEntity extends AbstractStardustProjectileEntity implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public HEProjectileEntity(EntityType<? extends AbstractStardustProjectileEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.projectileType = ProjectileType.KINETIC_HIGHLY_EXPLOSIVE;
    }

    public HEProjectileEntity(World worldIn, long energy, float attribute, TileEntity shootTile, double startX, double startY, double startZ, double accelerationX, double accelerationY, double accelerationZ) {
        super(EntityTypeRegistry.HE_PROJECTILE_ENTITY.get(), worldIn, ProjectileType.KINETIC_HIGHLY_EXPLOSIVE, energy, attribute, shootTile, startX, startY, startZ, accelerationX, accelerationY, accelerationZ);
        this.projectileType = ProjectileType.KINETIC_HIGHLY_EXPLOSIVE;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!world.isRemote()) {
            Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.getShooter()) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), this.attribute, false, explosion$mode);
        }
        super.onImpact(result);
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult result) {
        if (result.getEntity().isLiving()) {
            LivingEntity targetEntity = (LivingEntity) result.getEntity();
            DamageSource damageSource;
            if (this.getShooter() == null) {
                damageSource = DamageSource.causeIndirectMagicDamage(this, null);
            } else {
                damageSource = DamageSource.causeIndirectMagicDamage(this, this.getShooter());
            }
            targetEntity.attackEntityFrom(damageSource, this.getEnergy());
        }
        super.onEntityHit(result);
    }

    @Override
    protected void func_230299_a_(@Nonnull BlockRayTraceResult result) {
        if (!this.world.isRemote()) {
            this.remove();
        }

        super.func_230299_a_(result);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
