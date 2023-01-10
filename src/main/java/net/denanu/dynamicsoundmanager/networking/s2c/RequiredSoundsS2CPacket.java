package net.denanu.dynamicsoundmanager.networking.s2c;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import net.denanu.dynamicsoundmanager.groups.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.groups.SoundGroup;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.utils.FileModificationUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RequiredSoundsS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {

		final Path cache = ClientSoundGroupManager.getChach(client);

		for (final SoundGroup group : buf.readCollection(ArrayList::new, SoundGroup::new)) {

			final Path groupPath = cache.resolve(group.getId().toString());
			FileModificationUtils.mkdirIfAbsent(groupPath.toFile());
		}
	}

	public static PacketByteBuf toBuf(final Path path, final HashMap<Identifier, SoundGroup> sounds) {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeCollection(sounds.entrySet(), (buf2, entry) -> {
			entry.getValue().writeBuf(buf2);
		});
		return buf;
	}

	public static void send(final ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, NetworkHandler.S2C.REQUIRED_SOUNDS, RequiredSoundsS2CPacket.toBuf(ServerSoundGroups.getPath(), ServerSoundGroups.getSounds()));
	}
}
