package com.osir.terrametalcraft.api.thermo;

public interface IPhasePortrait {
	double getTemperature(float pressure, double energy);

	double getEnergy(float pressure, double temperature);

	Phase getPhase(float pressure, double energy);
}