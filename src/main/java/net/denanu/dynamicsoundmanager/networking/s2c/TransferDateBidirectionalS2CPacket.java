package net.denanu.dynamicsoundmanager.networking.s2c;

import java.io.FileInputStream;
import java.io.IOException;

import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.c2s.RequestMoreDataBidirectionalC2SPacket;
import net.denanu.dynamicsoundmanager.networking.shared.FileSynchronizer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class TransferDateBidirectionalS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final int inboundKey 	= buf.readInt();
		final int outboundKey 	= buf.readInt();
		if (TransferDateBidirectionalS2CPacket.run(buf, inboundKey)) {
			RequestMoreDataBidirectionalC2SPacket.send(inboundKey, outboundKey);
		}
	}

	public static boolean run(final PacketByteBuf buf, final int inboundKey) {
		final byte[] data = buf.readByteArray();
		final boolean isDone = buf.readBoolean();

		try {
			FileSynchronizer.inbounds.get(inboundKey).write(data);

			if (isDone) {
				FileSynchronizer.closeInbound(inboundKey);
				return false;
			}

			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	public static PacketByteBuf toBuf(final int inboundKey, final int outboundKey) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeInt(inboundKey);
		buf.writeInt(outboundKey);

		try {
			final FileInputStream outbound = FileSynchronizer.outbounds.get(outboundKey);
			byte[] bytes = new byte[Math.min(1024, outbound.available())];
			bytes = outbound.readNBytes(bytes.length);

			buf.writeByteArray(bytes);

			final boolean done = outbound.available() == 0;
			buf.writeBoolean(done);
			if (done) {
				FileSynchronizer.closeOutbound(outboundKey);
			}
		}
		catch (final IOException e) {
			e.printStackTrace();
			FileSynchronizer.closeOutbound(outboundKey);
		}


		return buf;
	}

	public static void send(final ServerPlayerEntity player, final int inboundKey, final int outboundKey) {
		ServerPlayNetworking.send(player, NetworkHandler.Bidirectional.TRANSFER_DATA, TransferDateBidirectionalS2CPacket.toBuf(inboundKey, outboundKey));
	}
}
