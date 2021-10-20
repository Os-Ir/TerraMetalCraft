package com.osir.terrametalcraft.api.material;

import java.util.List;

import com.github.zi_jing.cuckoolib.material.MaterialEntry;
import com.github.zi_jing.cuckoolib.material.SolidShape;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.osir.terrametalcraft.common.item.MaterialItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;

public class MaterialUtil {
	public static Item getMaterialItem(SolidShape shape, MaterialBase material) {
		MaterialEntry entry = new MaterialEntry(shape, material);
		if (MaterialItem.REGISTERED_MATERIAL_ITEM.containsKey(entry)) {
			return MaterialItem.REGISTERED_MATERIAL_ITEM.get(entry);
		}
		ITag<Item> tag = getMaterialTag(entry);
		if (tag != null) {
			List<Item> list = tag.getValues();
			if (!list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}

	public static ItemStack getMaterialItemStack(SolidShape shape, MaterialBase material) {
		return getMaterialItemStack(shape, material, 1);
	}

	public static ItemStack getMaterialItemStack(SolidShape shape, MaterialBase material, int count) {
		Item item = getMaterialItem(shape, material);
		if (item != null) {
			return new ItemStack(item, count);
		}
		return ItemStack.EMPTY;
	}

	public static ITag<Item> getMaterialTag(SolidShape shape) {
		return getItemTag(new ResourceLocation("forge", shape.getName()));
	}

	public static ITag<Item> getMaterialTag(MaterialEntry entry) {
		return getItemTag(entry.toString());
	}

	public static ITag<Item> getMaterialTag(SolidShape shape, MaterialBase material) {
		return getMaterialTag(new MaterialEntry(shape, material));
	}

	public static ITag<Item> getItemTag(String name) {
		return getItemTag(new ResourceLocation("forge", name));
	}

	public static ITag<Item> getItemTag(ResourceLocation id) {
		return TagCollectionManager.getInstance().getItems().getTag(id);
	}

	public static INamedTag<Item> createForgeItemTag(String name) {
		return ItemTags.createOptional(new ResourceLocation("forge", name));
	}
}