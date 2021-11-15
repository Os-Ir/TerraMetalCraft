package com.osir.terrametalcraft.common.item;

import java.util.List;

import com.github.zi_jing.cuckoolib.LibRegistryHandler;
import com.github.zi_jing.cuckoolib.client.render.TextureArea;
import com.github.zi_jing.cuckoolib.gui.ModularContainer;
import com.github.zi_jing.cuckoolib.gui.ModularGuiInfo;
import com.github.zi_jing.cuckoolib.gui.impl.IPlayerGuiInfo;
import com.github.zi_jing.cuckoolib.gui.impl.PlayerGuiHolder;
import com.github.zi_jing.cuckoolib.gui.widget.ButtonWidget;
import com.github.zi_jing.cuckoolib.gui.widget.SlotWidget;
import com.github.zi_jing.cuckoolib.item.ItemBase;
import com.github.zi_jing.cuckoolib.material.MaterialEntry;
import com.github.zi_jing.cuckoolib.material.type.MaterialBase;
import com.github.zi_jing.cuckoolib.util.data.ButtonClickData;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.capability.IHeatContainer;
import com.osir.terrametalcraft.api.capability.IHeatable;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.api.thermo.impl.SolidPhasePortrait;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

public class ItemSmallCrucible extends ItemBase implements IPlayerGuiInfo {
	private static final TextureArea BACKGROUND = TextureArea.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/small_crucible/background.png"));
	private static final ResourceLocation INFO_NAME = new ResourceLocation(Main.MODID, "small_crucible");
	private static final ITextComponent TITLE = new TranslationTextComponent("modulargui.small_crucible.name");

	public ItemSmallCrucible() {
		super(Main.GROUP_ITEM);
	}

	public static boolean addMaterialToItem(ItemStack stack, MaterialBase material, int unit) {
		if (stack.getItem() != ModItems.SMALL_CRUCIBLE) {
			return false;
		}
		IHeatContainer container = stack.getCapability(ModCapabilities.HEAT_CONTAINER).orElse(null);
		IHeatable heat = stack.getCapability(ModCapabilities.HEATABLE).orElse(null);
		if (container.getRemainingVolume() >= unit) {
			container.addMaterial(material, unit);
			((SolidPhasePortrait) heat.getPhasePortrait()).setCapacity(container.getHeatCapacity());
			return true;
		}
		return false;
	}

	public static int removeMaterialFromItem(ItemStack stack, MaterialBase material, int unit) {
		if (stack.getItem() != ModItems.SMALL_CRUCIBLE) {
			return 0;
		}
		IHeatContainer container = stack.getCapability(ModCapabilities.HEAT_CONTAINER).orElse(null);
		IHeatable heat = stack.getCapability(ModCapabilities.HEATABLE).orElse(null);
		int ret = container.removeMaterial(material, unit);
		((SolidPhasePortrait) heat.getPhasePortrait()).setCapacity(container.getHeatCapacity());
		return ret;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (world.isClientSide) {
			return ActionResult.success(player.getItemInHand(hand));
		}
		PlayerGuiHolder.openPlayerGui(this, (ServerPlayerEntity) player, hand == Hand.MAIN_HAND ? 1 : 0);
		return ActionResult.consume(player.getItemInHand(hand));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		stack.getCapability(ModCapabilities.HEAT_CONTAINER).ifPresent((container) -> {
			stack.getCapability(ModCapabilities.HEATABLE).ifPresent((heat) -> {
				double temp = heat.getTemperature();
				container.getMaterials().forEach((material, unit) -> {
					tooltip.add(new StringTextComponent(TextFormatting.AQUA + material.getLocalizedName() + TextFormatting.WHITE + " (" + material.getState(temp).getStateLocalizedName() + ") - " + TextFormatting.GREEN + unit + TextFormatting.AQUA + "L"));
				});
			});
		});
	}

	@Override
	public ResourceLocation getInfoRegistryName() {
		return INFO_NAME;
	}

	@Override
	public ITextComponent getTitle(PlayerEntity player) {
		return TITLE;
	}

	@Override
	public ModularGuiInfo createGuiInfo(PlayerEntity player) {
		ItemStackHandler inventory = new ItemStackHandler(1);
		return ModularGuiInfo.builder().setBackground(BACKGROUND).addPlayerInventory(player.inventory, true).addWidget(0, new SlotWidget(80, 8, inventory, 0)).addWidget(1, new ButtonWidget(0, 152, 8, 16, 16, this::onButtonClicked))
				.addCloseListener(this::onGuiClosed).build(player);
	}

	public void onGuiClosed(ModularContainer container) {
		PlayerEntity player = container.getGuiInfo().getPlayer();
		World world = player.level;
		if (!player.isAlive() || (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).hasDisconnected())) {
			player.drop(container.slots.get(0).getItem(), false);
		} else {
			player.inventory.placeItemBackInInventory(world, container.slots.get(0).getItem());
		}
		container.slots.get(0).set(ItemStack.EMPTY);
	}

	public void onButtonClicked(ButtonClickData data, ModularContainer container) {
		ItemStack stack = container.getGuiInfo().getPlayer().getItemInHand(container.getGuiInfo().getArg(0) == 1 ? Hand.MAIN_HAND : Hand.OFF_HAND);
		ItemStack addStack = container.getSlot(0).getItem();
		Item addItem = addStack.getItem();
		if (LibRegistryHandler.ITEM_MATERIAL_REGISTRY.containsKey(addItem)) {
			MaterialEntry entry = LibRegistryHandler.ITEM_MATERIAL_REGISTRY.get(addItem);
			if (addMaterialToItem(stack, entry.getMaterial(), entry.getShape().getUnit())) {
				addStack.shrink(1);
			}
		}
	}
}