package com.osir.terrametalcraft.api;

import com.github.zi_jing.cuckoolib.tool.IToolInfo;
import com.github.zi_jing.cuckoolib.util.data.UnorderedRegistry;

import net.minecraft.util.ResourceLocation;

public class ModRegistries {
	public static final UnorderedRegistry<ResourceLocation, IToolInfo> REGISTRY_TOOL = new UnorderedRegistry<ResourceLocation, IToolInfo>();
}