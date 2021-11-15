package com.osir.terrametalcraft.common.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BulletEntity extends ProjectileEntityBase {
	public static final EntityType<BulletEntity> TYPE = EntityType.Builder.<BulletEntity>of(BulletEntity::new, EntityClassification.MISC).sized(0.125f, 0.125F).build("bullet");

	public BulletEntity(EntityType<BulletEntity> type, World world) {
		super(type, world);
	}

	public BulletEntity(World world, double x, double y, double z) {
		super(TYPE, world);
		this.setPos(x, y, z);
		this.setBaseDamage(2);
	}

	public BulletEntity(World world, LivingEntity entity) {
		this(world, entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
		this.setOwner(entity);
	}

	@Override
	public ItemStack getPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public int maxTicksInGround() {
		return 0;
	}

	@Override
	public double getCollisionWidth() {
		return 0.125;
	}

	@Override
	public void defineSynchedData() {

	}

	@Override
	public void readAdditionalSaveData(CompoundNBT nbt) {

	}

	@Override
	public void addAdditionalSaveData(CompoundNBT nbt) {

	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}