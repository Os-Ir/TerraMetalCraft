package com.osir.terrametalcraft.common.item;

import com.osir.terrametalcraft.Main;

import net.minecraft.item.Item;

public class ItemBase extends Item {
	private String name;

	public ItemBase(String name) {
		this(name, new Properties());
	}

	public ItemBase(String name, Properties properties) {
		super(properties.group(Main.GROUP_ITEM));
		this.name = name;
		this.setRegistryName(Main.MODID, name);
		ModItems.REGISTERED_ITEM.add(this);
	}
}