package net.denanu.dynamicsoundmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;

public class DynamicSoundManager implements ModInitializer {

	public static String MOD_ID = "dynamicsoundmanager";

	public static final Logger LOGGER = LoggerFactory.getLogger(DynamicSoundManager.MOD_ID);

	@Override
	public void onInitialize() {
		ServerSoundGroups.register(Identifier.of(DynamicSoundManager.MOD_ID, "test"));

		ServerLifecycleEvents.SERVER_STARTED.register(ServerSoundGroups::setup);

		NetworkHandler.registerC2SPackets();

	}
}
