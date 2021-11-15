package com.osir.terrametalcraft.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class ProjectileEntityBase extends AbstractArrowEntity {
	protected BlockPos groundPos;
	protected BlockState lastGround;
	protected int inAirTime;

	public ProjectileEntityBase(EntityType<? extends ProjectileEntityBase> type, World world) {
		super(type, world);
	}

	@Override
	public void tick() {
		this.baseTick();
		BlockState ground = null;
		if (this.groundPos != null) {
			ground = this.level.getBlockState(this.groundPos);
		}
		if (ground != null && ground.getMaterial() != Material.AIR) {
			VoxelShape shape = ground.getCollisionShape(this.level, this.groundPos);
			if (!shape.isEmpty()) {
				for (AxisAlignedBB aabb : shape.toAabbs()) {
					Vector3d pos = this.position();
					if (aabb.contains(pos)) {
						this.inGround = true;
						break;
					}
				}
			}
		}
		if (this.inGround) {
			if (ground == this.lastGround) {
				this.inGroundTime++;
				if (this.inGroundTime >= this.maxTicksInGround()) {
					this.remove();
				}
			} else {
				this.inGround = false;
				this.setDeltaMovement(this.getDeltaMovement().multiply(this.random.nextFloat() * 0.2, this.random.nextFloat() * 0.2, this.random.nextFloat() * 0.2));
				this.inAirTime = 0;
				this.inGroundTime = 0;
			}
		} else {
			this.inGroundTime = 0;
			this.inAirTime++;
			if (this.inAirTime >= this.maxTicksInAir()) {
				this.remove();
				return;
			}
			Vector3d pos = this.position();
			Vector3d velocity = this.getDeltaMovement();
			Vector3d nextPos = pos.add(velocity);
			RayTraceResult rayTrace = this.level.clip(new RayTraceContext(pos, nextPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
			if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
				nextPos = rayTrace.getLocation();
			}
			List<Pair<Double, Entity>> entityHit = new ArrayList<Pair<Double, Entity>>();
			double distance = nextPos.distanceTo(pos);
			for (Entity entity : this.level.getEntities(this, this.getBoundingBox().expandTowards(velocity).inflate(1.0D), this::canHitEntity)) {
				AxisAlignedBB aabb = entity.getBoundingBox().inflate(this.getCollisionWidth() / 2);
				Optional<Vector3d> optional = aabb.clip(pos, nextPos);
				if (optional.isPresent()) {
					double d = pos.distanceTo(optional.get());
					if (d < distance) {
						entityHit.add(Pair.of(d, entity));
					}
				}
			}
			Entity owner = this.getOwner();
			entityHit.removeIf((pair) -> {
				Entity entity = pair.getRight();
				return entity instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity) owner).canHarmPlayer((PlayerEntity) entity);
			});
			if (entityHit.isEmpty()) {
				if (rayTrace != null && rayTrace.getType() == RayTraceResult.Type.BLOCK && !ForgeEventFactory.onProjectileImpact(this, rayTrace)) {
					this.onHitBlock((BlockRayTraceResult) rayTrace);
					this.hasImpulse = true;
				}
				velocity = this.getDeltaMovement();
				this.setPosRaw(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);
			} else {
				entityHit.sort((pairA, pairB) -> pairA.getLeft() > pairB.getLeft() ? 1 : (pairA.getLeft() == pairB.getLeft() ? 0 : -1));
				int size = entityHit.size();
				double usedTick = 0;
				double currentMove = 0;
				double velo = velocity.length();
				double loss = this.getPierceVelocityLoss();
				for (int i = 0; i < size; i++) {
					double p = entityHit.get(i).getLeft();
					double add = (p - currentMove) / velo;
					if (usedTick + add > 1) {
						break;
					}
					Entity entity = entityHit.get(i).getRight();
					this.onHitEntity(new EntityRayTraceResult(entity));
					usedTick += add;
					currentMove = p;
					velo = Math.max(velo - loss, 0);
					this.setDeltaMovement(velocity.normalize().scale(velo));
				}
				currentMove += (1 - usedTick) * velo;
				if (currentMove - distance > -0.05) {
					if (rayTrace != null && rayTrace.getType() == RayTraceResult.Type.BLOCK && !ForgeEventFactory.onProjectileImpact(this, rayTrace)) {
						this.onHitBlock((BlockRayTraceResult) rayTrace);
					}
					velocity = this.getDeltaMovement();
					this.setPosRaw(pos.x + velocity.x, pos.y + velocity.y, pos.z + velocity.z);
				} else {
					pos = pos.add(nextPos.subtract(pos).scale(currentMove / distance));
					this.setPosRaw(pos.x, pos.y, pos.z);
				}
				this.hasImpulse = true;
			}
			velocity = this.getDeltaMovement();
			pos = this.position();
			double vx = velocity.x;
			double vy = velocity.y;
			double vz = velocity.z;
			double vh = Math.sqrt(vx * vx + vz * vz);
			this.xRot = (float) (MathHelper.atan2(vy, vh) * 180 / Math.PI);
			this.yRot = (float) (MathHelper.atan2(vx, vz) * 180 / Math.PI);
			this.xRot = lerpRotation(this.xRotO, this.xRot);
			this.yRot = lerpRotation(this.yRotO, this.yRot);
			double decay = this.isInWater() ? 0.6 : 0.99;
			this.setDeltaMovement(this.getDeltaMovement().scale(decay).add(0, -this.getGravity(), 0));
			this.setPos(pos.x, pos.y, pos.z);
			this.checkInsideBlocks();
		}
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult rayTrace) {
		Entity entity = rayTrace.getEntity();
		int damage = MathHelper.ceil(this.getDeltaMovement().length() * this.getBaseDamage());
		Entity owner = this.getOwner();
		DamageSource damageSource;
		if (owner == null) {
			damageSource = DamageSource.arrow(this, this);
		} else {
			damageSource = DamageSource.arrow(this, owner);
			if (owner instanceof LivingEntity) {
				((LivingEntity) owner).setLastHurtMob(entity);
			}
		}
		if (entity.hurt(damageSource, damage)) {
			if (entity instanceof LivingEntity) {
				LivingEntity living = (LivingEntity) entity;
				if (!this.level.isClientSide && owner instanceof LivingEntity) {
					EnchantmentHelper.doPostHurtEffects(living, owner);
					EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, living);
				}
				this.doPostHurtEffects(living);
			}
			this.playSound(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
		}
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult rayTrace) {
		this.groundPos = rayTrace.getBlockPos();
		this.lastGround = this.level.getBlockState(this.groundPos);
		BlockState state = this.level.getBlockState(rayTrace.getBlockPos());
		state.onProjectileHit(this.level, state, rayTrace, this);
		Vector3d vector3d = rayTrace.getLocation().subtract(this.position());
		this.setDeltaMovement(vector3d);
		Vector3d vector3d1 = vector3d.normalize().scale(0.05);
		this.setPosRaw(this.getX() - vector3d1.x, this.getY() - vector3d1.y, this.getZ() - vector3d1.z);
		this.inGround = true;
	}

	@Override
	public boolean isCritArrow() {
		return false;
	}

	@Override
	public boolean shotFromCrossbow() {
		return false;
	}

	@Override
	public byte getPierceLevel() {
		return 0;
	}

	public int maxTicksInAir() {
		return 1200;
	}

	public double getGravity() {
		return 0.05;
	}

	public double getPierceVelocityLoss() {
		return 5;
	}

	@Override
	public abstract ItemStack getPickupItem();

	public abstract int maxTicksInGround();

	public abstract double getCollisionWidth();
}