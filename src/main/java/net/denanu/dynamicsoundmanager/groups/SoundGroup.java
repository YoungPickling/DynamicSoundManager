package net.denanu.dynamicsoundmanager.groups;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class SoundGroup {
	private final Identifier id;
	public List<DynamicSoundConfigs> sounds = new ArrayList<>();

	public SoundGroup(final Identifier id) {
		this.id = id;
	}

	public SoundGroup(final PacketByteBuf buf) {
		this.id = buf.readIdentifier();
	}

	public void writeBuf(final PacketByteBuf buf) {
		buf.writeIdentifier(this.id);
	}

	public Identifier getId() {
		return this.id;
	}
	public Path getPath() {
		return ServerSoundGroups.path.resolve(this.getId().getNamespace()).resolve(this.getId().getPath());
	}

	private int getWeight() {
		int i = 0;
		for (final DynamicSoundConfigs soundContainer : this.sounds) {
			i += soundContainer.getWeight();
		}
		return i;
	}

	public DynamicSoundConfigs getConfig(final long seed) {
		final int i = this.getWeight();
		if (this.sounds.isEmpty() || i == 0) {
			return null;
		}
		int j = Random.create(seed).nextInt(i);
		for (final DynamicSoundConfigs soundContainer : this.sounds) {
			j -= soundContainer.getWeight();
			if (j >= 0) {
				continue;
			}
			return soundContainer;
		}
		return null;
	}
}
