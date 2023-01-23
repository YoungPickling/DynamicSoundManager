package net.denanu.dynamicsoundmanager.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;

@Mixin(MusicTracker.class)
public interface IMusicTrackerAccessor {
	@Accessor SoundInstance getCurrent();
}
