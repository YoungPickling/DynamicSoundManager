package net.denanu.dynamicsoundmanager.networking.s2c;

import java.util.ArrayList;
import java.util.List;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.mixin.SoundManagerMixin;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.c2s.RequestDownloadFilesC2SPacket;
import net.denanu.dynamicsoundmanager.player_api.DebugSounds;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

public class RequiredSoundsS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {

		ClientSoundGroupManager.setup();

		final ArrayList<Pair<String, Long>> data = buf.readCollection(ArrayList::new, buf2 -> {
			final String key = buf2.readString();
			final long version = buf2.readLong();
			return new Pair<>(key, version);
		});

		ClientSoundGroupManager.soundIds = buf.readCollection(ArrayList::new, PacketByteBuf::readIdentifier);
		ClientSoundGroupManager.populateSounds(data, ClientSoundGroupManager.addClientSoundData());


		final List<String> nonMatching = ClientSoundGroupManager.metadata.getNonMatchingVersions(data);
		RequestDownloadFilesC2SPacket.send(nonMatching);

		final WeightedSoundSet sound = ((SoundManagerMixin)client.getSoundManager()).getSounds().get(DebugSounds.TEST_ID);
		DynamicSoundManager.LOGGER.info(sound.toString());
	}

	public static PacketByteBuf toBuf() {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeCollection(ServerSoundGroups.metadata.getVersions().entrySet(), (buf2, entry) -> {
			buf2.writeString(entry.getKey());
			buf2.writeLong(entry.getValue());
		});

		buf.writeCollection(ServerSoundGroups.sounds.entrySet(), (buf2, entry) -> {
			buf2.writeIdentifier(entry.getKey());
		});

		return buf;
	}

	public static void send(final ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, NetworkHandler.S2C.REQUIRED_SOUNDS, RequiredSoundsS2CPacket.toBuf());
	}
}
