package com.osir.terrametalcraft.api.te;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;

import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants.NBT;

public abstract class SyncedTE extends TileEntity {
	private List<Pair<Integer, byte[]>> updateData = new ArrayList<Pair<Integer, byte[]>>();

	public SyncedTE(TileEntityType tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public abstract void writeInitData(PacketBuffer buf);

	public abstract void receiveInitData(PacketBuffer buf);

	public abstract void receiveCustomData(int discriminator, PacketBuffer buf);

	public void writeCustomData(int index, Consumer<PacketBuffer> dataWriter) {
		PacketBuffer packet = new PacketBuffer(Unpooled.buffer());
		dataWriter.accept(packet);
		this.updateData.add(Pair.of(index, Arrays.copyOfRange(packet.array(), 0, packet.writerIndex())));
		BlockState state = this.getBlockState();
		this.world.notifyBlockUpdate(this.getPos(), state, state, 0);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		ListNBT list = new ListNBT();
		for (Pair<Integer, byte[]> entry : this.updateData) {
			CompoundNBT entryNBT = new CompoundNBT();
			entryNBT.putInt("i", entry.getLeft());
			entryNBT.putByteArray("d", entry.getRight());
			list.add(entryNBT);
		}
		this.updateData.clear();
		nbt.put("d", list);
		return new SUpdateTileEntityPacket(this.getPos(), 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT nbt = pkt.getNbtCompound();
		ListNBT list = nbt.getList("d", NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			CompoundNBT entryNBT = list.getCompound(i);
			this.receiveCustomData(entryNBT.getInt("i"),
					new PacketBuffer(Unpooled.copiedBuffer(entryNBT.getByteArray("d"))));
		}
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = super.getUpdateTag();
		PacketBuffer packet = new PacketBuffer(Unpooled.buffer());
		this.writeInitData(packet);
		nbt.putByteArray("d", Arrays.copyOfRange(packet.array(), 0, packet.writerIndex()));
		return nbt;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		super.handleUpdateTag(state, tag);
		this.receiveInitData(new PacketBuffer(Unpooled.copiedBuffer(tag.getByteArray("d"))));
	}
}