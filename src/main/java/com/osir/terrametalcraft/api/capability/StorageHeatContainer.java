package com.osir.terrametalcraft.api.capability;

import java.util.HashMap;
import java.util.Map;

import com.github.zi_jing.cuckoolib.material.type.MaterialBase;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageHeatContainer implements IStorage<IHeatContainer> {
	@Override
	public INBT writeNBT(Capability<IHeatContainer> capability, IHeatContainer instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putDouble("heatCapacity", instance.getHeatCapacity());
		nbt.putInt("volume", instance.getVolume());
		nbt.putInt("usedVolume", instance.getUsedVolume());
		ListNBT materialsList = new ListNBT();
		instance.getMaterials().forEach((material, unit) -> {
			CompoundNBT materialNbt = new CompoundNBT();
			materialNbt.putString("material", material.getName());
			materialNbt.putInt("unit", unit);
			materialsList.add(materialNbt);
		});
		nbt.put("materials", materialsList);
		return nbt;
	}

	@Override
	public void readNBT(Capability<IHeatContainer> capability, IHeatContainer instance, Direction side, INBT base) {
		CompoundNBT nbt = (CompoundNBT) base;
		instance.setHeatCapacity(nbt.getDouble("heatCapacity"));
		instance.setVolume(nbt.getInt("volume"));
		instance.setUsedVolume(nbt.getInt("usedVolume"));
		Map<MaterialBase, Integer> materials = new HashMap<MaterialBase, Integer>();
		ListNBT materialsList = nbt.getList("materials", 10);
		materialsList.forEach((n) -> {
			CompoundNBT materialNbt = (CompoundNBT) n;
			materials.put(MaterialBase.getMaterialByName(materialNbt.getString("material")), materialNbt.getInt("unit"));
		});
		instance.setMaterials(materials);
	}
}