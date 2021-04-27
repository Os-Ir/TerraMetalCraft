package com.osir.terrametalcraft.api.thermo;

import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.github.zi_jing.cuckoolib.util.data.UnorderedRegistry;

public class ThermoUtil {
	public static final UnorderedRegistry<MaterialBase, PhasePortrait> REGISTRY_MATERIAL_PORTRAIT = new UnorderedRegistry<MaterialBase, PhasePortrait>();

	public static final float UNIVERSAL_GAS_CONSTANT = 8.31451f;

	public static final float ATMOSPHERIC_PRESSURE = 101300;
	public static final float AMBIENT_TEMPERATURE = 293.15f;
	public static final float CELSIUS_ZERO = 273.15f;
}