package com.osir.terrametalcraft.api.thermo.impl;

import com.osir.terrametalcraft.api.thermo.IPhasePortrait;
import com.osir.terrametalcraft.api.thermo.Phase;

public class SolidPhasePortrait implements IPhasePortrait {
	protected double capacity;

	public SolidPhasePortrait(double capacity) {
		this.capacity = capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getCapacity() {
		return this.capacity;
	}

	@Override
	public double getTemperature(double pressure, double energy) {
		return energy / this.capacity;
	}

	@Override
	public double getEnergy(double pressure, double temperature) {
		return temperature * this.capacity;
	}

	@Override
	public Phase getPhase(double pressure, double energy) {
		return Phase.SOLID;
	}
}