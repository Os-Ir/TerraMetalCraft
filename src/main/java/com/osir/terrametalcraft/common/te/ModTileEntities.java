package com.osir.terrametalcraft.common.te;

import com.osir.terrametalcraft.Main;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Main.MODID);

	public static final RegistryObject<TileEntityType<?>> _STONE_WORK_TABLE = TILE_ENTITIES.register("stone_work_table", () -> TEStoneWorkTable.TYPE);
	public static final RegistryObject<TileEntityType<?>> _GRINDSTONE = TILE_ENTITIES.register("grindstone", () -> TEGrindstone.TYPE);
	public static final RegistryObject<TileEntityType<?>> _CAMPFIRE = TILE_ENTITIES.register("campfire", () -> TECampfire.TYPE);
	public static final RegistryObject<TileEntityType<?>> _POTTERY_WORK_TABLE = TILE_ENTITIES.register("pottery_work_table", () -> TEPotteryWorkTable.TYPE);
}