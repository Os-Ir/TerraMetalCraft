package com.osir.terrametalcraft.common.item;

import com.github.zi_jing.cuckoolib.item.ItemBase;
import com.osir.terrametalcraft.Main;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ItemGun extends ItemBase {
	public ItemGun() {
		super(Main.MODID, "gun", Main.GROUP_ITEM);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		Vector3d vec = player.getLookAngle();
		BulletEntity bullet = new BulletEntity(world, player);
		bullet.shoot(vec.x, vec.y, vec.z, 10, 0);
		world.addFreshEntity(bullet);
		return ActionResult.success(player.getItemInHand(hand));
	}

	public static class BulletEntity extends ArrowEntity {
		public BulletEntity(World world, LivingEntity shooter) {
			super(world, shooter);
			this.setBaseDamage(10);
		}

		public void tick() {
			super.tick();
			if (!this.level.isClientSide) {
				if (this.inGround || this.tickCount > 40) {
					this.remove();
				}
			}
		}
	}
}