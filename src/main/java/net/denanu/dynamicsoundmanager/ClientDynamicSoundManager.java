package net.denanu.dynamicsoundmanager;

import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.fabricmc.api.ClientModInitializer;

public class ClientDynamicSoundManager implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		NetworkHandler.registerS2CPackets();
	}

}
