package net.denanu.dynamicsoundmanager.player_api;

import java.util.List;

import net.denanu.dynamicsoundmanager.mixin.SoundManagerMixin;
import net.denanu.dynamicsoundmanager.mixin.client.IWeightedSoundSetMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundContainer;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

@Environment(value=EnvType.CLIENT)
public class DynamicWeightedSoundSet extends WeightedSoundSet {
	public DynamicWeightedSoundSet(final Identifier id, final String subtitle) {
		super(id, subtitle);
		final SoundManagerMixin manager = (SoundManagerMixin)MinecraftClient.getInstance().getSoundManager();
		manager.getSounds().put(id, this);
	}

	public void modifySound(final DynamicSoundConfigs config) {
		final List<SoundContainer<Sound>> sounds = ((IWeightedSoundSetMixin)this).getSounds();

		int idx = 0;
		boolean found = false;
		for (final SoundContainer<Sound> sound_container : sounds) {
			final Sound sound = sound_container.getSound(null);
			if (sound instanceof final DynamicSound dynamicSound && dynamicSound.getKey().equals(config.getKey())) {
				found = true;
				break;
			}
			idx++;
		}
		if (found) {
			sounds.set(idx, new DynamicSound(config));
		}
	}

	public void addSound(final DynamicSoundConfigs config) {
		if (this.doesNotHaveSound(config.getKey())) {
			this.add(new DynamicSound(
					config
					));
		}
	}

	private boolean doesNotHaveSound(final String key) {
		// TODO Auto-generated method stub
		return true;
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
