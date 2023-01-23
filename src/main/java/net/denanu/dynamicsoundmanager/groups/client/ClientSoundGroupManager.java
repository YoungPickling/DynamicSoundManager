package net.denanu.dynamicsoundmanager.groups.client;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import fi.dy.masa.malilib.util.FileUtils;
import net.denanu.dynamicsoundmanager.groups.FileSynchronizationMetadataBuilder;
import net.denanu.dynamicsoundmanager.groups.SoundGroup;
import net.denanu.dynamicsoundmanager.gui.GuiFileManager;
import net.denanu.dynamicsoundmanager.mixin.SoundManagerMixin;
import net.denanu.dynamicsoundmanager.networking.c2s.InitTransferBidirectionalC2SPacket;
import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.denanu.dynamicsoundmanager.player_api.DynamicWeightedSoundSet;
import net.denanu.dynamicsoundmanager.utils.FileModificationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.level.storage.LevelSummary;

@Environment(value=EnvType.CLIENT)
public class ClientSoundGroupManager {
	public static HashSet<Identifier> soundIds = new HashSet<>();

	public static FileSynchronizationMetadataBuilder metadata;

	public static GuiFileManager fileManager = null;

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

	public static Path getDeleteFile(final LevelSummary lvl) {
		return ClientSoundGroupManager.getChache().resolve("localhost").resolve(lvl.getName());
	}

	public static void init(final List<SoundGroup> configs, final MinecraftClient client) {
		ClientSoundGroupManager.soundIds.clear();
		final Stream<Identifier> out = configs.stream().map(SoundGroup::getId);
		ClientSoundGroupManager.soundIds.addAll(out.toList());

		ClientSoundGroupManager.removeUnnededFiles(configs, ClientSoundGroupManager.getChach(client));
	}

	private static void removeUnnededFiles(final List<SoundGroup> configs, final Path root) {
		for (final SoundGroup group : configs) {
			ClientSoundGroupManager.removeUnnededFiles(group, root);
		}
	}

	private static void removeUnnededFiles(final SoundGroup group, final Path root) {
		final File[] files = InitTransferBidirectionalC2SPacket.toPath(root, group.getId()).toFile().listFiles();
		if (files != null) {
			for (final File file : InitTransferBidirectionalC2SPacket.toPath(root, group.getId()).toFile().listFiles()) {
				boolean found = false;
				for (final DynamicSoundConfigs sound : group.sounds) {
					final String key = sound.getKey();
					final String name = file.getName();
					if (key.equals(name)) {
						found = true;
						break;
					}
				}

				if (!found) {
					file.delete();
				}
			}
		}
	}

	public static Path getChache() {
		return FileUtils.getConfigDirectory().toPath().resolve("dynamic_sounds");
	}

	public static Path getChach(final MinecraftClient client) {
		Path cachePath = ClientSoundGroupManager.getChache();

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

	public static Map<Identifier, DynamicWeightedSoundSet> addPlaySets(final Collection<Identifier> ids) {
		final Map<Identifier, WeightedSoundSet> sounds = ClientSoundGroupManager.getSounds();
		final Map<Identifier, DynamicWeightedSoundSet> dynamicSounds = new HashMap<>();

		for (final Identifier id : ids) {
			final DynamicWeightedSoundSet dynamic = new DynamicWeightedSoundSet(id, "placeholder");
			sounds.put(id, dynamic);
			dynamicSounds.put(id, dynamic);
		}

		return dynamicSounds;
	}

	public static void populateSounds(final List<SoundGroup> configs, final Map<Identifier, DynamicWeightedSoundSet> groups) {
		for (final SoundGroup sound_data : configs) {
			groups.get(sound_data.getId()).addSounds(sound_data.sounds);
		}
	}

	public static void modifyConfig(final DynamicSoundConfigs config) {
		final WeightedSoundSet set = ClientSoundGroupManager.getSounds().get(config.getId());
		if (set instanceof final DynamicWeightedSoundSet dynamicSet) {
			dynamicSet.modifySound(config);
		}
	}

	public static void update() {
		if (ClientSoundGroupManager.fileManager != null) {
			ClientSoundGroupManager.fileManager.update();
		}
	}
}