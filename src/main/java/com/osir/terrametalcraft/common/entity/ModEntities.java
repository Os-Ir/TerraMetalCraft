package com.osir.terrametalcraft.common.entity;

import com.osir.terrametalcraft.Main;

import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Main.MODID);

	public static final RegistryObject<EntityType<JavelinEntity>> _JAVELIN = ENTITY_TYPES.register("javelin", () -> JavelinEntity.TYPE);
	public static final RegistryObject<EntityType<BulletEntity>> _BULLET = ENTITY_TYPES.register("bullet", () -> BulletEntity.TYPE);
}