package net.denanu.dynamicsoundmanager.groups;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.gui.interfaces.IDirectoryCache;
import net.denanu.dynamicsoundmanager.utils.FileModificationUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

public class ServerSoundGroups implements IDirectoryCache {
	public static HashMap<Identifier, SoundGroup> sounds = new HashMap<>();

	public static Path path;

	public static SoundGroup register(final Identifier id, final SoundGroup group) {
		ServerSoundGroups.sounds.put(id, group);
		return group;
	}

	public static void register(final Identifier id) {
		ServerSoundGroups.register(id, new SoundGroup(id));
	}

	public static void setup(final MinecraftServer server) {
		ServerSoundGroups.path = server.getSavePath(WorldSavePath.ROOT).getParent().resolve("dynamic_sounds");

		server.getOverworld().playSound(0, 0, 0, null, null, 0, 0, false);

		final File partentFile = ServerSoundGroups.path.toFile();

		FileModificationUtils.mkdirIfAbsent(partentFile);

		for (final Identifier group : ServerSoundGroups.sounds.keySet()) {
			final File groupFile = ServerSoundGroups.path.resolve(group.toString()).toFile();
			if ((!groupFile.exists() || !groupFile.isDirectory()) && !groupFile.mkdir()) {
				throw new RuntimeException("unable to create dynamic sounds folder for the group " + group.toString());
			}
		}
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
}
