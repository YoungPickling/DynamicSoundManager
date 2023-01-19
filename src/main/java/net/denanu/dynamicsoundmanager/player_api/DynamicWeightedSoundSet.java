package net.denanu.dynamicsoundmanager.player_api;

import java.util.List;

import net.denanu.dynamicsoundmanager.mixin.SoundManagerMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.random.Random;

@Environment(value=EnvType.CLIENT)
public class DynamicWeightedSoundSet extends WeightedSoundSet {
	public DynamicWeightedSoundSet(final Identifier id, final String subtitle) {
		super(id, subtitle);
		final SoundManagerMixin manager = (SoundManagerMixin)MinecraftClient.getInstance().getSoundManager();
		manager.getSounds().put(id, this);
	}

	public void addSound(final DynamicSoundConfigs config) {
		this.add(new DynamicSound(
				config.getId(),
				ConstantFloatProvider.create(config.getVolume()),
				ConstantFloatProvider.create(config.getPitch()),
				1,
				config.getRegistrationType(),
				config.getStream(),
				config.getPreload(),
				config.getAttenuation(),
				config.getKey()
				));
	}

	@Override
	public Sound getSound(final Random random) {
		return super.getSound(random);
	}

	public void addSounds(final List<DynamicSoundConfigs> sounds) {
		for (final DynamicSoundConfigs sound : sounds) {
			this.addSound(sound);
		}
	}
}
