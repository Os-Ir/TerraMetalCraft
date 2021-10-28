package com.osir.terrametalcraft.api.material;

import java.util.HashMap;
import java.util.Map;

import com.github.zi_jing.cuckoolib.material.type.MaterialBase;

public class AlloyInfo {
	private MaterialBase alloyMaterial;
	private Map<MaterialBase, Integer> component;

	public AlloyInfo(MaterialBase alloyMateiral, Map<MaterialBase, Integer> component) {
		this.alloyMaterial = alloyMateiral;
		if (component.isEmpty()) {
			throw new IllegalArgumentException("Alloy components can't be empty");
		}
		this.component = component;
	}

	public static class Builder {
		private MaterialBase alloyMaterial;
		private Map<MaterialBase, Integer> component;

		public Builder(MaterialBase alloyMaterial) {
			this.alloyMaterial = alloyMaterial;
			this.component = new HashMap<MaterialBase, Integer>();
		}

		public Builder addComponent(MaterialBase material, int weight) {
			this.component.put(material, weight);
			return this;
		}

		public AlloyInfo build() {
			return new AlloyInfo(this.alloyMaterial, this.component);
		}
	}
}