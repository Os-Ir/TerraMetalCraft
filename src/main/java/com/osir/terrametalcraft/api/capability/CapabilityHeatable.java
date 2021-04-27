package com.osir.terrametalcraft.api.capability;

import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.thermo.PhasePortrait;
import com.osir.terrametalcraft.api.thermo.ThermoUtil;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityHeatable implements IHeatable, ICapabilitySerializable<CompoundNBT> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "heatable");

	protected PhasePortrait portrait;
	protected float moleNumber, energy;

	public CapabilityHeatable() {
		this(PhasePortrait.SIMPLE_SOLID);
	}

	public CapabilityHeatable(PhasePortrait portrait) {
		this(portrait, 1);
	}

	public CapabilityHeatable(PhasePortrait portrait, float moleNumber) {
		this(portrait, moleNumber,
				moleNumber * portrait.getMoleEnergy(ThermoUtil.ATMOSPHERIC_PRESSURE, ThermoUtil.AMBIENT_TEMPERATURE));
	}

	public CapabilityHeatable(PhasePortrait portrait, float moleNumber, float energy) {
		this.portrait = portrait;
		this.moleNumber = moleNumber;
		this.energy = energy;
	}

	@Override
	public PhasePortrait getPhasePortrait() {
		return this.portrait;
	}

	@Override
	public float getMoleNumber() {
		return this.moleNumber;
	}

	@Override
	public void setMoleNumber(float moleNumber) {
		this.moleNumber = moleNumber;
	}

	@Override
	public float getEnergy() {
		return this.energy;
	}

	@Override
	public void setEnergy(float energy) {
		this.energy = energy;
	}

	@Override
	public void increaseEnergy(float energy) {
		this.energy += energy;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ModCapabilities.HEATABLE) {
			return LazyOptional.of(() -> (T) this);
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("moleNumber", this.moleNumber);
		nbt.putFloat("energy", this.energy);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt.contains("moleNumber")) {
			this.moleNumber = nbt.getFloat("moleNumber");
		}
		if (nbt.contains("energy")) {
			this.energy = nbt.getFloat("energy");
		}
	}
}