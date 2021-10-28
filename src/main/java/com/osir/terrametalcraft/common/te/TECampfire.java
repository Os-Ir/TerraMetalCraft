package com.osir.terrametalcraft.common.te;

import com.github.zi_jing.cuckoolib.LibRegistryHandler;
import com.github.zi_jing.cuckoolib.client.render.TextureArea;
import com.github.zi_jing.cuckoolib.gui.IGuiHolderCodec;
import com.github.zi_jing.cuckoolib.gui.IModularGuiHolder;
import com.github.zi_jing.cuckoolib.gui.ModularGuiInfo;
import com.github.zi_jing.cuckoolib.gui.impl.TileEntityCodec;
import com.github.zi_jing.cuckoolib.gui.widget.MoveType;
import com.github.zi_jing.cuckoolib.gui.widget.PointerWidget;
import com.github.zi_jing.cuckoolib.gui.widget.ProgressWidget;
import com.github.zi_jing.cuckoolib.gui.widget.SlotWidget;
import com.github.zi_jing.cuckoolib.item.ItemFuelEntry;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.capability.CapabilityHeatable;
import com.osir.terrametalcraft.api.capability.IHeatable;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.api.te.SyncedTE;
import com.osir.terrametalcraft.api.thermo.IPhasePortrait;
import com.osir.terrametalcraft.api.thermo.ThermoUtil;
import com.osir.terrametalcraft.api.thermo.impl.RecipePhasePortrait;
import com.osir.terrametalcraft.api.thermo.impl.SolidPhasePortrait;
import com.osir.terrametalcraft.common.block.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class TECampfire extends SyncedTE implements ITickableTileEntity, IModularGuiHolder {
	public static final TileEntityType<TECampfire> TYPE = TileEntityType.Builder.of(() -> new TECampfire(), ModBlocks.CAMPFIRE).build(null);

	private static final TextureArea BACKGROUND = TextureArea.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/campfire/background.png"));
	private static final TextureArea OVERLAY_FIRE = TextureArea.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/campfire/overlay_fire.png"));
	private static final TextureArea POINTER = TextureArea.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/campfire/pointer.png"));
	private static final ITextComponent TITLE = new TranslationTextComponent("modulargui.campfire.name");

	private static final float COOLING_RESISTANCE = 0.5f;
	private static final float RESISTANCE_H = 0.15f;
	private static final float RESISTANCE_L = 0.3f;
	private static final float POWER = 2000;

	protected ItemStackHandler inventory;
	protected IHeatable heat;
	protected int burnTime;

	public TECampfire() {
		super(TYPE);
		this.inventory = new ItemStackHandler(5) {
			@Override
			public int getSlotLimit(int slot) {
				if (slot > 0) {
					return 1;
				}
				return 64;
			}
		};
		this.heat = new CapabilityHeatable(new SolidPhasePortrait(1600));
	}

	@Override
	public void tick() {
		if (this.getTileEntity().getLevel().isClientSide) {
			return;
		}
		ThermoUtil.heatExchange(this.heat, ThermoUtil.AMBIENT_TEMPERATURE, COOLING_RESISTANCE);
		if (this.burnTime > 0) {
			this.burnTime--;
			this.heat.increaseEnergy(POWER);
		} else {
			ItemStack fuelStack = this.inventory.getStackInSlot(0);
			Item fuelItem = fuelStack.getItem();
			if (LibRegistryHandler.ITEM_FUEL_REGISTRY.containsKey(fuelItem)) {
				ItemFuelEntry entry = LibRegistryHandler.ITEM_FUEL_REGISTRY.get(fuelItem);
				this.burnTime = (int) (entry.getFuelMass() * entry.getFuleInfo().getCalorificValue() / POWER);
				fuelStack.shrink(1);
			}
		}
		for (int i = 1; i <= 3; i++) {
			LazyOptional<IHeatable> optional = this.inventory.getStackInSlot(i).getCapability(ModCapabilities.HEATABLE, null);
			if (optional.isPresent()) {
				IHeatable cap = optional.orElse(null);
				ThermoUtil.heatExchange(this.heat, cap, RESISTANCE_L);
				this.checkHeatRecipe(cap, i);
			}
		}
		LazyOptional<IHeatable> optional = this.inventory.getStackInSlot(4).getCapability(ModCapabilities.HEATABLE, null);
		if (optional.isPresent()) {
			IHeatable cap = optional.orElse(null);
			ThermoUtil.heatExchange(this.heat, cap, RESISTANCE_H);
			this.checkHeatRecipe(cap, 4);
		}
		if (this.level != null) {
			this.level.blockEntityChanged(this.getBlockPos(), this);
		}
	}

	private void checkHeatRecipe(IHeatable cap, int slot) {
		IPhasePortrait p = cap.getPhasePortrait();
		if (p instanceof RecipePhasePortrait) {
			RecipePhasePortrait portrait = (RecipePhasePortrait) p;
			if (portrait.complete(cap.getEnergy())) {
				this.inventory.setStackInSlot(slot, portrait.copyResult());
			}
		}
	}

	public float getBurnTimeProgress() {
		return this.burnTime / 1600f;
	}

	public int getTemperaturePointer() {
		return MathHelper.clamp((int) ((this.heat.getTemperature() - ThermoUtil.AMBIENT_TEMPERATURE) / 20), 1, 75);
	}

	@Override
	public IGuiHolderCodec getCodec() {
		return TileEntityCodec.INSTANCE;
	}

	@Override
	public ITextComponent getTitle(PlayerEntity player) {
		return TITLE;
	}

	@Override
	public ModularGuiInfo createGuiInfo(PlayerEntity player) {
		return ModularGuiInfo.builder().setBackground(BACKGROUND).addPlayerInventory(player.inventory).addWidget(0, new SlotWidget(8, 63, this.inventory, 0)).addWidget(1, new SlotWidget(53, 10, this.inventory, 1))
				.addWidget(2, new SlotWidget(80, 10, this.inventory, 2)).addWidget(3, new SlotWidget(107, 10, this.inventory, 3)).addWidget(4, new SlotWidget(80, 37, this.inventory, 4))
				.addWidget(5, new ProgressWidget(30, 64, 14, 14, this::getBurnTimeProgress, MoveType.VERTICAL_INVERTED).setTexture(OVERLAY_FIRE))
				.addWidget(6, new PointerWidget(47, 63, 5, 17, this::getTemperaturePointer, MoveType.HORIZONTAL).setRenderer(POINTER)).build(player);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.put("inventory", this.inventory.serializeNBT());
		nbt.put("heat", ModCapabilities.HEATABLE.writeNBT(this.heat, null));
		return nbt;
	}

	@Override
	public void deserializeNBT(BlockState state, CompoundNBT nbt) {
		this.inventory.deserializeNBT(nbt.getCompound("inventory"));
		ModCapabilities.HEATABLE.readNBT(this.heat, null, nbt.get("heat"));
		super.deserializeNBT(state, nbt);
	}

	@Override
	public void writeInitData(PacketBuffer buf) {

	}

	@Override
	public void receiveInitData(PacketBuffer buf) {

	}

	@Override
	public void receiveCustomData(int discriminator, PacketBuffer buf) {

	}
}