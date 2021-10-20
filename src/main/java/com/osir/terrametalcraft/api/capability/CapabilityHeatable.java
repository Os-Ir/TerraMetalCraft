package com.osir.terrametalcraft.api.capability;

import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.thermo.IPhasePortrait;
import com.osir.terrametalcraft.api.thermo.SolidPhasePortrait;
import com.osir.terrametalcraft.api.thermo.ThermoUtil;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityHeatable implements IHeatable, ICapabilitySerializable<CompoundNBT> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "heatable");

	protected IPhasePortrait portrait;
	protected double energy;

	public CapabilityHeatable() {
		this(new SolidPhasePortrait(1600));
	}

	public CapabilityHeatable(IPhasePortrait portrait) {
		this(portrait, portrait.getEnergy(ThermoUtil.ATMOSPHERIC_PRESSURE, ThermoUtil.AMBIENT_TEMPERATURE));
	}

	public CapabilityHeatable(IPhasePortrait portrait, double energy) {
		this.portrait = portrait;
		this.energy = energy;
	}

	@Override
	public IPhasePortrait getPhasePortrait() {
		return this.portrait;
	}

	@Override
	public double getEnergy() {
		return this.energy;
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = Math.max(energy, 0);
	}

	@Override
	public void increaseEnergy(double energy) {
		this.energy = Math.max(this.energy + energy, 0);
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
		nbt.putDouble("energy", this.energy);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt.contains("energy")) {
			this.energy = nbt.getDouble("energy");
		}
	}
}