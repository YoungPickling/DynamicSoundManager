package net.denanu.dynamicsoundmanager.networking.s2c;

import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.c2s.TransferDateBidirectionalC2SPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class RequestMoreDataBidirectionalS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final int inboundKey = buf.readInt();
		final int outboundKey = buf.readInt();
		TransferDateBidirectionalC2SPacket.send(inboundKey, outboundKey);
	}

	private static PacketByteBuf toBuf(final int inboundKey, final int outboundKey) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeInt(inboundKey);
		buf.writeInt(outboundKey);

		return buf;
	}

	public static void send(final ServerPlayerEntity player, final int inboundKey, final int outboundKey) {
		ServerPlayNetworking.send(player, NetworkHandler.Bidirectional.REQUEST_DATA, RequestMoreDataBidirectionalS2CPacket.toBuf(inboundKey, outboundKey));
	}
}
