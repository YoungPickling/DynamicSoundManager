package net.denanu.dynamicsoundmanager.networking;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.networking.bidirectional.InitTransferBidirectionalPacket;
import net.denanu.dynamicsoundmanager.networking.bidirectional.RequestMoreDataBidirectionalPacket;
import net.denanu.dynamicsoundmanager.networking.bidirectional.TransferDateBidirectionalPacket;
import net.denanu.dynamicsoundmanager.networking.c2s.RequestDownloadFilesC2SPacket;
import net.denanu.dynamicsoundmanager.networking.s2c.RequiredSoundsS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkHandler {
	public class Bidirectional {
		public static final Identifier INIT_TRANSFER =		Identifier.of(DynamicSoundManager.MOD_ID, "inittransfer");
		public static final Identifier REQUEST_DATA =		Identifier.of(DynamicSoundManager.MOD_ID, "request");
		public static final Identifier TRANSFER_DATA =		Identifier.of(DynamicSoundManager.MOD_ID, "transfer");
	}

	public class S2C {
		public static final Identifier REQUIRED_SOUNDS = 	Identifier.of(DynamicSoundManager.MOD_ID, "required_sounds");


		private static void register() {
			ClientPlayNetworking.registerGlobalReceiver(S2C.REQUIRED_SOUNDS, 	RequiredSoundsS2CPacket::receive);

			ClientPlayNetworking.registerGlobalReceiver(Bidirectional.INIT_TRANSFER, 	InitTransferBidirectionalPacket::receive);
			ClientPlayNetworking.registerGlobalReceiver(Bidirectional.REQUEST_DATA, 	RequestMoreDataBidirectionalPacket::receive);
			ClientPlayNetworking.registerGlobalReceiver(Bidirectional.TRANSFER_DATA, 	TransferDateBidirectionalPacket::receive);
		}
	}

	public class C2S {
		public static final Identifier REQUEST_FILES = 	Identifier.of(DynamicSoundManager.MOD_ID, "request_files");

		private static void register() {
			ServerPlayNetworking.registerGlobalReceiver(C2S.REQUEST_FILES, 		RequestDownloadFilesC2SPacket::receive);

			ServerPlayNetworking.registerGlobalReceiver(Bidirectional.INIT_TRANSFER, 	InitTransferBidirectionalPacket::receive);
			ServerPlayNetworking.registerGlobalReceiver(Bidirectional.REQUEST_DATA, 	RequestMoreDataBidirectionalPacket::receive);
			ServerPlayNetworking.registerGlobalReceiver(Bidirectional.TRANSFER_DATA, 	TransferDateBidirectionalPacket::receive);
		}
	}

	public static void registerC2SPackets() {
		C2S.register();
	}

	public static void registerS2CPackets() {
		S2C.register();
	}
}
