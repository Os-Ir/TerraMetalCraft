package com.osir.terrametalcraft.common;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.capability.CapabilityCarving;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.api.thermo.ThermoUtil;
import com.osir.terrametalcraft.common.item.ModItems;
import com.osir.terrametalcraft.common.world.feature.ModFeatures;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.items.ItemHandlerHelper;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
public class EventHandler {
	@SubscribeEvent
	public static void attachItemCapability(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack stack = event.getObject();
		Item item = stack.getItem();
		if ((item == ModItems.chippedFlint || item == ModItems.chippedStone || item == ModItems.grindedFlint
				|| item == ModItems.grindedStone) && !stack.getCapability(ModCapabilities.CARVING).isPresent()) {
			event.addCapability(CapabilityCarving.KEY, new CapabilityCarving());
		}
	}

	@SubscribeEvent
	public static void onStoneWork(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().isRemote) {
			return;
		}
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		BlockState state = world.getBlockState(pos);
		if (!(state.isNormalCube(world, pos) && state.getMaterial() == Material.ROCK)) {
			return;
		}
		ItemStack stack = event.getItemStack();
		PlayerEntity player = event.getPlayer();
		ItemStack result = ItemStack.EMPTY;
		Direction face = event.getFace();
		if (stack.getItem() == Items.FLINT) {
			if (face == Direction.UP) {
				result = new ItemStack(ModItems.grindedFlint);
			} else if (face != Direction.DOWN) {
				result = new ItemStack(ModItems.chippedFlint);
			}
		} else if (stack.getItem() == ModItems.stone) {
			if (face == Direction.UP) {
				result = new ItemStack(ModItems.grindedStone);
			} else if (face != Direction.DOWN) {
				result = new ItemStack(ModItems.chippedStone);
			}
		}
		if (!result.isEmpty()) {
			stack.shrink(1);
			ItemHandlerHelper.giveItemToPlayer(player, result);
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void addItemTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		List<ITextComponent> tooltip = e.getToolTip();
		stack.getCapability(ModCapabilities.CARVING).ifPresent((cap) -> {
			if (!cap.isEmpty()) {
				tooltip.add(
						new StringTextComponent(TextFormatting.AQUA + I18n.format("cuckoolib.item.carving.carved")));
			}
		});
		stack.getCapability(ModCapabilities.HEATABLE).ifPresent((cap) -> {
			tooltip.add(new StringTextComponent("Temp: " + cap.getTemperature(ThermoUtil.ATMOSPHERIC_PRESSURE)));
			tooltip.add(new StringTextComponent("Energy: " + cap.getEnergy()));
		});
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onDrawBlockHighlight(DrawHighlightEvent event) {
		if (!(event.getTarget() instanceof BlockRayTraceResult)
				|| !(event.getInfo().getRenderViewEntity() instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) event.getInfo().getRenderViewEntity();
		World world = player.world;
		BlockRayTraceResult target = (BlockRayTraceResult) event.getTarget();
		if (target.getType() != RayTraceResult.Type.BLOCK || target.getFace() != Direction.UP) {
			return;
		}
		BlockPos pos = target.getPos();
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (!(state.isNormalCube(world, pos) && state.getMaterial() == Material.ROCK)) {
			return;
		}
		ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
		Item item = stack.getItem();
		if (item != Items.FLINT && item != ModItems.stone) {
			return;
		}
		float grow = 0.002f;
		VoxelShape shape = state.getRenderShape(world, pos);
		if (shape.isEmpty()) {
			return;
		}
		MatrixStack transform = event.getMatrix();
		transform.push();
		Vector3d renderView = event.getInfo().getProjectedView();
		transform.translate(-renderView.x, -renderView.y, -renderView.z);
		transform.translate(pos.getX(), pos.getY(), pos.getZ());
		AxisAlignedBB box = shape.getBoundingBox();
		Matrix4f matrix = transform.getLast().getMatrix();
		IVertexBuilder builder = event.getBuffers().getBuffer(RenderType.getLines());
		builder.pos(matrix, 0.25f, 1 + grow, 0.25f).color(0, 0, 0, 0.4f).endVertex();
		builder.pos(matrix, 0.25f, 1 + grow, 0.75f).color(0, 0, 0, 0.4f).endVertex();
		builder.pos(matrix, 0.25f, 1 + grow, 0.75f).color(0, 0, 0, 0.4f).endVertex();
		builder.pos(matrix, 0.75f, 1 + grow, 0.75f).color(0, 0, 0, 0.4f).endVertex();
		builder.pos(matrix, 0.75f, 1 + grow, 0.75f).color(0, 0, 0, 0.4f).endVertex();
		builder.pos(matrix, 0.75f, 1 + grow, 0.25f).color(0, 0, 0, 0.4f).endVertex();
		builder.pos(matrix, 0.75f, 1 + grow, 0.25f).color(0, 0, 0, 0.4f).endVertex();
		builder.pos(matrix, 0.25f, 1 + grow, 0.25f).color(0, 0, 0, 0.4f).endVertex();
		transform.pop();
	}

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		event.getGeneration().withFeature(Decoration.VEGETAL_DECORATION, ModFeatures.stone);
	}
}