package com.osir.terrametalcraft.api.capability;

import java.util.Arrays;

import com.github.zi_jing.cuckoolib.util.math.MathUtil;
import com.osir.terrametalcraft.Main;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityPottery implements IPottery, ICapabilitySerializable<CompoundNBT> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "pottery");

	protected int[] data;

	public CapabilityPottery() {
		this.data = new int[8];
	}

	@Override
	public int getData(int index) {
		return this.data[index];
	}

	@Override
	public int[] getAllData() {
		return this.data;
	}

	@Override
	public void setData(int index, int data) {
		this.data[index] = data;
	}

	@Override
	public void setAllData(int[] data) {
		if (data.length != 8) {
			throw new IllegalArgumentException("Pottery data length " + data.length + " is not 8");
		}
		for (int i = 0; i < 8; i++) {
			this.data[i] = data[i];
		}
	}

	@Override
	public void potteryWork(int index, int count) {
		this.data[index] = Math.min(50, this.data[index] + count);
	}

	@Override
	public boolean isAreaValid(int index, int min, int max) {
		return MathUtil.between(this.data[index], min, max);
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < 8; i++) {
			if (this.data[i] != 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ModCapabilities.POTTERY) {
			return LazyOptional.of(() -> (T) this);
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putIntArray("data", Arrays.copyOf(this.data, 8));
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.data = Arrays.copyOf(nbt.getIntArray("data"), 8);
	}
}