package com.osir.terrametalcraft.api.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ModCapabilities {
	@CapabilityInject(ICarving.class)
	public static final Capability<ICarving> CARVING = null;

	@CapabilityInject(IHeatable.class)
	public static final Capability<IHeatable> HEATABLE = null;

	@CapabilityInject(IHeatContainer.class)
	public static final Capability<IHeatContainer> HEAT_CONTAINER = null;

	@CapabilityInject(IPottery.class)
	public static final Capability<IPottery> POTTERY = null;
}