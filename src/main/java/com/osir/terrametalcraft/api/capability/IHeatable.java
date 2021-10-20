package com.osir.terrametalcraft.api.capability;

import com.osir.terrametalcraft.api.thermo.IPhasePortrait;
import com.osir.terrametalcraft.api.thermo.ThermoUtil;

public interface IHeatable {
	IPhasePortrait getPhasePortrait();

	double getEnergy();

	void setEnergy(double energy);

	void increaseEnergy(double energy);

	default double getTemperature() {
		return this.getTemperature(ThermoUtil.ATMOSPHERIC_PRESSURE);
	}

	default double getTemperature(float pressure) {
		return this.getPhasePortrait().getTemperature(pressure, this.getEnergy());
	}
}