package net.denanu.dynamicsoundmanager.networking.c2s;

import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.s2c.RequiredSoundsS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ReloadResourcesC2SPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		RequiredSoundsS2CPacket.send(player);
	}


	private static PacketByteBuf toBuf() {
		return PacketByteBufs.create();
	}

	public static void send() {
		if (MinecraftClient.getInstance().getNetworkHandler() != null) {
			ClientPlayNetworking.send(NetworkHandler.C2S.RELOAD_RESOURCES, ReloadResourcesC2SPacket.toBuf());
		}
	}
}
