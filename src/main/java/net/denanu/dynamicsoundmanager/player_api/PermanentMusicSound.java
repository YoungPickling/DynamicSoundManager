package net.denanu.dynamicsoundmanager.player_api;

import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;

public class PermanentMusicSound extends MusicSound {
	public PermanentMusicSound(final SoundEvent sound, final int minDelay, final int maxDelay, final boolean replaceCurrentMusic) {
		super(sound, minDelay, maxDelay, replaceCurrentMusic);
	}
}
