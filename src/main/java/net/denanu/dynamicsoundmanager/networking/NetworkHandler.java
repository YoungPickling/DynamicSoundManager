package net.denanu.dynamicsoundmanager.networking;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.networking.c2s.DeleteSoundInGroupC2SPacket;
import net.denanu.dynamicsoundmanager.networking.c2s.InitTransferBidirectionalC2SPacket;
import net.denanu.dynamicsoundmanager.networking.c2s.RequestDownloadFilesC2SPacket;
import net.denanu.dynamicsoundmanager.networking.c2s.RequestMoreDataBidirectionalC2SPacket;
import net.denanu.dynamicsoundmanager.networking.c2s.TransferDateBidirectionalC2SPacket;
import net.denanu.dynamicsoundmanager.networking.c2s.UpdatePlayConfigsC2SPacket;
import net.denanu.dynamicsoundmanager.networking.s2c.InitTransferBidirectionalS2CPacket;
import net.denanu.dynamicsoundmanager.networking.s2c.RequestMoreDataBidirectionalS2CPacket;
import net.denanu.dynamicsoundmanager.networking.s2c.RequiredSoundsS2CPacket;
import net.denanu.dynamicsoundmanager.networking.s2c.TransferDateBidirectionalS2CPacket;
import net.denanu.dynamicsoundmanager.networking.s2c.UpdateePlayConfigsS2CPacket;
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
		public static final Identifier SEND_SOUND_CONFIG_UPDATE = Identifier.of(DynamicSoundManager.MOD_ID, "update_config");

		private static void register() {
			ClientPlayNetworking.registerGlobalReceiver(S2C.REQUIRED_SOUNDS, 	RequiredSoundsS2CPacket::receive);
			ClientPlayNetworking.registerGlobalReceiver(S2C.SEND_SOUND_CONFIG_UPDATE, UpdateePlayConfigsS2CPacket::receive);

			ClientPlayNetworking.registerGlobalReceiver(Bidirectional.INIT_TRANSFER, 	InitTransferBidirectionalS2CPacket::receive);
			ClientPlayNetworking.registerGlobalReceiver(Bidirectional.REQUEST_DATA, 	RequestMoreDataBidirectionalS2CPacket::receive);
			ClientPlayNetworking.registerGlobalReceiver(Bidirectional.TRANSFER_DATA, 	TransferDateBidirectionalS2CPacket::receive);
		}
	}

	public class C2S {
		public static final Identifier REQUEST_FILES 			= Identifier.of(DynamicSoundManager.MOD_ID, "request_files");
		public static final Identifier SEND_SOUND_CONFIG_UPDATE = Identifier.of(DynamicSoundManager.MOD_ID, "update_config");
		public static final Identifier DELETE_SOUND				= Identifier.of(DynamicSoundManager.MOD_ID, "delete_file");

		private static void register() {
			ServerPlayNetworking.registerGlobalReceiver(C2S.REQUEST_FILES, 				RequestDownloadFilesC2SPacket::receive);
			ServerPlayNetworking.registerGlobalReceiver(C2S.SEND_SOUND_CONFIG_UPDATE, 	UpdatePlayConfigsC2SPacket::receive);
			ServerPlayNetworking.registerGlobalReceiver(C2S.DELETE_SOUND, 				DeleteSoundInGroupC2SPacket::receive);

			ServerPlayNetworking.registerGlobalReceiver(Bidirectional.INIT_TRANSFER, 	InitTransferBidirectionalC2SPacket::receive);
			ServerPlayNetworking.registerGlobalReceiver(Bidirectional.REQUEST_DATA, 	RequestMoreDataBidirectionalC2SPacket::receive);
			ServerPlayNetworking.registerGlobalReceiver(Bidirectional.TRANSFER_DATA, 	TransferDateBidirectionalC2SPacket::receive);
		}
	}

	public static void registerC2SPackets() {
		C2S.register();
	}

	public static void registerS2CPackets() {
		S2C.register();
	}
}
