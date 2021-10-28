package com.osir.terrametalcraft.common.te;

import org.apache.commons.lang3.ArrayUtils;

import com.github.zi_jing.cuckoolib.client.gui.ModularScreen;
import com.github.zi_jing.cuckoolib.client.render.IWidgetRenderer;
import com.github.zi_jing.cuckoolib.client.render.TextureArea;
import com.github.zi_jing.cuckoolib.gui.IGuiHolderCodec;
import com.github.zi_jing.cuckoolib.gui.IModularGuiHolder;
import com.github.zi_jing.cuckoolib.gui.ModularContainer;
import com.github.zi_jing.cuckoolib.gui.ModularGuiInfo;
import com.github.zi_jing.cuckoolib.gui.impl.TileEntityCodec;
import com.github.zi_jing.cuckoolib.gui.widget.ButtonWidget;
import com.github.zi_jing.cuckoolib.gui.widget.SlotWidget;
import com.github.zi_jing.cuckoolib.gui.widget.VariableListWidget;
import com.github.zi_jing.cuckoolib.tool.IToolInfo;
import com.github.zi_jing.cuckoolib.util.data.ButtonClickData;
import com.github.zi_jing.cuckoolib.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.ModRegistries;
import com.osir.terrametalcraft.api.te.SyncedTE;
import com.osir.terrametalcraft.api.tool.IGrindstoneTool;
import com.osir.terrametalcraft.common.block.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

public class TEGrindstone extends SyncedTE implements ITickableTileEntity, IModularGuiHolder {
	public static final TileEntityType<TEGrindstone> TYPE = TileEntityType.Builder.of(() -> new TEGrindstone(), ModBlocks.GRINDSTONE).build(null);

	private static final TextureArea BACKGROUND = TextureArea.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/grindstone/background.png"));
	private static final TextureArea BUTTON = TextureArea.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/grindstone/button.png"));
	private static final TextureArea BUTTON_ACTIVATED = TextureArea.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/grindstone/button_activated.png"));
	private static final ITextComponent TITLE = new TranslationTextComponent("modulargui.grindstone.name");

	private ItemStackHandler inventory;
	private IGrindstoneTool plan;
	private int[] order;
	private int progress;

	private boolean slotUpdate;

	public TEGrindstone() {
		super(TYPE);
		this.inventory = new ItemStackHandler(6) {
			@Override
			protected void onContentsChanged(int slot) {
				if (!TEGrindstone.this.level.isClientSide && slot < 5) {
					TEGrindstone.this.slotUpdate = true;
				}
			}

			@Override
			public int getSlotLimit(int slot) {
				if (slot < 5) {
					return 1;
				}
				return 64;
			}
		};
		this.order = new int[0];
	}

	@Override
	public void tick() {
		if (this.slotUpdate) {
			this.slotUpdate = false;
			this.updatePlan();
		}
	}

	private void updatePlan() {
		ItemStack[] parts = new ItemStack[5];
		for (int i = 0; i < 5; i++) {
			parts[i] = this.inventory.getStackInSlot(i).copy();
		}
		for (IToolInfo info : ModRegistries.REGISTRY_TOOL.values()) {
			if (info instanceof IGrindstoneTool) {
				IGrindstoneTool tool = (IGrindstoneTool) info;
				if (tool.matches(parts)) {
					this.setPlan(tool);
					this.order = MathUtil.getRandomSortedArray(MathHelper.clamp(tool.getToolComplexity(), 1, 5) * 2);
					this.progress = 0;
					this.writeCustomData(101, (buf) -> {
						buf.writeInt(this.order.length);
						for (int i = 0; i < this.order.length; i++) {
							buf.writeInt(this.order[i]);
						}
					});
					this.writeCustomData(102, (buf) -> {
						buf.writeInt(0);
					});
					return;
				}
			}
		}
		this.setPlan(null);
		this.order = new int[0];
		this.progress = 0;
		this.writeCustomData(101, (buf) -> {
			buf.writeInt(0);
		});
		this.writeCustomData(102, (buf) -> {
			buf.writeInt(0);
		});
	}

	private void setPlan(IGrindstoneTool plan) {
		this.plan = plan;
		if (plan == null) {
			this.writeCustomData(100, (buf) -> buf.writeBoolean(false));
		} else {
			this.writeCustomData(100, (buf) -> {
				buf.writeBoolean(true);
				buf.writeResourceLocation(plan.getRegistryName());
			});
		}
		this.level.blockEntityChanged(this.getBlockPos(), this);
	}

