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

public class CapabilityCarving implements ICarving, ICapabilitySerializable<CompoundNBT> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "carving");

	protected long[] data;

	public CapabilityCarving() {
		this.data = new long[49];
	}

	@Override
	public long getCarveData(int index) {
		return this.data[index];
	}

	@Override
	public long[] getAllCarveData() {
		return this.data;
	}

	@Override
	public void setCarveData(int index, long data) {
		this.data[index] = data;
	}

	@Override
	public void setAllCarveData(long[] data) {
		if (data.length != 49) {
			throw new IllegalArgumentException("Carving data length " + data.length + " is not 49");
		}
		for (int i = 0; i < 49; i++) {
			this.data[i] = data[i];
		}
	}

	@Override
	public boolean isAreaValid(int index, float require, float tolerance) {
		return MathUtil.between(((float) MathUtil.getTrueBits(this.data[index])) / 64, require - tolerance,
				require + tolerance);
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < 49; i++) {
			if (this.data[i] != 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return LazyOptional.of(() -> (T) this);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putLongArray("data", Arrays.copyOf(this.data, 49));
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt.contains("data")) {
			this.data = Arrays.copyOf(nbt.getLongArray("data"), 49);
		}
	}
}