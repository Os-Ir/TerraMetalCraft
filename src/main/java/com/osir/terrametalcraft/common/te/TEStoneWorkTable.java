package com.osir.terrametalcraft.common.te;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.github.zi_jing.cuckoolib.client.render.IWidgetRenderer;
import com.github.zi_jing.cuckoolib.client.render.TextureArea;
import com.github.zi_jing.cuckoolib.gui.IGuiHolderCodec;
import com.github.zi_jing.cuckoolib.gui.IModularGuiHolder;
import com.github.zi_jing.cuckoolib.gui.ModularContainer;
import com.github.zi_jing.cuckoolib.gui.ModularGuiInfo;
import com.github.zi_jing.cuckoolib.gui.impl.IPlanInfoTileEntity;
import com.github.zi_jing.cuckoolib.gui.impl.PlanGuiHolder;
import com.github.zi_jing.cuckoolib.gui.impl.TileEntityCodec;
import com.github.zi_jing.cuckoolib.gui.widget.ButtonWidget;
import com.github.zi_jing.cuckoolib.gui.widget.SlotWidget;
import com.github.zi_jing.cuckoolib.recipe.Recipe;
import com.github.zi_jing.cuckoolib.util.data.ButtonClickData;
import com.github.zi_jing.cuckoolib.util.math.MathUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.capability.ICarving;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.api.te.SyncedTE;
import com.osir.terrametalcraft.common.block.ModBlocks;
import com.osir.terrametalcraft.common.recipe.RecipeHandler;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

public class TEStoneWorkTable extends SyncedTE implements ITickableTileEntity, IModularGuiHolder, IPlanInfoTileEntity {
	public static final TileEntityType<TEStoneWorkTable> TYPE = TileEntityType.Builder
			.of(() -> new TEStoneWorkTable(), ModBlocks.stoneWorkTable).build(null);

	private static final TextureArea BACKGROUND = TextureArea
			.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/stone_work_table/background.png"));
	private static final TextureArea OUTLINE = TextureArea
			.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/stone_work_table/outline.png"));
	private static final TextureArea STONE = TextureArea
			.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/stone_work_table/stone.png"));
	private static final TextureArea STONE_SMALL = TextureArea.createTexture(
			new ResourceLocation(Main.MODID, "textures/gui/stone_work_table/stone.png"), 0, 0, 0.125f, 0.125f);
	private static final ITextComponent TITLE = new TranslationTextComponent("modulargui.stone_work_table.name");

	private static final Cache<Item, List<Recipe>> CACHE_RECIPE = CacheBuilder.newBuilder().maximumSize(64).build();
	private static final Cache<Item, Boolean> CACHE_BLACK_LIST = CacheBuilder.newBuilder().maximumSize(64).build();

	private ItemStackHandler inventory;
	private int plan;

	private boolean slotUpdate;

	public TEStoneWorkTable() {
		super(TYPE);
		this.inventory = new ItemStackHandler(2) {
			@Override
			protected void onContentsChanged(int slot) {
				if (!TEStoneWorkTable.this.level.isClientSide && slot == 0) {
					TEStoneWorkTable.this.slotUpdate = true;
				}
			}
		};
		this.plan = -1;
	}

	private static List<Recipe> getRecipe(ItemStack stack) {
		Item item = stack.getItem();
		if (stack.getCount() != 1 || CACHE_BLACK_LIST.asMap().containsKey(item)) {
			return new ArrayList<Recipe>();
		}
		if (CACHE_RECIPE.asMap().containsKey(item)) {
			return CACHE_RECIPE.getIfPresent(item);
		}
		List<Recipe> recipes = RecipeHandler.MAP_CARVING.findAllRecipes(Arrays.asList(new ItemStack(item)),
				new ArrayList<FluidStack>());
		if (recipes.isEmpty()) {
			CACHE_BLACK_LIST.put(item, false);
		} else {
			CACHE_RECIPE.put(item, recipes);
		}
		return recipes;
	}

	@Override
	public void tick() {
		if (this.slotUpdate) {
			this.slotUpdate = false;
			this.setPlan(-1);
		}
	}

