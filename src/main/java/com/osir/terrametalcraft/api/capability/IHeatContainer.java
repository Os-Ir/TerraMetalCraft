package com.osir.terrametalcraft.api.capability;

import java.util.Map;

import com.github.zi_jing.cuckoolib.material.type.MaterialBase;

public interface IHeatContainer {
	int addMaterial(MaterialBase material, int unit);

	int removeMaterial(MaterialBase material, int unit);

	Map<MaterialBase, Integer> getMaterials();

	void setMaterials(Map<MaterialBase, Integer> materials);

	double getHeatCapacity();

	void setHeatCapacity(double heatCapacity);

	int getVolume();

	void setVolume(int volume);

	int getUsedVolume();

	void setUsedVolume(int usedVolume);

	default int getMaterialUnit(MaterialBase material) {
		return this.getMaterials().get(material);
	}

	default int getRemainingVolume() {
		return this.getVolume() - this.getUsedVolume();
	}
}