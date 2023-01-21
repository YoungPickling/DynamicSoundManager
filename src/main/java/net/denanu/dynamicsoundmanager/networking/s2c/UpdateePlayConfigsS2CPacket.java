package net.denanu.dynamicsoundmanager.networking.s2c;

import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class UpdateePlayConfigsS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final DynamicSoundConfigs config = new DynamicSoundConfigs(buf);
		ClientSoundGroupManager.modifyConfig(config);
	}

	public static PacketByteBuf toBuf(final DynamicSoundConfigs config) {
		final PacketByteBuf buf = PacketByteBufs.create();
		config.toBuf(buf);
		return buf;
	}

	public static void send(final ServerPlayerEntity player, final DynamicSoundConfigs config) {
		ServerPlayNetworking.send(player, NetworkHandler.S2C.SEND_SOUND_CONFIG_UPDATE, UpdateePlayConfigsS2CPacket.toBuf(config));
	}

	public static void send(final MinecraftServer server, final DynamicSoundConfigs config) {
		final PacketByteBuf buf = UpdateePlayConfigsS2CPacket.toBuf(config);

		for (final ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			ServerPlayNetworking.send(player, NetworkHandler.S2C.SEND_SOUND_CONFIG_UPDATE, buf);
		}
	}
}
