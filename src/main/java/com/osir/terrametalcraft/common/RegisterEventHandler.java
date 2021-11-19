package com.osir.terrametalcraft.common;

import java.util.ArrayList;
import java.util.List;

import com.github.zi_jing.cuckoolib.data.ModBlockTagsProvider;
import com.github.zi_jing.cuckoolib.material.SolidShape;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.client.renderer.entity.BulletRenderer;
import com.osir.terrametalcraft.client.renderer.entity.JavelinRenderer;
import com.osir.terrametalcraft.common.entity.BulletEntity;
import com.osir.terrametalcraft.common.entity.JavelinEntity;
import com.osir.terrametalcraft.common.item.MaterialItem;
import com.osir.terrametalcraft.common.util.loot.GrassDropSerializer;
import com.osir.terrametalcraft.data.ModItemTagsProvider;
import com.osir.terrametalcraft.data.ModRecipesProvider;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
public class RegisterEventHandler {
	@SubscribeEvent
	public static void registerItem(RegistryEvent.Register<Item> event) {
		SolidShape.REGISTRY.values().forEach((shape) -> {
			MaterialBase.REGISTRY.values().forEach((material) -> {
				if (shape.generateMaterial(material)) {
					new MaterialItem(shape, material);
				}
			});
		});
		List<MaterialItem> list = new ArrayList(MaterialItem.REGISTERED_MATERIAL_ITEM.values());
		list.sort(MaterialItem.COMPARATOR);
		event.getRegistry().registerAll(list.toArray(new MaterialItem[0]));
	}

	@SubscribeEvent
	public static void registerModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
		event.getRegistry().register(new GrassDropSerializer().setRegistryName(Main.MODID, "grass_drop"));
	}

	@SubscribeEvent
	public static void onClientSetUpEvent(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(JavelinEntity.TYPE, (EntityRendererManager manager) -> {
			return new JavelinRenderer(manager);
		});
		RenderingRegistry.registerEntityRenderingHandler(BulletEntity.TYPE, (EntityRendererManager manager) -> {
			return new BulletRenderer(manager);
		});
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		if (event.includeServer()) {
			DataGenerator gen = event.getGenerator();
			ExistingFileHelper helper = event.getExistingFileHelper();
			BlockTagsProvider providerBlock = new ModBlockTagsProvider(gen, Main.MODID, helper);
			gen.addProvider(providerBlock);
			gen.addProvider(new ModItemTagsProvider(gen, providerBlock, Main.MODID, helper));
			gen.addProvider(new ModRecipesProvider(gen));
		}
	}
}