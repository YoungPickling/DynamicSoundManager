package net.denanu.dynamicsoundmanager.networking;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.networking.s2c.RequiredSoundsS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkHandler {
	public class S2C {
		public static final Identifier REQUIRED_SOUNDS = Identifier.of(DynamicSoundManager.MOD_ID, "required_sounds");

		private static void register() {
			ClientPlayNetworking.registerGlobalReceiver(S2C.REQUIRED_SOUNDS, RequiredSoundsS2CPacket::receive);
		}
	}

	public class C2S {
		private static void register() {
		}
	}

	public static void registerC2SPackets() {
		C2S.register();
	}

	public static void registerS2CPackets() {
		S2C.register();
	}
}
