package net.denanu.dynamicsoundmanager.networking.c2s;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.gui.Utils;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class UpdatePlayConfigsC2SPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		if (Utils.hasModificationPermission(player)) {
			final DynamicSoundConfigs config = new DynamicSoundConfigs(buf);
			ServerSoundGroups.modifyConfig(server, config);
		}

	}
	public static PacketByteBuf toBuf(final DynamicSoundConfigs config) {
		final PacketByteBuf buf = PacketByteBufs.create();
		config.toBuf(buf);
		return buf;
	}

	public static void send(final DynamicSoundConfigs config) {
		ClientPlayNetworking.send(NetworkHandler.C2S.SEND_SOUND_CONFIG_UPDATE, UpdatePlayConfigsC2SPacket.toBuf(config));
	}
}
