package com.osir.terrametalcraft.api.thermo;

public interface IPhasePortrait {
	double getTemperature(double pressure, double energy);

	double getEnergy(double pressure, double temperature);

	Phase getPhase(double pressure, double energy);
}