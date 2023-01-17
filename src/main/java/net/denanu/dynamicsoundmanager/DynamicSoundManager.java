package net.denanu.dynamicsoundmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.networking.NetworkHandler;
import net.denanu.dynamicsoundmanager.player_api.DebugSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DynamicSoundManager implements ModInitializer {

	public static String MOD_ID = "dynamicsoundmanager";

	public static final Logger LOGGER = LoggerFactory.getLogger(DynamicSoundManager.MOD_ID);

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(ServerSoundGroups::setup);
		DebugSounds.setup();
		NetworkHandler.registerC2SPackets();

		Identifier.of("::", DynamicSoundManager.MOD_ID);

		final SoundEvent TestEvent = Registry.SOUND_EVENT.get(DebugSounds.TEST_ID);
		DynamicSoundManager.LOGGER.info(TestEvent.toString());
	}
}
