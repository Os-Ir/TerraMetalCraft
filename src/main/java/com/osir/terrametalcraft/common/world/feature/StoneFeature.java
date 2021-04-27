package com.osir.terrametalcraft.common.world.feature;

import java.util.Random;

import com.mojang.serialization.Codec;
import com.osir.terrametalcraft.common.block.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class StoneFeature extends Feature<NoFeatureConfig> {
	public StoneFeature(Codec codec) {
		super(codec);
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos,
			NoFeatureConfig config) {
		BlockState stateStone = ModBlocks.coverItemStone.getDefaultState();
		BlockState stateFlint = ModBlocks.coverItemFlint.getDefaultState();
		BlockState stateStick = ModBlocks.coverItemStick.getDefaultState();
		int num = rand.nextInt(3) + 3;
		while (num-- > 0) {
			int dx = rand.nextInt(16);
			int dz = rand.nextInt(16);
			BlockPos genPos = reader.getHeight(Heightmap.Type.WORLD_SURFACE, pos.add(dx, 0, dz));
			if (this.validate(reader, genPos)) {
				switch (rand.nextInt(3)) {
				case 0:
					reader.setBlockState(genPos, stateStone, 2);
					break;
				case 1:
					reader.setBlockState(genPos, stateFlint, 2);
					break;
				case 2:
					reader.setBlockState(genPos, stateStick, 2);
					break;
				default:
				}
			}
		}
		return true;
	}

	private boolean validate(ISeedReader reader, BlockPos pos) {
		Block block = reader.getBlockState(pos).getBlock();
		Material materialDown = reader.getBlockState(pos.down()).getMaterial();
		boolean flag2 = block == Blocks.GRASS || block == Blocks.STONE || block == Blocks.SAND || block == Blocks.DIRT;
		return reader.getBlockState(pos.down()).isOpaqueCube(reader, pos)
				&& (reader.isAirBlock(pos) || block == Blocks.SNOW || block == Blocks.GRASS
						|| block == Blocks.TALL_GRASS)
				&& (materialDown == Material.ROCK || materialDown == Material.SAND || materialDown == Material.EARTH
						|| materialDown == Material.ORGANIC);
	}
}