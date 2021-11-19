package com.osir.terrametalcraft.common.te;

import com.github.zi_jing.cuckoolib.client.render.IWidgetRenderer;
import com.github.zi_jing.cuckoolib.client.render.TextureArea;
import com.github.zi_jing.cuckoolib.gui.IGuiHolderCodec;
import com.github.zi_jing.cuckoolib.gui.IModularGuiHolder;
import com.github.zi_jing.cuckoolib.gui.ModularGuiInfo;
import com.github.zi_jing.cuckoolib.gui.impl.IPlanInfoTileEntity;
import com.github.zi_jing.cuckoolib.gui.impl.TileEntityCodec;
import com.osir.terrametalcraft.Main;
import com.osir.terrametalcraft.api.te.SyncedTE;
import com.osir.terrametalcraft.common.block.ModBlocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TEPotteryWorkTable extends SyncedTE implements ITickableTileEntity, IModularGuiHolder, IPlanInfoTileEntity {
	public static final TileEntityType<TEPotteryWorkTable> TYPE = TileEntityType.Builder.of(() -> new TEPotteryWorkTable(), ModBlocks.POTTERY_WORK_TABLE).build(null);

	private static final TextureArea BACKGROUND = TextureArea.createFullTexture(new ResourceLocation(Main.MODID, "textures/gui/pottery_work_table/background.png"));
	private static final ITextComponent TITLE = new TranslationTextComponent("modulargui.pottery_work_table.name");

	public TEPotteryWorkTable() {
		super(TYPE);
	}

	@Override
	public void tick() {

	}

	@Override
	public int getPlanCount() {
		return 0;
	}

	@Override
	public IWidgetRenderer getOverlayRenderer(int count) {
		return null;
	}

	@Override
	public void callback(int count) {

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
		return ModularGuiInfo.builder(176, 216).setBackground(BACKGROUND).build(player);
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