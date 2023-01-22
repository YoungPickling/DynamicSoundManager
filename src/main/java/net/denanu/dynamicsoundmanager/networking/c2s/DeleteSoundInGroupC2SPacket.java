package net.denanu.dynamicsoundmanager.networking.c2s;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.utils.Permission;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DeleteSoundInGroupC2SPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		if (Permission.hasModificationPermission(player)) {
			final Identifier id = buf.readIdentifier();
			final String key = buf.readString();

			ServerSoundGroups.getSounds().get(id).delete(id, key);
			TransferDateBidirectionalC2SPacket.update(server);
			ServerSoundGroups.metadata.delete(id, key);

			TransferDateBidirectionalC2SPacket.update(server);
		}
	}

	private static PacketByteBuf toBuf(final Identifier id, final String key) {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeIdentifier(id);
		buf.writeString(key);
		return buf;
	}

	public static void send(final Identifier id, final String key) {
		ClientPlayNetworking.send(NetworkHandler.C2S.DELETE_SOUND, DeleteSoundInGroupC2SPacket.toBuf(id, key));
	}
}
