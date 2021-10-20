package com.osir.terrametalcraft.common.util.loot;

import com.google.gson.JsonObject;

import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;

public class GrassDropSerializer extends GlobalLootModifierSerializer<GrassDropModifier> {
	@Override
	public GrassDropModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
		return new GrassDropModifier(ailootcondition);
	}

	@Override
	public JsonObject write(GrassDropModifier instance) {
		return new JsonObject();
	}
}