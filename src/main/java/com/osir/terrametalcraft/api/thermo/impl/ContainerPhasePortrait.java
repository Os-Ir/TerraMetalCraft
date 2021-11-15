package com.osir.terrametalcraft.api.thermo.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.osir.terrametalcraft.api.thermo.IPhasePortrait;
import com.osir.terrametalcraft.api.thermo.Phase;

public class ContainerPhasePortrait implements IPhasePortrait {
	protected Map<MaterialBase, Integer> materials;
	private double capacity;

	public ContainerPhasePortrait() {
		this.materials = new HashMap<MaterialBase, Integer>();
		this.capacity = 0;
	}

	public void addMaterial(MaterialBase material, Integer unit) {
		this.capacity += material.getHeatCapacity() * unit / 144;
		if (this.materials.containsKey(material)) {
			this.materials.put(material, this.materials.get(material) + unit);
		} else {
			this.materials.put(material, unit);
		}
	}

	@Override
	public double getTemperature(double pressure, double energy) {
		return energy / this.capacity;
	}

	@Override
	public double getEnergy(double pressure, double temperature) {
		return this.capacity * temperature;
	}

	@Override
	public Phase getPhase(double pressure, double energy) {
		return Phase.SOLID;
	}
}