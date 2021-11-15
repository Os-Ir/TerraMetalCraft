package com.osir.terrametalcraft.api.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageCarving implements IStorage<ICarving> {
	@Override
	public INBT writeNBT(Capability<ICarving> capability, ICarving instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putLongArray("data", instance.getAllCarveData());
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICarving> capability, ICarving instance, Direction side, INBT base) {
		CompoundNBT nbt = (CompoundNBT) base;
		instance.setAllCarveData(nbt.getLongArray("data"));
	}
}