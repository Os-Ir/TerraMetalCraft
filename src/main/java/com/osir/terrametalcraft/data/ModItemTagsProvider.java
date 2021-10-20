package com.osir.terrametalcraft.data;

import com.github.zi_jing.cuckoolib.material.MaterialEntry;
import com.github.zi_jing.cuckoolib.material.SolidShape;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.osir.terrametalcraft.api.material.MaterialUtil;
import com.osir.terrametalcraft.common.item.MaterialItem;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {
	public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId,
			ExistingFileHelper existingFileHelper) {
		super(dataGenerator, blockTagProvider, modId, existingFileHelper);
	}

	@Override
	protected void addTags() {
		SolidShape.REGISTRY.values().forEach((shape) -> {
			INamedTag<Item> tagShape = MaterialUtil.createForgeItemTag(shape.getName());
			TagsProvider.Builder<Item> shapeBuilder = this.tag(tagShape);
			MaterialBase.REGISTRY.values().forEach((material) -> {
				MaterialEntry entry = new MaterialEntry(shape, material);
				INamedTag<Item> tagEntry = MaterialUtil.createForgeItemTag(entry.toString());
				if (MaterialItem.REGISTERED_MATERIAL_ITEM.containsKey(entry)) {
					this.tag(tagEntry).add(MaterialItem.REGISTERED_MATERIAL_ITEM.get(entry));
					shapeBuilder.addTag(tagEntry);
				}
			});
		});
	}
}