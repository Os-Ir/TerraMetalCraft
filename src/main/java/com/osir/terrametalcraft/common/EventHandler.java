package com.osir.terrametalcraft.common;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.capability.CapabilityCarving;
import com.osir.terrametalcraft.api.capability.CapabilityHeatable;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.api.thermo.RecipePhasePortrait;
import com.osir.terrametalcraft.common.block.BlockCoverGrass;
import com.osir.terrametalcraft.common.block.ModBlocks;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
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
		if (item == Items.IRON_INGOT || item == Items.GOLD_INGOT) {
			event.addCapability(CapabilityHeatable.KEY, new CapabilityHeatable());
		}
		if (item == Items.PORKCHOP || item == Items.BEEF || item == Items.MUTTON || item == Items.CHICKEN
				|| item == Items.RABBIT || item == Items.COD || item == Items.SALMON || item == Items.KELP
				|| item == Items.POTATO) {
			event.addCapability(CapabilityHeatable.KEY,
					new CapabilityHeatable(new RecipePhasePortrait(3200, 480, getCookedItem(item))));
		}
	}

	private static ItemStack getCookedItem(Item item) {
		ItemStack result = ItemStack.EMPTY;
		if (item == Items.PORKCHOP) {
			result = new ItemStack(Items.COOKED_PORKCHOP);
		} else if (item == Items.BEEF) {
			result = new ItemStack(Items.COOKED_BEEF);
		} else if (item == Items.MUTTON) {
			result = new ItemStack(Items.COOKED_MUTTON);
		} else if (item == Items.CHICKEN) {
			result = new ItemStack(Items.COOKED_CHICKEN);
		} else if (item == Items.RABBIT) {
			result = new ItemStack(Items.COOKED_RABBIT);
		} else if (item == Items.COD) {
			result = new ItemStack(Items.COOKED_COD);
		} else if (item == Items.SALMON) {
			result = new ItemStack(Items.COOKED_SALMON);
		} else if (item == Items.KELP) {
			result = new ItemStack(Items.DRIED_KELP);
		} else if (item == Items.POTATO) {
			result = new ItemStack(Items.BAKED_POTATO);
		}
		return result;
	}

	@SubscribeEvent
	public static void onCoverItemPlace(PlayerInteractEvent.RightClickBlock event) {

	}

	@SubscribeEvent
	public static void onStoneWork(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().isClientSide) {
			return;
		}
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		BlockState state = world.getBlockState(pos);
		if (!(state.isCollisionShapeFullBlock(world, pos) && state.getMaterial() == Material.STONE)) {
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
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().isClientSide) {
			return;
		}
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		BlockState state = world.getBlockState(pos);
		ItemStack stack = event.getItemStack();
		PlayerEntity player = event.getPlayer();
		if (player.isCrouching() && state.isCollisionShapeFullBlock(world, pos) && stack.getItem() == ModItems.grass
				&& event.getFace() == Direction.UP) {
			world.setBlock(pos.above(), ModBlocks.coverGrass.defaultBlockState().setValue(BlockCoverGrass.AGE, 0), 2);
			stack.shrink(1);
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void addItemTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		List<ITextComponent> tooltip = e.getToolTip();
		stack.getCapability(ModCapabilities.CARVING).ifPresent((cap) -> {
			if (!cap.isEmpty()) {
				tooltip.add(new StringTextComponent(TextFormatting.AQUA + I18n.get("cuckoolib.item.carving.carved")));
			}
		});
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onDrawBlockHighlight(DrawHighlightEvent event) {
		if (!(event.getTarget() instanceof BlockRayTraceResult)
				|| !(event.getInfo().getEntity() instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) event.getInfo().getEntity();
		World world = player.level;
		BlockRayTraceResult target = (BlockRayTraceResult) event.getTarget();
		if (target.getType() != RayTraceResult.Type.BLOCK || target.getDirection() != Direction.UP) {
			return;
		}
		BlockPos pos = target.getBlockPos();
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (!(state.isCollisionShapeFullBlock(world, pos) && state.getMaterial() == Material.STONE)) {
			return;
		}
		ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);
		Item item = stack.getItem();
		if (item != Items.FLINT && item != ModItems.stone) {
			return;
		}
		float grow = 0.002f;
		VoxelShape shape = state.getShape(world, pos);
		if (shape.isEmpty()) {
			return;
		}
		MatrixStack transform = event.getMatrix();
		transform.pushPose();
		Vector3f renderView = event.getInfo().getLookVector();
		transform.translate(-renderView.x(), -renderView.y(), -renderView.z());
		transform.translate(pos.getX(), pos.getY(), pos.getZ());
		Matrix4f matrix = transform.last().pose();
		IVertexBuilder builder = event.getBuffers().getBuffer(RenderType.lines());
		builder.vertex(matrix, 0.25f, 1 + grow, 0.25f).color(0, 0, 0, 0.4f).endVertex();
		builder.vertex(matrix, 0.25f, 1 + grow, 0.75f).color(0, 0, 0, 0.4f).endVertex();
		builder.vertex(matrix, 0.25f, 1 + grow, 0.75f).color(0, 0, 0, 0.4f).endVertex();
		builder.vertex(matrix, 0.75f, 1 + grow, 0.75f).color(0, 0, 0, 0.4f).endVertex();
		builder.vertex(matrix, 0.75f, 1 + grow, 0.75f).color(0, 0, 0, 0.4f).endVertex();
		builder.vertex(matrix, 0.75f, 1 + grow, 0.25f).color(0, 0, 0, 0.4f).endVertex();
		builder.vertex(matrix, 0.75f, 1 + grow, 0.25f).color(0, 0, 0, 0.4f).endVertex();
		builder.vertex(matrix, 0.25f, 1 + grow, 0.25f).color(0, 0, 0, 0.4f).endVertex();
		transform.popPose();
	}

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		event.getGeneration().addFeature(Decoration.VEGETAL_DECORATION, ModFeatures.stone);
	}
}