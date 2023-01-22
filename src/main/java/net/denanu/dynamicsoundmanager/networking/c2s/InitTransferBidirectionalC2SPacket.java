package net.denanu.dynamicsoundmanager.networking.c2s;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.s2c.RequestMoreDataBidirectionalS2CPacket;
import net.denanu.dynamicsoundmanager.networking.shared.FileSynchronizer;
import net.denanu.dynamicsoundmanager.utils.Permission;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InitTransferBidirectionalC2SPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		if (Permission.hasModificationPermission(player)) {
			final Identifier id = buf.readIdentifier();
			final String filename = buf.readString();
			final File file = InitTransferBidirectionalC2SPacket.toFile(ServerSoundGroups.path, id, filename);

			try {
				final int inboundKey = FileSynchronizer.openInbound(file);
				final int outboundKey = buf.readInt();
				ServerSoundGroups.metadata.changeVersion(id, filename);
				ServerSoundGroups.addConfig(id, filename);
				RequestMoreDataBidirectionalS2CPacket.send(player, inboundKey, outboundKey);
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static File toFile(final Path dir, final Identifier group, final String filename) {
		return InitTransferBidirectionalC2SPacket.toPath(dir, group).resolve(filename).toFile();
	}

	public static Path toPath(final Path dir, final Identifier group ) {
		return dir.resolve(group.getNamespace()).resolve(group.getPath());
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

	public static void send(final File file, final Identifier group) {
		ClientPlayNetworking.send(NetworkHandler.Bidirectional.INIT_TRANSFER, InitTransferBidirectionalC2SPacket.toBuf(file, group));
	}
}
