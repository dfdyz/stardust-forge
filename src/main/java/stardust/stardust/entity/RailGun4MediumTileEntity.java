package stardust.stardust.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import stardust.stardust.Stardust;
import stardust.stardust.registry.TileEntityTypeRegistry;

import java.net.PortUnreachableException;
import java.util.zip.DeflaterOutputStream;

public class RailGun4MediumTileEntity extends TileEntity implements IAnimatable, ITickableTileEntity {
    public static final Logger LOGGER = LogManager.getLogger();
    private final AnimationFactory factory = new AnimationFactory(this);
    private final AnimationController<RailGun4MediumTileEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);

    public enum RotationState {
        ROTATING, COMPLETED, FREE
    }

    private RotationState rotationState = RotationState.FREE;
    private double initialRotationYPrefix = 180;
    private double initialRotationXPrefix = 0;
    private int cd = 60;
    private long lastShootTick = 0;
    private int shootCount = 0;
    private final int rotationSpeed = 3;
    public double nowRotationX = 0;
    public double nowRotationY = 0;
    public Vector3d targetPos;
    public double targetRotationX = 0;
    public double targetRotationY = 0;
    private final double heightOffset = 1.0d;
    private final Vector3d barrelRootOffset = new Vector3d(0.0d, -0.3d, 4.0d);

    public RailGun4MediumTileEntity() {
        super(TileEntityTypeRegistry.RAIL_GUN_4_MEDIUM_TILE_ENTITY.get());
    }

    public void rotate(MatrixStack stack) {
//        stack.rotate(Vector3f.XP.rotationDegrees((float) nowRotationX));
//        stack.rotate(Vector3f.YP.rotationDegrees((float) nowRotationY));
//        stack.rotate(Vector3f.XP.rotationDegrees(45));
//        stack.rotate(Vector3f.YP.rotationDegrees(180));

    }

    public double getActualRotationY() {
        return nowRotationY + initialRotationYPrefix;
    }

    public double getActualRotationX() {
        return nowRotationX + initialRotationXPrefix;
    }


    private Vector3d getBarrelRootPos(Vector3d blockCenterPos) {
        return blockCenterPos.add(this.barrelRootOffset.rotateYaw((float) Math.toRadians(this.getActualRotationY())));
    }

    public void rotateTo(Vector3d targetPos) {

        Vector3d turretCenter = getBlockCenter(this.getPos());

        double a = turretCenter.getX();
        double b = turretCenter.getY();
        double c = turretCenter.getZ();

        double d = targetPos.getX();
        double e = targetPos.getY();
        double f = targetPos.getZ();
        this.rotationState = RotationState.ROTATING;
        this.targetRotationY = - getAngularDifferenceY(getBarrelInitialTowardTo(), new Vector3d(d - a, 0, f - c));

    }

    public Vector3d getBarrelInitialTowardTo() {
        return new Vector3d(0, 0, 1.0d).rotateYaw((float) Math.toRadians(initialRotationYPrefix));
    }

    public double getAngularDifferenceY(Vector3d vec1, Vector3d vec2) {
        return Math.toDegrees(Math.acos((vec1.x * vec2.x + vec1.z * vec2.z) / (Math.sqrt(vec1.x * vec1.x + vec1.z * vec1.z) * Math.sqrt(vec2.x * vec2.x + vec2.z * vec2.z))));
    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("railgun4_shooting", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(controller);
    }


    public void shoot(Vector3d targetPos) {
        World world = this.getWorld();
        assert world != null;
        if (this.lastShootTick + this.cd > getWorld().getGameTime()) return;
        this.lastShootTick = getWorld().getGameTime();
        if (!world.isRemote()) {

            LOGGER.info(String.format("the %s times of shooting", ++shootCount));

            Vector3d blockCenter = this.getBlockCenter(this.getPos());
            Vector3d barrelRootPos = getBarrelRootPos(blockCenter);
            double x0 = barrelRootPos.getX();
            double y0 = barrelRootPos.getY();
            double z0 = barrelRootPos.getZ();

            y0 += heightOffset;

            double x1 = targetPos.getX() - x0;
            double y1 = targetPos.getY() - y0;
            double z1 = targetPos.getZ() - z0;

            RailGunProjectileEntity projectile = new RailGunProjectileEntity(this.world, AbstractStardustProjectileEntity.ProjectileType.KINETIC_HIGHLY_EXPLOSIVE, 1000, 10.0f, this, x0, y0, z0, x1, y1, z1);
            projectile.setRawPosition(x0, y0, z0);
            world.addEntity(projectile);
        }
        this.rotationState = RotationState.FREE;
    }

    private Vector3d getBlockCenter(BlockPos pos) {
        return new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }

    private void rotateTick() {
        if (this.targetRotationY > this.nowRotationY) {
            if (Math.abs(this.targetRotationY - this.nowRotationY) < this.rotationSpeed) this.nowRotationY = this.targetRotationY;
            else this.nowRotationY += this.rotationSpeed;
        } else if (this.targetRotationY < this.nowRotationY) {
            if (Math.abs(this.targetRotationY - this.nowRotationY) < this.rotationSpeed) this.nowRotationY = this.targetRotationY;
            else this.nowRotationY -= this.rotationSpeed;
        } else if (this.targetRotationY == this.nowRotationY){
            this.rotationState = RotationState.COMPLETED;
        }
//        this.nowRotationY = this.targetRotationY;
//        this.rotationState = RotationState.COMPLETED;
    }

    @Override
    public void tick() {
        if (this.rotationState == RotationState.FREE) {
            World world = this.world;
            assert world != null;
            LivingEntity entity = world.getClosestEntity(VillagerEntity.class, EntityPredicate.DEFAULT, null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getRenderBoundingBox().grow(64d, 20d, 64d));
            if (entity != null) {
                this.targetPos = entity.getPositionVec();
                rotateTo(this.targetPos);
            }
        } else if (this.rotationState == RotationState.COMPLETED) {
            shoot(this.targetPos);
        } else if (this.rotationState == RotationState.ROTATING) {
            rotateTick();
        }
//        if (controller.getAnimationState() == AnimationState.Stopped) {
//            controller.setAnimation(new AnimationBuilder().addAnimation("railgun4_shooting", true));
//        }
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
