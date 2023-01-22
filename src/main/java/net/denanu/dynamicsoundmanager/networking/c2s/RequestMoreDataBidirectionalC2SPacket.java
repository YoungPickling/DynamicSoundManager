package net.denanu.dynamicsoundmanager.networking.c2s;

import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.s2c.TransferDateBidirectionalS2CPacket;
import net.denanu.dynamicsoundmanager.utils.Permission;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class RequestMoreDataBidirectionalC2SPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		if (Permission.hasModificationPermission(player)) {
			final int inboundKey = buf.readInt();
			final int outboundKey = buf.readInt();
			TransferDateBidirectionalS2CPacket.send(player, inboundKey, outboundKey);
		}
	}

	private static PacketByteBuf toBuf(final int inboundKey, final int outboundKey) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeInt(inboundKey);
		buf.writeInt(outboundKey);

		return buf;
	}

	public static void send(final int inboundKey, final int outboundKey) {
		ClientPlayNetworking.send(NetworkHandler.Bidirectional.REQUEST_DATA, RequestMoreDataBidirectionalC2SPacket.toBuf(inboundKey, outboundKey));
	}
}
