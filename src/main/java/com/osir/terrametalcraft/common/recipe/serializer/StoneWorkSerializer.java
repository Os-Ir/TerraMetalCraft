package com.osir.terrametalcraft.common.recipe.serializer;

import com.github.zi_jing.cuckoolib.recipe.data.OutputItem;
import com.google.gson.JsonObject;
import com.osir.terrametalcraft.common.recipe.StoneWorkRecipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class StoneWorkSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<StoneWorkRecipe> {
	@Override
	public StoneWorkRecipe fromJson(ResourceLocation id, JsonObject json) {
		return new StoneWorkRecipe(id, Ingredient.fromJson(json.get("input")), OutputItem.fromJson(json.get("output")), json.get("data").getAsLong());
	}

	@Override
	public StoneWorkRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
		return new StoneWorkRecipe(id, Ingredient.fromNetwork(buffer), OutputItem.fromPacketBuffer(buffer), buffer.readLong());
	}

	@Override
	public void toNetwork(PacketBuffer buffer, StoneWorkRecipe recipe) {
		recipe.input.toNetwork(buffer);
		recipe.output.writeToPacketBuffer(buffer);
		buffer.writeLong(recipe.data);
	}
}