package net.denanu.dynamicsoundmanager.networking.bidirectional;

import java.io.FileInputStream;
import java.io.IOException;

import net.denanu.dynamicsoundmanager.gui.Utils;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.s2c.RequiredSoundsS2CPacket;
import net.denanu.dynamicsoundmanager.networking.shared.FileSynchronizer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class TransferDateBidirectionalPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		if (Utils.hasModificationPermission(player)) {
			final int inboundKey 	= buf.readInt();
			final int outboundKey 	= buf.readInt();
			if (TransferDateBidirectionalPacket.run(buf, inboundKey)) {
				RequestMoreDataBidirectionalPacket.send(player, inboundKey, outboundKey);
			}
			else {
				TransferDateBidirectionalPacket.update(server);
			}
		}
	}

	private static void update(final MinecraftServer server) {
		for (final ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			RequiredSoundsS2CPacket.send(player);
		}
	}

	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final int inboundKey 	= buf.readInt();
		final int outboundKey 	= buf.readInt();
		if (TransferDateBidirectionalPacket.run(buf, inboundKey)) {
			RequestMoreDataBidirectionalPacket.send(inboundKey, outboundKey);
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

	public static void send(final int inboundKey, final int outboundKey) {
		ClientPlayNetworking.send(NetworkHandler.Bidirectional.TRANSFER_DATA, TransferDateBidirectionalPacket.toBuf(inboundKey, outboundKey));
	}

	public static void send(final ServerPlayerEntity player, final int inboundKey, final int outboundKey) {
		ServerPlayNetworking.send(player, NetworkHandler.Bidirectional.TRANSFER_DATA, TransferDateBidirectionalPacket.toBuf(inboundKey, outboundKey));
	}
}
