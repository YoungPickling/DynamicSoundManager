package net.denanu.dynamicsoundmanager.networking.bidirectional;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.shared.FileSynchronizer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InitTransferBidirectionalPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final Identifier id = buf.readIdentifier();
		final String filename = buf.readString();
		final File file = InitTransferBidirectionalPacket.toFile(ServerSoundGroups.path, id, filename);

		try {
			final int inboundKey = FileSynchronizer.openInbound(file);
			final int outboundKey = buf.readInt();
			ServerSoundGroups.metadata.changeVersion(id, filename);
			ServerSoundGroups.addConfig(id, filename);
			RequestMoreDataBidirectionalPacket.send(player, inboundKey, outboundKey);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final Identifier id = buf.readIdentifier();
		final String str = buf.readString();

		final File file = InitTransferBidirectionalPacket.toFile(ClientSoundGroupManager.getChach(client), id, str);
		try {
			final int inboundKey = FileSynchronizer.openInbound(file);
			final int outboundKey = buf.readInt();

			ClientSoundGroupManager.metadata.changeVersion(id, str, buf.readLong());
			RequestMoreDataBidirectionalPacket.send(inboundKey, outboundKey);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static File toFile(final Path dir, final Identifier group, final String filename) {
		return dir.resolve(group.getNamespace()).resolve(group.getPath()).resolve(filename).toFile();
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
		final PacketByteBuf buf = InitTransferBidirectionalPacket.toBuf(file, group);
		buf.writeLong(version);
		return buf;
	}

	public static void send(final File file, final Identifier group) {
		ClientPlayNetworking.send(NetworkHandler.Bidirectional.INIT_TRANSFER, InitTransferBidirectionalPacket.toBuf(file, group));
	}

	public static void send(final ServerPlayerEntity player, final File file, final Identifier group, final long version) {
		ServerPlayNetworking.send(player, NetworkHandler.Bidirectional.INIT_TRANSFER, InitTransferBidirectionalPacket.toBuf(file, group, version));
	}

	public static void send(final ServerPlayerEntity player, final Identifier group, final String filename) {
		final File file = InitTransferBidirectionalPacket.toFile(ServerSoundGroups.path, group, filename);
		InitTransferBidirectionalPacket.send(player, file, group, ServerSoundGroups.metadata.get(group, filename));
	}
}