	private void onStoneWork(ButtonClickData clickData, ModularContainer container) {
		ItemStack stack = this.inventory.getStackInSlot(0);
		if (stack.getCount() == 1 && stack.getCapability(ModCapabilities.CARVING).isPresent()) {
			long[] data = this.inventory.getStackInSlot(0).getCapability(ModCapabilities.CARVING).orElseThrow(null)
					.getAllCarveData();
			int dataCount = clickData.getY() / 16 * 7 + clickData.getX() / 16;
			int px = clickData.getX() / 2;
			int py = clickData.getY() / 2;
			if (this.getCarvingPoint(data, px - 1, py) != 0 || this.getCarvingPoint(data, px + 1, py) != 0
					|| this.getCarvingPoint(data, px, py - 1) != 0 || this.getCarvingPoint(data, px, py + 1) != 0) {
				Random rand = new Random();
				int minx = px - 3;
				int maxx = px + 3;
				int miny = py - 3;
				int maxy = py + 3;
				for (int i = minx; i <= maxx; i++) {
					for (int j = miny; j <= maxy; j++) {
						if (MathUtil.between(i, minx + 1, maxx - 1) || MathUtil.between(j, miny + 1, maxy - 1)
								|| rand.nextBoolean()) {
							this.setCarvingPoint(data, i, j, true);
						}
					}
				}
				if (this.checkRecipe()) {
					this.setOutputStack();
					this.inventory.setStackInSlot(0, ItemStack.EMPTY);
				}
				if (this.level != null) {
					this.level.blockEntityChanged(this.getBlockPos(), this);
				}
			}
		}
	}

	private int getCarvingPoint(long[] data, int px, int py) {
		if (px < 0 || px >= 56 || py < 0 || py >= 56) {
			return -1;
		}
		return (data[px / 8 + py / 8 * 7] & (1l << (px % 8 + py % 8 * 8))) == 0 ? 0 : 1;
	}

	private void setCarvingPoint(long[] data, int px, int py, boolean value) {
		if (px < 0 || px >= 56 || py < 0 || py >= 56) {
			return;
		}
		int dataCount = px / 8 + py / 8 * 7;
		int dx = px % 8;
		int dy = py % 8;
		if (((data[dataCount] & (1l << (dx + dy * 8))) != 0) ^ value) {
			data[dataCount] ^= (1l << (dx + dy * 8));
		}
	}

	private void renderWorkButton(MatrixStack transform, int x, int y, int width, int height) {
		ItemStack stack = this.inventory.getStackInSlot(0);
		if (stack.getCount() == 1 && stack.getCapability(ModCapabilities.CARVING).isPresent()) {
			long[] data = this.inventory.getStackInSlot(0).getCapability(ModCapabilities.CARVING)
					.orElseThrow(() -> new NullPointerException()).getAllCarveData();
			for (int i = 0; i < 49; i++) {
				if (data[i] == 0) {
					STONE.draw(transform, x + (i % 7) * 16, y + i / 7 * 16, 16, 16);
				} else {
					for (int j = 0; j < 64; j++) {
						if ((data[i] & (1l << j)) == 0) {
							STONE_SMALL.draw(transform, x + (i % 7) * 16 + j % 8 * 2, y + i / 7 * 16 + j / 8 * 2, 2, 2);
						}
					}
				}
			}
		}
		if (this.plan >= 0) {
			List<Recipe> list = getRecipe(stack);
			if (list.size() > this.plan) {
				Recipe recipe = list.get(this.plan);
				if (recipe.containsProperty(RecipeHandler.PROPERTY_CARVING)) {
					long value = recipe.getPropertyValue(RecipeHandler.PROPERTY_CARVING);
					int alpha = 60
							+ (int) (Math.cos(((float) (System.currentTimeMillis() % 2000)) / 2000 * 2 * Math.PI) * 40);
					for (int i = 0; i < 49; i++) {
						if ((value & (1l << i)) != 0) {
							OUTLINE.draw(transform, x + (i % 7) * 16, y + i / 7 * 16, 16, 16, alpha);
						}
					}
				}
			}
		}
	}

