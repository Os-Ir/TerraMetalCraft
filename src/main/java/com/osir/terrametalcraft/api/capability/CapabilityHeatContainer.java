package com.osir.terrametalcraft.api.capability;

import java.util.HashMap;
import java.util.Map;

import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.osir.terrametalcraft.Main;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityHeatContainer implements IHeatContainer, ICapabilitySerializable<CompoundNBT> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "heat_container");

	protected Map<MaterialBase, Integer> materials;
	protected double heatCapacity;
	protected int volume, usedVolume;

	public CapabilityHeatContainer() {
		this(0, 0);
	}

	public CapabilityHeatContainer(double baseCapacity, int volume) {
		this.materials = new HashMap<MaterialBase, Integer>();
		this.heatCapacity = baseCapacity;
		this.volume = volume;
		this.usedVolume = 0;
	}

	@Override
	public int addMaterial(MaterialBase material, int unit) {
		if (unit <= 0) {
			return 0;
		}
		int addUnit = Math.min(this.getRemainingVolume(), unit);
		this.heatCapacity += material.getHeatCapacity() * addUnit / 144;
		this.usedVolume += addUnit;
		if (this.materials.containsKey(material)) {
			this.materials.put(material, this.materials.get(material) + addUnit);
		} else {
			this.materials.put(material, addUnit);
		}
		return unit - addUnit;
	}

	@Override
	public int removeMaterial(MaterialBase material, int unit) {
		if (unit <= 0) {
			return 0;
		}
		if (this.materials.containsKey(material)) {
			int removeUnit = Math.min(this.getMaterialUnit(material), unit);
			this.heatCapacity -= material.getHeatCapacity() * removeUnit / 144;
			this.usedVolume -= removeUnit;
			this.materials.put(material, this.materials.get(material) - removeUnit);
			return unit - removeUnit;
		}
		return unit;
	}

	@Override
	public Map<MaterialBase, Integer> getMaterials() {
		return this.materials;
	}

	@Override
	public void setMaterials(Map<MaterialBase, Integer> materials) {
		this.materials = materials;
	}

	@Override
	public double getHeatCapacity() {
		return this.heatCapacity;
	}

	@Override
	public void setHeatCapacity(double heatCapacity) {
		this.heatCapacity = heatCapacity;
	}

	@Override
	public int getVolume() {
		return this.volume;
	}

	@Override
	public void setVolume(int volume) {
		this.volume = volume;
	}

	@Override
	public int getUsedVolume() {
		return this.usedVolume;
	}

	@Override
	public void setUsedVolume(int usedVolume) {
		this.usedVolume = usedVolume;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ModCapabilities.HEAT_CONTAINER) {
			return LazyOptional.of(() -> (T) this);
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putDouble("heatCapacity", this.heatCapacity);
		nbt.putInt("volume", this.volume);
		nbt.putInt("usedVolume", this.usedVolume);
		ListNBT materialsList = new ListNBT();
		this.materials.forEach((material, unit) -> {
			CompoundNBT materialNbt = new CompoundNBT();
			materialNbt.putString("material", material.getName());
			materialNbt.putInt("unit", unit);
			materialsList.add(materialNbt);
		});
		nbt.put("materials", materialsList);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.heatCapacity = nbt.getDouble("heatCapacity");
		this.volume = nbt.getInt("volume");
		this.usedVolume = nbt.getInt("usedVolume");
		this.materials = new HashMap<MaterialBase, Integer>();
		ListNBT materialsList = nbt.getList("materials", 10);
		materialsList.forEach((n) -> {
			CompoundNBT materialNbt = (CompoundNBT) n;
			this.materials.put(MaterialBase.getMaterialByName(materialNbt.getString("material")), materialNbt.getInt("unit"));
		});
	}
}