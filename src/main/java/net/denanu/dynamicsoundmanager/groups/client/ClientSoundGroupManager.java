package net.denanu.dynamicsoundmanager.groups.client;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.dy.masa.malilib.util.FileUtils;
import net.denanu.dynamicsoundmanager.groups.FileSynchronizationMetadataBuilder;
import net.denanu.dynamicsoundmanager.mixin.SoundManagerMixin;
import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.denanu.dynamicsoundmanager.player_api.DynamicWeightedSoundSet;
import net.denanu.dynamicsoundmanager.utils.FileKey;
import net.denanu.dynamicsoundmanager.utils.FileModificationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.WorldSavePath;

@Environment(value=EnvType.CLIENT)
public class ClientSoundGroupManager {
	public static ArrayList<Identifier> soundIds = new ArrayList<>();

	public static FileSynchronizationMetadataBuilder metadata;

	public static String getServerName(final MinecraftClient client) {
		final ServerInfo data = client.getCurrentServerEntry();
		if (client.isInSingleplayer()) {
			return "localhost/" + client
					.getServer()
			.getSavePath(WorldSavePath.ROOT)
			.getParent()
			.getFileName()
			.toString();
		}
		return new StringBuilder()
				.append("remote")
				.append("/")
				.append(data.name)
				.append("/")
				.append(data.address)
				.toString();
	}

	public static Path getChach(final MinecraftClient client) {
		Path cachePath =  FileUtils.getConfigDirectory().toPath().resolve("dynamic_sounds");

		cachePath = cachePath.resolve(ClientSoundGroupManager.getServerName(client));
		final File cacheFile = cachePath.toFile();

		FileModificationUtils.mkdirIfAbsent(cacheFile);

		return cachePath;
	}

	public static void setup() {
		final File path = ClientSoundGroupManager.getChach(MinecraftClient.getInstance()).resolve("metadata.json").toFile();
		ClientSoundGroupManager.metadata = new FileSynchronizationMetadataBuilder(path);
		ClientSoundGroupManager.removeClientSoundData();
	}

	public static Map<Identifier, WeightedSoundSet> getSounds() {
		return ((SoundManagerMixin)MinecraftClient.getInstance().getSoundManager()).getSounds();
	}

	private static void removeClientSoundData() {
		final Map<Identifier, WeightedSoundSet> sounds = ClientSoundGroupManager.getSounds();
		for (final Identifier key : ClientSoundGroupManager.soundIds) {
			sounds.remove(key);
		}
	}

	public static Map<Identifier, DynamicWeightedSoundSet> addClientSoundData() {
		return ClientSoundGroupManager.addPlaySets(ClientSoundGroupManager.soundIds);
	}

	public static Map<Identifier, DynamicWeightedSoundSet> addPlaySets(final List<Identifier> ids) {
		final Map<Identifier, WeightedSoundSet> sounds = ClientSoundGroupManager.getSounds();
		final Map<Identifier, DynamicWeightedSoundSet> dynamicSounds = new HashMap<>();

		for (final Identifier id : ids) {
			final DynamicWeightedSoundSet dynamic = new DynamicWeightedSoundSet(id, "placeholder");
			sounds.put(id, dynamic);
			dynamicSounds.put(id, dynamic);
		}

		return dynamicSounds;
	}

	public static void populateSounds(final ArrayList<Pair<String, Long>> data, final Map<Identifier, DynamicWeightedSoundSet> groups) {
		for (final Pair<String, Long> sound_data : data) {
			final FileKey key = new FileKey(sound_data.getLeft());
			groups.get(key.getId()).addSound(DynamicSoundConfigs.of(key));
		}

	}
}










































