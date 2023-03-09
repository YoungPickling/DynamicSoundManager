package net.denanu.dynamicsoundmanager.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.util.Identifier;

@Mixin(SoundManager.class)
public interface SoundManagerMixin {
	@Accessor Map<Identifier, WeightedSoundSet> getSounds();
}
