package net.denanu.dynamicsoundmanager.gui.widgets;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.player_api.PreviewSound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class AudioPlayerWidgetGlobals {
	public static final Identifier MUSIC_PREVIEW_ID = Identifier.of(DynamicSoundManager.MOD_ID, "music.preview");
	static final RegistryEntry<SoundEvent> MUSIC_PREVIEW = AudioPlayerWidgetGlobals.register(AudioPlayerWidgetGlobals.MUSIC_PREVIEW_ID);
	static final MusicSound MUSIC = new MusicSound(AudioPlayerWidgetGlobals.MUSIC_PREVIEW, 20, 600, false);

	static final PreviewSound PREVIEW_SOUND = new PreviewSound(Identifier.of(DynamicSoundManager.MOD_ID, "music.preview").toString());

	private static RegistryEntry<SoundEvent> register(final Identifier id) {
		return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

	public static void setup() {}
}
