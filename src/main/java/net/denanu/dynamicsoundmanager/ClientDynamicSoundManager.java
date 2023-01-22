package net.denanu.dynamicsoundmanager;

import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.gui.widgets.AudioPlayerWidgetGlobals;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class ClientDynamicSoundManager implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		NetworkHandler.registerS2CPackets();
		AudioPlayerWidgetGlobals.setup();

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> ClientSoundGroupManager.soundIds.clear());
	}

}
