package com.osir.terrametalcraft.common.recipe.builder;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.util.ResourceLocation;

public abstract class RecipeBuilderBase<T extends RecipeBuilderBase> {
	protected Advancement.Builder advancement = Advancement.Builder.advancement();

	public T addCriterion(String name, ICriterionInstance criteria) {
		this.advancement.addCriterion(name, criteria);
		return (T) this;
	}

	public abstract void build(Consumer<IFinishedRecipe> out, String id);

	public ResourceLocation buildAdvancement(String modid, String id, String folder) {
		ResourceLocation location = new ResourceLocation(modid, id);
		this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(IRequirementsStrategy.OR);
		return new ResourceLocation(modid, folder + "/" + id);
	}

	protected abstract class FinishRecipeBase implements IFinishedRecipe {
		protected ResourceLocation id, advancementId;

		protected FinishRecipeBase(ResourceLocation id, ResourceLocation advancementId) {
			this.id = id;
			this.advancementId = advancementId;
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public ResourceLocation getAdvancementId() {
			return this.advancementId;
		}

		@Override
		public JsonObject serializeAdvancement() {
			return RecipeBuilderBase.this.advancement.serializeToJson();
		}
	}
}