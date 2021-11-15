package com.osir.terrametalcraft.api.thermo.impl;

import com.osir.terrametalcraft.api.thermo.IPhasePortrait;
import com.osir.terrametalcraft.api.thermo.Phase;

import net.minecraft.item.ItemStack;

public class RecipePhasePortrait implements IPhasePortrait {
	protected double capacity;
	protected double transitionTemp;
	protected ItemStack result;

	public RecipePhasePortrait(double capacity, double transitionTemp, ItemStack result) {
		this.capacity = capacity;
		this.transitionTemp = transitionTemp;
		this.result = result;
	}

	@Override
	public double getTemperature(double pressure, double energy) {
		return Math.min(energy / this.capacity, this.transitionTemp);
	}

	@Override
	public double getEnergy(double pressure, double temperature) {
		return Math.min(temperature, this.transitionTemp) * this.capacity;
	}

	@Override
	public Phase getPhase(double pressure, double energy) {
		return Phase.SOLID;
	}

	public boolean complete(double energy) {
		return energy / this.capacity >= this.transitionTemp * 1.2;
	}

	public ItemStack copyResult() {
		return this.result.copy();
	}
}