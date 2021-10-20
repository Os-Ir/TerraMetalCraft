package com.osir.terrametalcraft.common.item;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.github.zi_jing.cuckoolib.CuckooLib;
import com.github.zi_jing.cuckoolib.item.ItemBase;
import com.github.zi_jing.cuckoolib.material.MaterialEntry;
import com.github.zi_jing.cuckoolib.material.SolidShape;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.osir.terrametalcraft.Main;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class MaterialItem extends ItemBase {
	public static final Map<MaterialEntry, MaterialItem> REGISTERED_MATERIAL_ITEM = new HashMap<MaterialEntry, MaterialItem>();

	public static final Comparator<MaterialItem> COMPARATOR = (itemA, itemB) -> {
		String shapeA = itemA.getShape().getName();
		String shapeB = itemB.getShape().getName();
		String materialA = itemA.getMaterial().getName();
		String materialB = itemB.getMaterial().getName();
		return shapeA.equals(shapeB) ? materialA.compareTo(materialB) : shapeA.compareTo(shapeB);
	};

	protected SolidShape shape;
	protected MaterialBase material;

	public MaterialItem(SolidShape shape, MaterialBase material) {
		super(Main.MODID, "material_item." + shape.getName() + "." + material.getName(), new Properties(),
				CuckooLib.GROUP_MATERIAL, false);
		this.shape = shape;
		this.material = material;
		REGISTERED_MATERIAL_ITEM.put(new MaterialEntry(shape, material), this);
	}

	public static SolidShape getItemShape(ItemStack stack) {
		return ((MaterialItem) stack.getItem()).shape;
	}

	public static MaterialBase getItemMaterial(ItemStack stack) {
		return ((MaterialItem) stack.getItem()).material;
	}

	public SolidShape getShape() {
		return this.shape;
	}

	public MaterialBase getMaterial() {
		return this.material;
	}

	public ItemStack createItemStack() {
		return this.createItemStack(1);
	}

	public ItemStack createItemStack(int count) {
		return new ItemStack(this, count);
	}

	public int getItemColor(ItemStack stack, int tintIndex) {
		MaterialBase material = getItemMaterial(stack);
		if (material != null) {
			return material.getColor();
		}
		return 0xffffff;
	}

	@Override
	public ITextComponent getName(ItemStack stack) {
		SolidShape shape = getItemShape(stack);
		MaterialBase material = getItemMaterial(stack);
		if (shape != null && material != null) {
			return new StringTextComponent(getItemShape(stack).getLocalizedname(getItemMaterial(stack)));
		}
		return StringTextComponent.EMPTY;
	}
}