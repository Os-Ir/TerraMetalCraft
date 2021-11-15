package com.osir.terrametalcraft.common.item;

import com.github.zi_jing.cuckoolib.item.ItemBase;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.entity.BulletEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ItemGun extends ItemBase {
	public ItemGun() {
		super(Main.GROUP_ITEM);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (world.isClientSide) {
			return ActionResult.success(player.getItemInHand(hand));
		}
		Vector3d vec = player.getLookAngle();
		BulletEntity bullet = new BulletEntity(world, player);
		bullet.shoot(vec.x, vec.y, vec.z, 20, 0);
		world.addFreshEntity(bullet);
		return ActionResult.success(player.getItemInHand(hand));
	}
}