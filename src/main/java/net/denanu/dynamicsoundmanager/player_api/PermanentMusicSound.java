package net.denanu.dynamicsoundmanager.player_api;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;

public class PermanentMusicSound extends MusicSound {
	public PermanentMusicSound(final RegistryEntry<SoundEvent> sound, final int minDelay, final int maxDelay, final boolean replaceCurrentMusic) {
		super(sound, minDelay, maxDelay, replaceCurrentMusic);
	}
}
