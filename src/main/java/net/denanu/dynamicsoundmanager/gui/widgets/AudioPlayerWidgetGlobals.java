package net.denanu.dynamicsoundmanager.gui.widgets;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.player_api.PreviewSound;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AudioPlayerWidgetGlobals {
	public static final Identifier MUSIC_PREVIEW_ID = Identifier.of(DynamicSoundManager.MOD_ID, "music.preview");
	static final SoundEvent MUSIC_PREVIEW = AudioPlayerWidgetGlobals.register(AudioPlayerWidgetGlobals.MUSIC_PREVIEW_ID);
	static final MusicSound MUSIC = new MusicSound(AudioPlayerWidgetGlobals.MUSIC_PREVIEW, 20, 600, false);

	static final PreviewSound PREVIEW_SOUND = new PreviewSound(Identifier.of(DynamicSoundManager.MOD_ID, "music.preview").toString());

	private static SoundEvent register(final Identifier id) {
		return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
	}

	public static void setup() {}
}
