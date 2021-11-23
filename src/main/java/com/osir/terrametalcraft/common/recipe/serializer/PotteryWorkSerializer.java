package com.osir.terrametalcraft.common.recipe.serializer;

import com.github.zi_jing.cuckoolib.recipe.data.OutputItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.osir.terrametalcraft.common.recipe.PotteryWorkRecipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PotteryWorkSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PotteryWorkRecipe> {
	@Override
	public PotteryWorkRecipe fromJson(ResourceLocation id, JsonObject json) {
		JsonArray array = json.getAsJsonArray("data");
		int[] data = new int[array.size()];
		for (int i = 0; i < data.length; i++) {
			data[i] = array.get(i).getAsInt();
		}
		return new PotteryWorkRecipe(id, Ingredient.fromJson(json.get("input")), OutputItem.fromJson(json.get("output")), data);
	}

	@Override
	public PotteryWorkRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
		Ingredient input = Ingredient.fromNetwork(buffer);
		OutputItem output = OutputItem.fromPacketBuffer(buffer);
		int[] data = new int[8];
		for (int i = 0; i < data.length; i++) {
			data[i] = buffer.readInt();
		}
		return new PotteryWorkRecipe(id, input, output, data);
	}

	@Override
	public void toNetwork(PacketBuffer buffer, PotteryWorkRecipe recipe) {
		recipe.input.toNetwork(buffer);
		recipe.output.writeToPacketBuffer(buffer);
		for (int d : recipe.data) {
			buffer.writeInt(d);
		}
	}
}