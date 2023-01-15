package net.denanu.dynamicsoundmanager.networking.s2c;

import java.util.ArrayList;
import java.util.Map;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.groups.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

public class RequiredSoundsS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {

		ClientSoundGroupManager.setup();

		final ArrayList<Pair<String, Double>> data = buf.readCollection(ArrayList::new, buf2 -> {
			final String key = buf2.readString();
			final double version = buf2.readLong();
			return new Pair<>(key, version);
		});

		final Map<String, Double> nonMatching = ClientSoundGroupManager.metadata.getNonMatchingVersions(data);
		DynamicSoundManager.LOGGER.info(nonMatching.toString());
	}

	public static PacketByteBuf toBuf() {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeCollection(ServerSoundGroups.metadata.getVersions().entrySet(), (buf2, entry) -> {
			buf2.writeString(entry.getKey());
			buf2.writeDouble(entry.getValue());
		});
		return buf;
	}

	public static void send(final ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, NetworkHandler.S2C.REQUIRED_SOUNDS, RequiredSoundsS2CPacket.toBuf());
	}
}
