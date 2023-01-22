package net.denanu.dynamicsoundmanager.groups;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import net.denanu.dynamicsoundmanager.networking.s2c.UpdateePlayConfigsS2CPacket;
import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.denanu.dynamicsoundmanager.utils.FileModificationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;

public class ServerSoundGroups extends PersistentState {
	public static HashMap<Identifier, SoundGroup> sounds = new HashMap<>();

	public static Path path;
	public static FileSynchronizationMetadataBuilder metadata;
	private static boolean dirty = false;

	public static FileFilter AUDIO_FILE_FILTER = dir -> {
		if (dir.isFile() && dir.getName().endsWith(".ogg")) {
			return true;
		}
		return false;
	};

	public static SoundGroup register(final Identifier id, final SoundGroup group) {
		ServerSoundGroups.sounds.put(id, group);
		return group;
	}

	public static void register(final Identifier id) {
		ServerSoundGroups.register(id, new SoundGroup(id));
	}

	public static void setup(final MinecraftServer server) {
		ServerSoundGroups.path = server.getSavePath(WorldSavePath.ROOT).getParent().resolve("dynamic_sounds");
		final File partentFile = ServerSoundGroups.path.toFile();
		FileModificationUtils.mkdirIfAbsent(partentFile);


		for (final Entry<Identifier, SoundGroup> group : ServerSoundGroups.sounds.entrySet()) {
			final File groupFile = group.getValue().getPath().toFile();
			if ((!groupFile.exists() || !groupFile.isDirectory()) && !groupFile.mkdirs()) {
				throw new RuntimeException("unable to create dynamic sounds folder for the group " + group.toString());
			}

			group.getValue().clear();

			for (final File audio : groupFile.listFiles(ServerSoundGroups.AUDIO_FILE_FILTER)) {
				group.getValue().addConfig(
						new DynamicSoundConfigs(group.getKey(), audio.getName())
						);
			}
		}

		ServerSoundGroups.metadata = new FileSynchronizationMetadataBuilder(ServerSoundGroups.path.resolve("metadata.json").toFile());
	}

	public static void addConfig(final Identifier id, final String key) {
		ServerSoundGroups.sounds.get(id).addConfig(new DynamicSoundConfigs(id, key));
	}

	public static Path getPath() {
		return ServerSoundGroups.path;
	}

	public static HashMap<Identifier, SoundGroup> getSounds() {
		return ServerSoundGroups.sounds;
	}

	public static DynamicSoundConfigs playSound(final ServerWorld world, @Nullable final PlayerEntity player, final Vec3d pos, final SoundEvent sound, final SoundCategory category, final float volume, final float pitch) {
		return ServerSoundGroups.playSound(world, player, pos.x, pos.y, pos.z, sound, category, volume, pitch);
	}

	public static DynamicSoundConfigs playSound(final ServerWorld world, @Nullable final PlayerEntity player, final double x, final double y, final double z, final SoundEvent sound, final SoundCategory category, final float volume, final float pitch) {
		final long seed = world.getRandom().nextLong();
		world.playSound(player, x, y, z, sound, category, volume, pitch, seed);
		return ServerSoundGroups.getConfig(seed, sound.getId());
	}

	public static DynamicSoundConfigs playSoundFromEntity(final ServerWorld world, @Nullable final PlayerEntity player, final Entity entity, final SoundEvent sound, final SoundCategory category, final float volume, final float pitch) {
		final long seed = world.getRandom().nextLong();
		world.playSoundFromEntity(player, entity, sound, category, volume, pitch, seed);
		return ServerSoundGroups.getConfig(seed, sound.getId());
	}

	private static DynamicSoundConfigs getConfig(final long seed, final Identifier id) {
		return ServerSoundGroups.sounds.get(id).getConfig(seed);
	}

	public static void modifyConfig(final MinecraftServer server, final DynamicSoundConfigs config) {
		ServerSoundGroups.sounds.get(config.getId()).modifyConfig(config);
		ServerSoundGroups.dirty = true;
		UpdateePlayConfigsS2CPacket.send(server, config);
	}

	@Override
	public NbtCompound writeNbt(final NbtCompound nbt) {
		for (final Entry<Identifier, SoundGroup> sound : ServerSoundGroups.sounds.entrySet()) {
			nbt.put(sound.getKey().toString(), sound.getValue().toNbt());
		}
		ServerSoundGroups.dirty = false;
		return nbt;
	}

	public static ServerSoundGroups fromNbt(final NbtCompound nbt) {
		for (final Entry<Identifier, SoundGroup> key : ServerSoundGroups.sounds.entrySet()) {
			if (nbt.contains(key.getKey().toString())) {
				key.getValue().load(nbt.getList(key.getKey().toString(), NbtElement.COMPOUND_TYPE));
			}
		}

		return new ServerSoundGroups();
	}

	public static ServerSoundGroups loadDefault() {
		final ServerSoundGroups out = new ServerSoundGroups();
		ServerSoundGroups.dirty = true;
		return out;
	}

	public static void setDirty() {
		ServerSoundGroups.dirty = true;
	}

	@Override
	public boolean isDirty() {
		return ServerSoundGroups.dirty;
	}
}