	public void openPlanGui(ButtonClickData clickData, ModularContainer container) {
		ModularGuiInfo.openModularGui(new PlanGuiHolder(this), (ServerPlayerEntity) container.getGuiInfo().getPlayer(),
				container.getParentGuiHolders());
	}

	private void setPlan(int plan) {
		this.plan = plan;
		this.writeCustomData(100, (buf) -> buf.writeInt(plan));
		this.level.blockEntityChanged(this.getBlockPos(), this);
	}

	private boolean checkRecipe() {
		ItemStack stack = this.inventory.getStackInSlot(0);
		List<Recipe> list = getRecipe(stack);
		if (list.size() > this.plan && this.plan >= 0) {
			Recipe recipe = list.get(this.plan);
			if (!recipe.containsProperty(RecipeHandler.PROPERTY_CARVING)) {
				return true;
			}
			long value = recipe.getPropertyValue(RecipeHandler.PROPERTY_CARVING);
			if (stack.getCapability(ModCapabilities.CARVING).isPresent()) {
				ICarving cap = stack.getCapability(ModCapabilities.CARVING)
						.orElseThrow(() -> new NullPointerException());
				for (int i = 0; i < 49; i++) {
					if (!cap.isAreaValid(i, (value & (1l << i)) == 0 ? 1 : 0, 0.125f)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void setOutputStack() {
		ItemStack result = this.inventory.insertItem(1,
				getRecipe(this.inventory.getStackInSlot(0)).get(this.plan).getOutputs().get(0).getItemStack().copy(),
				false);
		if (!result.isEmpty()) {
			BlockPos pos = this.getBlockPos();
			InventoryHelper.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), result);
		}
	}

	@Override
	public int getPlanCount() {
		return getRecipe(this.inventory.getStackInSlot(0)).size();
	}

	@Override
	public IWidgetRenderer getOverlayRenderer(int count) {
		return (transform, x, y, width, height) -> {
			List<Recipe> recipes = getRecipe(this.inventory.getStackInSlot(0));
			if (recipes.size() > count) {
				Minecraft.getInstance().getItemRenderer()
						.renderAndDecorateItem(recipes.get(count).getOutputs().get(0).getItemStack(), x + 1, y + 1);
			}
		};
	}

	@Override
	public void callback(int count) {
		if (getRecipe(this.inventory.getStackInSlot(0)).size() <= count) {
			return;
		}
		this.setPlan(count);
		if (this.checkRecipe()) {
			this.setOutputStack();
			this.inventory.setStackInSlot(0, ItemStack.EMPTY);
		}
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
		return ModularGuiInfo.builder(176, 216).setBackground(BACKGROUND).addPlayerInventory(player.inventory, 8, 134)
				.addWidget(0, new SlotWidget(153, 7, this.inventory, 0))
				.addWidget(1, new SlotWidget(153, 101, this.inventory, 1, false, true))
				.addWidget(2,
						new ButtonWidget(0, 6, 6, 112, 112, this::onStoneWork).setRenderer(this::renderWorkButton))
				.addWidget(3, new ButtonWidget(1, 133, 7, 16, 16, this::openPlanGui)
						.setRenderer((transform, x, y, width, height) -> {
							if (this.plan >= 0) {
								List<Recipe> recipes = getRecipe(this.inventory.getStackInSlot(0));
								if (recipes.size() > this.plan) {
									Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(
											recipes.get(this.plan).getOutputs().get(0).getItemStack(), x, y);
								}
							}
						}))
				.build(player);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.put("inventory", this.inventory.serializeNBT());
		if (this.plan >= 0) {
			nbt.putInt("plan", this.plan);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(BlockState state, CompoundNBT nbt) {
		this.inventory.deserializeNBT(nbt.getCompound("inventory"));
		if (nbt.contains("plan")) {
			this.plan = nbt.getInt("plan");
		}
		super.deserializeNBT(state, nbt);
	}

	@Override
	public void writeInitData(PacketBuffer buf) {
		buf.writeInt(this.plan);
	}

	@Override
	public void receiveInitData(PacketBuffer buf) {
		this.plan = buf.readInt();
	}

	@Override
	public void receiveCustomData(int discriminator, PacketBuffer buf) {
		if (discriminator == 100) {
			this.plan = buf.readInt();
		}
	}
}