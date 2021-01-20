package com.osir.terrametalcraft.common.te;

import java.util.Random;

import com.github.zi_jing.cuckoolib.client.render.TextureArea;
import com.github.zi_jing.cuckoolib.gui.IGuiHolderCodec;
import com.github.zi_jing.cuckoolib.gui.IModularGuiHolder;
import com.github.zi_jing.cuckoolib.gui.ModularGuiInfo;
import com.github.zi_jing.cuckoolib.gui.TileEntityCodec;
import com.github.zi_jing.cuckoolib.gui.widget.ButtonWidget;
import com.github.zi_jing.cuckoolib.gui.widget.SlotWidget;
import com.github.zi_jing.cuckoolib.util.data.ButtonClickData;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.capability.ModCapabilities;
import com.osir.terrametalcraft.api.te.SyncedTE;
import com.osir.terrametalcraft.common.block.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class TEStoneWorkTable extends SyncedTE implements IModularGuiHolder {
	public static final TileEntityType<TEStoneWorkTable> TYPE = TileEntityType.Builder
			.create(() -> new TEStoneWorkTable(), ModBlocks.stoneWorkTable).build(null);

	private static final TextureArea BACKGROUND = TextureArea
			.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/stone_work_table/background.png"));
	private static final TextureArea OUTLINE = TextureArea
			.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/stone_work_table/outline.png"));
	private static final TextureArea STONE = TextureArea
			.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/stone_work_table/stone.png"));
	private static final TextureArea STONE_SMALL = TextureArea.createTexture(
			new ResourceLocation(Main.MODID, "textures/gui/stone_work_table/stone.png"), 0, 0, 0.125f, 0.125f);

	private IItemHandlerModifiable inventory = new ItemStackHandler(2);

	public TEStoneWorkTable() {
		super(TYPE);
	}

	private void onStoneWork(ButtonClickData clickData) {
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
				for (int i = px - 1; i <= px + 1; i++) {
					for (int j = py - 1; j <= py + 1; j++) {
						if (i == px || j == py || rand.nextBoolean()) {
							this.setCarvingPoint(data, i, j, true);
						}
					}
				}
				if (this.world != null) {
					this.world.markChunkDirty(this.pos, this);
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
	}

	@Override
	public IGuiHolderCodec getCodec() {
		return TileEntityCodec.INSTANCE;
	}

	@Override
	public ModularGuiInfo createGuiInfo(PlayerEntity player) {
		return ModularGuiInfo.builder(176, 216).setBackground(BACKGROUND).addPlayerInventory(player.inventory, 8, 134)
				.addWidget(new SlotWidget(153, 7, this.inventory, 0))
				.addWidget(new SlotWidget(153, 101, this.inventory, 1, false, true))
				.addWidget(new ButtonWidget(6, 6, 112, 112, this::onStoneWork).setRenderer(this::renderWorkButton))
				.build(player);
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		return super.write(nbt);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
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