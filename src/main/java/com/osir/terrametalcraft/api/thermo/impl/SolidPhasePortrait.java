package com.osir.terrametalcraft.api.thermo.impl;

import com.osir.terrametalcraft.api.thermo.IPhasePortrait;
import com.osir.terrametalcraft.api.thermo.Phase;

public class SolidPhasePortrait implements IPhasePortrait {
	protected float capacity;

	public SolidPhasePortrait(float capacity) {
		this.capacity = capacity;
	}

	@Override
	public double getTemperature(float pressure, double energy) {
		return energy / this.capacity;
	}

	@Override
	public double getEnergy(float pressure, double temperature) {
		return temperature * this.capacity;
	}

	@Override
	public Phase getPhase(float pressure, double energy) {
		return Phase.SOLID;
	}
}