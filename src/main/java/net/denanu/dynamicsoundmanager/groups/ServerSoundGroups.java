package net.denanu.dynamicsoundmanager.groups;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.gui.interfaces.IDirectoryCache;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.denanu.dynamicsoundmanager.utils.FileModificationUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.Vec3d;

public class ServerSoundGroups implements IDirectoryCache {
	public static HashMap<Identifier, SoundGroup> sounds = new HashMap<>();

	public static Path path;
	public static FileSynchronizationMetadataBuilder metadata;

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

	@Override
	@Nullable
	public File getCurrentDirectoryForContext(final String context) {
		return ClientSoundGroupManager.getChach(MinecraftClient.getInstance()).toFile();
	}

	@Override
	public void setCurrentDirectoryForContext(final String context, final File dir) {
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
}
