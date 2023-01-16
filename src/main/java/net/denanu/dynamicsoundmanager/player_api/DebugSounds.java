package net.denanu.dynamicsoundmanager.player_api;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DebugSounds {
	public static Identifier TEST_ID = Identifier.of(DynamicSoundManager.MOD_ID, "test");

	public static SoundEvent TEST = DebugSounds.register(DebugSounds.TEST_ID);

	private static SoundEvent register(final Identifier id) {
		final SoundEvent event = new SoundEvent(id);
		return Registry.register(Registry.SOUND_EVENT, id, event);
	}

	public static void setup() {}
}
