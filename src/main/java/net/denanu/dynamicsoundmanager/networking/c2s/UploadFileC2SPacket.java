package net.denanu.dynamicsoundmanager.networking.c2s;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class UploadFileC2SPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final String filename 	= buf.readString();
		final Identifier group 	= buf.readIdentifier();
		final byte[] buff 		= buf.readByteArray();

		try {
			final FileOutputStream out = new FileOutputStream(ServerSoundGroups.getSounds().get(group).getPath().resolve(filename).toFile());
			out.write(buff);
			out.close();

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static PacketByteBuf toBuf(final File file, final Identifier group) throws IOException {
		final FileInputStream stream = new FileInputStream(file);
		final byte[] buff = stream.readAllBytes();
		stream.close();

		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeString(file.getName());
		buf.writeIdentifier(group);
		buf.writeByteArray(buff);
		return buf;
	}

	public static void send(final File file, final Identifier group) {
		try {
			ServerSoundGroups.metadata.changeVersion(group, file);
			ClientPlayNetworking.send(NetworkHandler.C2S.FILE_UPLOAD, UploadFileC2SPacket.toBuf(file, group));
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
