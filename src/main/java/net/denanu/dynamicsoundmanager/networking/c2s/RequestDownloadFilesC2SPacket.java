package net.denanu.dynamicsoundmanager.networking.c2s;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.networking.s2c.InitTransferBidirectionalS2CPacket;
import net.denanu.dynamicsoundmanager.utils.FileKey;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class RequestDownloadFilesC2SPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {

		final Collection<String> files = buf.readCollection(ArrayList::new, PacketByteBuf::readString);

		for (final String file : files) {
			final FileKey key = new FileKey(file);

			InitTransferBidirectionalS2CPacket.send(player, key.getId(), key.getFileName());
		}

	}
	public static PacketByteBuf toBuf(final List<String> files) {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeCollection(files, (buf2, file) -> {
			buf2.writeString(file);
		});
		return buf;
	}

	public static void send(final List<String> files) {
		if (files.size() > 0) {
			while (MinecraftClient.getInstance().getNetworkHandler() == null) {

			}
			ClientPlayNetworking.send(NetworkHandler.C2S.REQUEST_FILES, RequestDownloadFilesC2SPacket.toBuf(files));
		}
	}
}
