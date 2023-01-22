package net.denanu.dynamicsoundmanager.networking.s2c;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.c2s.RequestMoreDataBidirectionalC2SPacket;
import net.denanu.dynamicsoundmanager.networking.shared.FileSynchronizer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InitTransferBidirectionalS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final Identifier id = buf.readIdentifier();
		final String str = buf.readString();

		final File file = InitTransferBidirectionalS2CPacket.toFile(ClientSoundGroupManager.getChach(client), id, str);
		try {
			final int inboundKey = FileSynchronizer.openInbound(file);
			final int outboundKey = buf.readInt();

			ClientSoundGroupManager.metadata.changeVersion(id, str, buf.readLong());
			RequestMoreDataBidirectionalC2SPacket.send(inboundKey, outboundKey);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static File toFile(final Path dir, final Identifier group, final String filename) {
		try {
			return dir.resolve(group.getNamespace()).resolve(group.getPath()).resolve(filename).toFile();
		}
		catch (final RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private static PacketByteBuf toBuf(final File file, final Identifier group) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeIdentifier(group);
		buf.writeString(file.getName());
		try {
			buf.writeInt(FileSynchronizer.openOutbound(file));
			return buf;
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static PacketByteBuf toBuf(final File file, final Identifier group, final long version) {
		final PacketByteBuf buf = InitTransferBidirectionalS2CPacket.toBuf(file, group);
		buf.writeLong(version);
		return buf;
	}

	public static void send(final ServerPlayerEntity player, final File file, final Identifier group, final long version) {
		ServerPlayNetworking.send(player, NetworkHandler.Bidirectional.INIT_TRANSFER, InitTransferBidirectionalS2CPacket.toBuf(file, group, version));
	}

	public static void send(final ServerPlayerEntity player, final Identifier group, final String filename) {
		final File file = InitTransferBidirectionalS2CPacket.toFile(ServerSoundGroups.path, group, filename);
		InitTransferBidirectionalS2CPacket.send(player, file, group, ServerSoundGroups.metadata.get(group, filename));
	}
}
