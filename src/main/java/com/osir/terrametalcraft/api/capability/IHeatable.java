package com.osir.terrametalcraft.api.capability;

import com.osir.terrametalcraft.api.thermo.PhasePortrait;

public interface IHeatable {
	PhasePortrait getPhasePortrait();

	float getMoleNumber();

	void setMoleNumber(float moleNumber);

	float getEnergy();

	void setEnergy(float energy);

	void increaseEnergy(float energy);

	default float getTemperature(float pressure) {
		return this.getPhasePortrait().getTemperature(pressure, this.getEnergy() / this.getMoleNumber());
	}
}