package net.denanu.dynamicsoundmanager.player_api;

import net.denanu.dynamicsoundmanager.mixin.SoundManagerMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class DynamicWeightedSoundSet extends WeightedSoundSet {

	public DynamicWeightedSoundSet(final Identifier id, final String subtitle) {
		super(id, subtitle);
		final SoundManagerMixin manager = (SoundManagerMixin)MinecraftClient.getInstance().getSoundManager();
		manager.getSounds().put(id, this);
	}

}
