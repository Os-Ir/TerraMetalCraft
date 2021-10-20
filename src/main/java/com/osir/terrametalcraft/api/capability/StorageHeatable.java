package com.osir.terrametalcraft.api.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageHeatable implements IStorage<IHeatable> {
	@Override
	public INBT writeNBT(Capability<IHeatable> capability, IHeatable instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putDouble("energy", instance.getEnergy());
		return nbt;
	}

	@Override
	public void readNBT(Capability<IHeatable> capability, IHeatable instance, Direction side, INBT base) {
		CompoundNBT nbt = (CompoundNBT) base;
		if (nbt.contains("energy")) {
			instance.setEnergy(nbt.getDouble("energy"));
		}
	}
}