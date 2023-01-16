package net.denanu.dynamicsoundmanager.utils;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class DebugSound {
	public static SoundEvent TEST = new SoundEvent(Identifier.of(DynamicSoundManager.MOD_ID, "test"));
}
