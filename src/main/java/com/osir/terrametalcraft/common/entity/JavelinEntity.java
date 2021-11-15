package com.osir.terrametalcraft.common.entity;

import com.github.zi_jing.cuckoolib.item.ToolItem;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class JavelinEntity extends ProjectileEntityBase {
	public static final EntityType<JavelinEntity> TYPE = EntityType.Builder.<JavelinEntity>of(JavelinEntity::new, EntityClassification.MISC).sized(0.125f, 0.125F).build("javelin");

	protected ItemStack stack;

	public JavelinEntity(EntityType<JavelinEntity> entityType, World world) {
		super(entityType, world);
		this.stack = ItemStack.EMPTY;
	}

	public JavelinEntity(World world, ItemStack stack, double x, double y, double z) {
		super(TYPE, world);
		this.setPos(x, y, z);
		this.setBaseDamage(4);
		this.stack = stack;
	}

	public JavelinEntity(World world, ItemStack stack, LivingEntity entity) {
		this(world, stack, entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
		this.setOwner(entity);
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult rayTrace) {
		super.onHitEntity(rayTrace);
		int damage = MathHelper.ceil(this.getDeltaMovement().length() * this.getBaseDamage());
		Item item = this.stack.getItem();
		if (item instanceof ToolItem) {
			((ToolItem) item).damageItem(this.stack, damage * 100);
		}
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult rayTrace) {
		super.onHitBlock(rayTrace);
		Item item = this.stack.getItem();
		if (item instanceof ToolItem) {
			((ToolItem) item).damageItem(this.stack, 100);
		}
	}

	@Override
	public ItemStack getPickupItem() {
		return this.stack;
	}

	@Override
	public int maxTicksInGround() {
		return 1200;
	}

	@Override
	public double getCollisionWidth() {
		return 0.375;
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