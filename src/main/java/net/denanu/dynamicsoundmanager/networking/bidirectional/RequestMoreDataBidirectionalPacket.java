package net.denanu.dynamicsoundmanager.networking.bidirectional;

import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
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

public class RequestMoreDataBidirectionalPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final int inboundKey = buf.readInt();
		final int outboundKey = buf.readInt();
		TransferDateBidirectionalPacket.send(player, inboundKey, outboundKey);
	}

	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final int inboundKey = buf.readInt();
		final int outboundKey = buf.readInt();
		TransferDateBidirectionalPacket.send(inboundKey, outboundKey);
	}

	private static PacketByteBuf toBuf(final int inboundKey, final int outboundKey) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeInt(inboundKey);
		buf.writeInt(outboundKey);

		return buf;
	}

	public static void send(final int inboundKey, final int outboundKey) {
		ClientPlayNetworking.send(NetworkHandler.Bidirectional.REQUEST_DATA, RequestMoreDataBidirectionalPacket.toBuf(inboundKey, outboundKey));
	}

	public static void send(final ServerPlayerEntity player, final int inboundKey, final int outboundKey) {
		ServerPlayNetworking.send(player, NetworkHandler.Bidirectional.REQUEST_DATA, RequestMoreDataBidirectionalPacket.toBuf(inboundKey, outboundKey));
	}
}
