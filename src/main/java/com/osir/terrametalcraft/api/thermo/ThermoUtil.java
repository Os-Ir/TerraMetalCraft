package com.osir.terrametalcraft.api.thermo;

import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.github.zi_jing.cuckoolib.util.data.UnorderedRegistry;
import com.osir.terrametalcraft.api.capability.IHeatable;

public class ThermoUtil {
	public static final UnorderedRegistry<MaterialBase, PhasePortrait> REGISTRY_MATERIAL_PORTRAIT = new UnorderedRegistry<MaterialBase, PhasePortrait>();

	public static final double UNIVERSAL_GAS_CONSTANT = 8.31451;

	public static final double ATMOSPHERIC_PRESSURE = 101300;
	public static final double AMBIENT_TEMPERATURE = 290;
	public static final double CELSIUS_ZERO = 273.15f;

	public static void heatExchange(IHeatable cap, double temperature, float resistance) {
		if (cap == null) {
			return;
		}
		double exchange = (cap.getTemperature() - temperature) / resistance;
		cap.increaseEnergy(-exchange);
	}

	public static void heatExchange(IHeatable capA, IHeatable capB, float resistance) {
		if (capA == null || capB == null) {
			return;
		}
		double exchange = (capA.getTemperature() - capB.getTemperature()) / resistance;
		capA.increaseEnergy(-exchange);
		capB.increaseEnergy(exchange);
	}
}