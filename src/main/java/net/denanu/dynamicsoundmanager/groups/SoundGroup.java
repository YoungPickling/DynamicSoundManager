package net.denanu.dynamicsoundmanager.groups;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
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
		this.sounds = buf.readList(DynamicSoundConfigs::new);
	}

	public void writeBuf(final PacketByteBuf buf) {
		buf.writeIdentifier(this.id);
		buf.writeCollection(this.sounds, (buf2, sound) -> {
			sound.toBuf(buf2);
		});
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

	public void addConfig(final DynamicSoundConfigs config) {
		this.sounds.add(config);
	}

	public void clear() {
		this.sounds.clear();
	}

	public void modifyConfig(final DynamicSoundConfigs config) {
		final String key = config.getKey();
		for (final DynamicSoundConfigs base : this.sounds) {
			final String baseKey = base.getKey();
			final boolean val = key.equals(baseKey);
			if (val) {
				base.set(config);
				break;
			}
		}
	}

	public NbtElement toNbt() {
		final NbtList list = new NbtList();

		for (final DynamicSoundConfigs sound : this.sounds) {
			list.add(sound.toNbt());
		}

		return list;
	}

	public void load(final NbtList list) {
		int idx = 0;
		for (final DynamicSoundConfigs config : this.sounds) {
			if (list.get(idx) instanceof final NbtCompound nbt) {
				config.load(nbt);
			}

			idx++;
		}
	}
}
