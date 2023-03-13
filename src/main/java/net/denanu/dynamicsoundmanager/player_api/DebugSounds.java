package net.denanu.dynamicsoundmanager.player_api;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class DebugSounds {
	public static Identifier TEST_ID = Identifier.of(DynamicSoundManager.MOD_ID, "test");

	public static RegistryEntry<SoundEvent> TEST = DebugSounds.register(DebugSounds.TEST_ID);

	private static RegistryEntry<SoundEvent> register(final Identifier id) {
		final SoundEvent event = SoundEvent.of(id);
		ServerSoundGroups.register(id);
		return Registry.registerReference(Registries.SOUND_EVENT, id, event);
	}

	public static void setup() {}
}
