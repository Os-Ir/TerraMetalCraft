package com.osir.terrametalcraft.common.te;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.zi_jing.cuckoolib.client.render.GLUtil;
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
import com.github.zi_jing.cuckoolib.gui.widget.RendererWidget;
import com.github.zi_jing.cuckoolib.gui.widget.SlotWidget;
import com.github.zi_jing.cuckoolib.util.data.ButtonClickData;
import com.github.zi_jing.cuckoolib.util.math.MathUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.capability.IPottery;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.api.te.SyncedTE;
import com.osir.terrametalcraft.common.block.ModBlocks;
import com.osir.terrametalcraft.common.recipe.ModRecipeTypes;
import com.osir.terrametalcraft.common.recipe.PotteryWorkRecipe;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class TEPotteryWorkTable extends SyncedTE implements ITickableTileEntity, IModularGuiHolder, IPlanInfoTileEntity {
	public static final TileEntityType<TEPotteryWorkTable> TYPE = TileEntityType.Builder.of(() -> new TEPotteryWorkTable(), ModBlocks.POTTERY_WORK_TABLE).build(null);

	private static final TextureArea BACKGROUND = TextureArea.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/pottery_work_table/background.png"));
	private static final ITextComponent TITLE = new TranslationTextComponent("modulargui.pottery_work_table.name");

	private static final Cache<Item, List<PotteryWorkRecipe>> CACHE_RECIPE = CacheBuilder.newBuilder().maximumSize(64).build();
	private static final Cache<Item, Boolean> CACHE_BLACK_LIST = CacheBuilder.newBuilder().maximumSize(64).build();

	private ItemStackHandler inventory;
	private int plan;

	private boolean slotUpdate;

	public TEPotteryWorkTable() {
		super(TYPE);
		this.inventory = new ItemStackHandler(2) {
			@Override
			protected void onContentsChanged(int slot) {
				if (!TEPotteryWorkTable.this.level.isClientSide && slot == 0) {
					TEPotteryWorkTable.this.slotUpdate = true;
				}
			}
		};
		this.plan = -1;
	}

	private List<PotteryWorkRecipe> getRecipe(ItemStack stack) {
		Item item = stack.getItem();
		if (stack.getCount() != 1 || CACHE_BLACK_LIST.asMap().containsKey(item)) {
			return Collections.emptyList();
		}
		if (CACHE_RECIPE.asMap().containsKey(item)) {
			return CACHE_RECIPE.getIfPresent(item);
		}
		List<PotteryWorkRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.POTTERY_WORK_TYPE).stream().filter((recipe) -> recipe.matches(stack)).collect(Collectors.toList());
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

	private void onPotteryWork(ButtonClickData clickData, ModularContainer container, long clickTime) {
		ItemStack stack = this.inventory.getStackInSlot(0);
		if (stack.getCount() == 1 && stack.getCapability(ModCapabilities.POTTERY).isPresent()) {
			int index = clickData.getY() / 15;
			stack.getCapability(ModCapabilities.POTTERY).orElse(null).potteryWork(index, Math.max(1, (int) (clickTime / 100)));
			if (this.checkRecipe()) {
				this.setOutputStack();
				this.inventory.setStackInSlot(0, ItemStack.EMPTY);
			}
			if (this.level != null) {
				this.level.blockEntityChanged(this.getBlockPos(), this);
			}
		}
	}

	private void renderWorkBoard(ModularContainer container, MatrixStack transform, int x, int y, int width, int height) {
		ItemStack stack = this.inventory.getStackInSlot(0);
		if (stack.getCount() == 1 && stack.getCapability(ModCapabilities.POTTERY).isPresent()) {
			int[] data = stack.getCapability(ModCapabilities.POTTERY).orElse(null).getAllData();
			for (int i = 0; i < 8; i++) {
				GLUtil.fill(transform, x + data[i], y + i * 15, 50 - data[i], 15, 0xcfe9e9ff);
				GLUtil.fill(transform, x + 55, y + i * 15, 50 - data[i], 15, 0xcfe9e9ff);
			}
		}
		List<PotteryWorkRecipe> list = this.getRecipe(stack);
		if (list.size() > this.plan && this.plan >= 0) {
			PotteryWorkRecipe recipe = list.get(this.plan);
			int[] recipeData = recipe.data;
			for (int i = 0; i < 8; i++) {
				int v = MathHelper.clamp(recipeData[i], 1, 49);
				int color = 0x00ff0000 + (int) (MathUtil.getTriangularWave(2000, 0.125, 0.25) * 0xff);
				GLUtil.fill(transform, x + recipeData[i] - 1, y + i * 15, 2, 15, color);
				GLUtil.fill(transform, x + 104 - recipeData[i], y + i * 15, 2, 15, color);
			}
		}
	}

	public void openPlanGui(ButtonClickData clickData, ModularContainer container) {
		ModularGuiInfo.openModularGui(new PlanGuiHolder(this), (ServerPlayerEntity) container.getGuiInfo().getPlayer(), container.getParentGuiHolders());
	}

	private void setPlan(int plan) {
		this.plan = plan;
		this.writeCustomData(100, (buf) -> buf.writeInt(plan));
		this.level.blockEntityChanged(this.getBlockPos(), this);
	}

	private boolean checkRecipe() {
		ItemStack stack = this.inventory.getStackInSlot(0);
		List<PotteryWorkRecipe> list = this.getRecipe(stack);
		if (list.size() > this.plan && this.plan >= 0) {
			PotteryWorkRecipe recipe = list.get(this.plan);
			int[] data = recipe.data;
			if (stack.getCapability(ModCapabilities.POTTERY).isPresent()) {
				IPottery cap = stack.getCapability(ModCapabilities.POTTERY).orElse(null);
				for (int i = 0; i < 8; i++) {
					if (!cap.isAreaValid(i, data[i] - 1, data[i] + 1)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	private void setOutputStack() {
		ItemStack result = this.inventory.insertItem(1, this.getRecipe(this.inventory.getStackInSlot(0)).get(this.plan).output.get().copy(), false);
		if (!result.isEmpty()) {
			BlockPos pos = this.getBlockPos();
			InventoryHelper.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), result);
		}
	}

	@Override
	public int getPlanCount() {
		return this.getRecipe(this.inventory.getStackInSlot(0)).size();
	}

	@Override
	public IWidgetRenderer getOverlayRenderer(int count) {
		return (transform, x, y, width, height) -> {
			List<PotteryWorkRecipe> recipes = this.getRecipe(this.inventory.getStackInSlot(0));
			if (recipes.size() > count) {
				Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(recipes.get(count).output.get(), x + 1, y + 1);
			}
		};
	}

	@Override
	public void callback(int count) {
		if (this.getRecipe(this.inventory.getStackInSlot(0)).size() <= count) {
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
		return ModularGuiInfo.builder(176, 216).setBackground(BACKGROUND).addWidget(0, new SlotWidget(153, 7, this.inventory, 0)).addWidget(1, new SlotWidget(153, 101, this.inventory, 1, false, true)).addPlayerInventory(player.inventory, 8, 134)
				.addWidget(2, new ButtonWidget(0, 6, 6, 16, 120).setReleaseCallback(this::onPotteryWork)).addWidget(3, new RendererWidget(25, 6, 105, 120, this::renderWorkBoard))
				.addWidget(4, new ButtonWidget(1, 133, 7, 16, 16, this::openPlanGui).setRenderer((transform, x, y, width, height) -> {
					if (this.plan >= 0) {
						List<PotteryWorkRecipe> recipes = this.getRecipe(this.inventory.getStackInSlot(0));
						if (recipes.size() > this.plan) {
							Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(recipes.get(this.plan).output.get(), x, y);
						}
					}
				})).build(player);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.put("inventory", this.inventory.serializeNBT());
		if (this.plan >= 0) {
			nbt.putInt("plan", this.plan);
		}
		return nbt;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		this.inventory.deserializeNBT(nbt.getCompound("inventory"));
		if (nbt.contains("plan")) {
			this.plan = nbt.getInt("plan");
		}
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