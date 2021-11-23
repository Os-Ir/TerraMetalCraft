package com.osir.terrametalcraft.api.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StoragePottery implements IStorage<IPottery> {
	@Override
	public INBT writeNBT(Capability<IPottery> capability, IPottery instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putIntArray("data", instance.getAllData());
		return nbt;
	}

	@Override
	public void readNBT(Capability<IPottery> capability, IPottery instance, Direction side, INBT base) {
		CompoundNBT nbt = (CompoundNBT) base;
		instance.setAllData(nbt.getIntArray("data"));
	}
}