	private void setOutputStack(ItemStack[] parts) {
		ItemStack result = this.inventory.insertItem(5, this.plan.getToolItem(parts).createItemStack(1, this.plan.getMaterial(parts)), false);
		if (!result.isEmpty()) {
			BlockPos pos = this.getBlockPos();
			InventoryHelper.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), result);
		}
	}

	private void onButtonClick(ButtonClickData data, ModularContainer container) {
		int count = data.getCount();
		if (count == this.order[this.progress]) {
			this.progress++;
			if (this.progress == this.order.length) {
				ItemStack[] parts = new ItemStack[5];
				for (int i = 0; i < 5; i++) {
					parts[i] = this.inventory.getStackInSlot(i).copy();
				}
				this.setOutputStack(parts);
				for (int i = 0; i < 5; i++) {
					this.inventory.setStackInSlot(i, ItemStack.EMPTY);
				}
				this.progress = 0;
			}
		} else {
			boolean flag = true;
			for (int i = 0; i < this.progress; i++) {
				if (this.order[i] == count) {
					flag = false;
				}
			}
			if (flag) {
				this.progress = 0;
			}
		}
		this.writeCustomData(102, (buf) -> {
			buf.writeInt(this.progress);
		});
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
		return ModularGuiInfo.builder().setBackground(BACKGROUND).addPlayerInventory(player.inventory).addWidget(0, new SlotWidget(8, 8, this.inventory, 0)).addWidget(1, new SlotWidget(30, 8, this.inventory, 1))
				.addWidget(2, new SlotWidget(52, 8, this.inventory, 2)).addWidget(3, new SlotWidget(74, 8, this.inventory, 3)).addWidget(4, new SlotWidget(96, 8, this.inventory, 4)).addWidget(5, new SlotWidget(152, 8, this.inventory, 5))
				.addWidget(6,
						new VariableListWidget().addWidget(0, new ButtonWidget(0, 8, 41, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(0)))
								.addWidget(1, new ButtonWidget(1, 8, 63, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(1))).addWidget(2, new ButtonWidget(2, 30, 41, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(2)))
								.addWidget(3, new ButtonWidget(3, 30, 63, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(3))).addWidget(4, new ButtonWidget(4, 52, 41, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(4)))
								.addWidget(5, new ButtonWidget(5, 52, 63, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(5))).addWidget(6, new ButtonWidget(6, 74, 41, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(6)))
								.addWidget(7, new ButtonWidget(7, 74, 63, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(7))).addWidget(8, new ButtonWidget(8, 96, 41, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(8)))
								.addWidget(9, new ButtonWidget(9, 96, 63, 16, 16, this::onButtonClick).setRenderer(new ButtonRenderer(9))))
				.build(player);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void executeRenderTask(ModularScreen screen) {
		VariableListWidget listWidget = ((VariableListWidget) screen.getGuiInfo().getWidget(6));
		int length = this.order.length;
		for (int i = 0; i < 10; i++) {
			if (i < length) {
				listWidget.getWidget(i).setEnable(true);
			} else {
				listWidget.getWidget(i).setEnable(false);
			}
		}
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.put("inventory", this.inventory.serializeNBT());
		if (this.plan != null) {
			nbt.putString("plan", this.plan.getRegistryName().toString());
		}
		if (!ArrayUtils.isEmpty(this.order)) {
			nbt.putIntArray("order", this.order);
		}
		nbt.putInt("progress", this.progress);
		return nbt;
	}

	@Override
	public void deserializeNBT(BlockState state, CompoundNBT nbt) {
		this.inventory.deserializeNBT(nbt.getCompound("inventory"));
		if (nbt.contains("plan")) {
			this.plan = (IGrindstoneTool) ModRegistries.REGISTRY_TOOL.getValue(new ResourceLocation(nbt.getString("plan")));
		}
		if (nbt.contains("order")) {
			this.order = nbt.getIntArray("order");
		}
		this.progress = nbt.getInt("progress");
		super.deserializeNBT(state, nbt);
	}

	@Override
	public void writeInitData(PacketBuffer buf) {
		buf.writeInt(this.order.length);
		for (int i = 0; i < this.order.length; i++) {
			buf.writeInt(this.order[i]);
		}
		buf.writeInt(this.progress);
	}

	@Override
	public void receiveInitData(PacketBuffer buf) {
		int length = buf.readInt();
		this.order = new int[length];
		for (int i = 0; i < length; i++) {
			this.order[i] = buf.readInt();
		}
		this.progress = buf.readInt();
	}

	@Override
	public void receiveCustomData(int discriminator, PacketBuffer buf) {
		if (discriminator == 100) {
			if (buf.readBoolean()) {
				this.plan = (IGrindstoneTool) ModRegistries.REGISTRY_TOOL.get(buf.readResourceLocation());
			} else {
				this.plan = null;
			}
		} else if (discriminator == 101) {
			int length = buf.readInt();
			this.order = new int[length];
			for (int i = 0; i < length; i++) {
				this.order[i] = buf.readInt();
			}
		} else if (discriminator == 102) {
			this.progress = buf.readInt();
		}
	}

	private class ButtonRenderer implements IWidgetRenderer {
		private int count;

		public ButtonRenderer(int count) {
			this.count = count;
		}

		@Override
		public void draw(MatrixStack transform, int x, int y, int width, int height) {
			for (int i = 0; i < TEGrindstone.this.progress; i++) {
				if (TEGrindstone.this.order[i] == this.count) {
					BUTTON_ACTIVATED.draw(transform, x, y, width, height);
					return;
				}
			}
			BUTTON.draw(transform, x, y, width, height);
		}
	}
}