package com.osir.terrametalcraft.common.block;

import com.github.zi_jing.cuckoolib.gui.ModularGuiInfo;
import com.github.zi_jing.cuckoolib.item.ItemBase;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.common.te.TECampfire;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCampfire extends Block {
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 4, 16);

	public BlockCampfire() {
		super(AbstractBlock.Properties.of(Material.WOOD).strength(0.5f).sound(SoundType.WOOD).noOcclusion());
		this.setRegistryName(Main.MODID, "campfire");
		ModBlocks.REGISTERED_BLOCK.add(this);
		ItemBase.REGISTERED_ITEM.add(new BlockItem(this, new Item.Properties().tab(Main.GROUP_EQUIPMENT))
				.setRegistryName(Main.MODID, "campfire"));
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		TECampfire te = (TECampfire) world.getBlockEntity(pos);
		ModularGuiInfo.openModularGui(te, (ServerPlayerEntity) player);
		return ActionResultType.CONSUME;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TECampfire();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}