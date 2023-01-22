package net.denanu.dynamicsoundmanager.groups;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.utils.FileModificationUtils;
import net.minecraft.data.DataProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;

public class FileSynchronizationMetadataBuilder {
	private final Map<String, Long> versions;
	private final File file;

	public FileSynchronizationMetadataBuilder(final File file) {
		this.versions = new HashMap<>();
		this.file = file;
		this.reload();
	}

	public void reload() {
		JsonObject json = null;

		try {
			json = this.read();
		}
		catch (final Exception e) {
			DynamicSoundManager.LOGGER.warn("Unable to read sound metadata from json file: " + this.file.getAbsolutePath());
		}

		if (json == null) {
			this.mkfile();
		}
		else {
			this.versions.clear();
			for (final String key : json.keySet()) {
				final long version = JsonHelper.getLong(json, key);
				this.versions.put(key, version);
			}
		}
	}

	public void mkfile() {
		FileModificationUtils.mkdirIfAbsent(this.file.getParentFile());
		this.update();
	}

	public void update() {
		final JsonObject json = new JsonObject();
		for (final Entry<String, Long> key : this.versions.entrySet()) {
			json.addProperty(key.getKey(), key.getValue());
		}
		try {
			this.write(json);
		} catch (final IOException e) {
			DynamicSoundManager.LOGGER.warn("Unable to write sound metadata to json file");
		}
	}

	private void write(final JsonObject json) throws IOException {
		final FileOutputStream stream = new FileOutputStream(this.file);
		final OutputStreamWriter writer2 = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
		final JsonWriter jsonWriter = new JsonWriter(writer2);
		jsonWriter.setSerializeNulls(false);
		jsonWriter.setIndent("  ");
		JsonHelper.writeSorted(jsonWriter, json, DataProvider.JSON_KEY_SORTING_COMPARATOR);
		jsonWriter.close();
		writer2.close();
		stream.close();
	}

	public JsonObject read() throws FileNotFoundException {
		return JsonHelper.deserialize(new FileReader(this.file));
	}

	public void changeVersion(final Identifier id, final String file) {
		this.changeVersion(id, file, Instant.now().toEpochMilli());
	}

	public void delete(final Identifier id, final String file) {
		final String key = FileSynchronizationMetadataBuilder.buildKey(id, file);
		this.versions.remove(key);
		this.update();
	}

	public void changeVersion(final Identifier id, final String file, final long version) {
		final String key = FileSynchronizationMetadataBuilder.buildKey(id, file);
		this.versions.put(key, version);
		this.update();
	}

	public long get(final Identifier id, final String file) {
		return this.versions.get(FileSynchronizationMetadataBuilder.buildKey(id, file));
	}

	private static String buildKey(final Identifier id, final String file) {
		return new StringBuilder()
				.append(id.toString())
				.append(":")
				.append(file)
				.toString();
	}

	public Map<String, Long> getVersions() {
		return this.versions;
	}

	public List<String> getNonMatchingVersions(final ArrayList<Pair<String, Long>> shouldBe) {
		final List<String> nonMatching = new LinkedList<>();
		for (final Pair<String, Long> entry : shouldBe) {
			if (!this.versions.containsKey(entry.getLeft()) || entry.getRight() != (long)this.versions.get(entry.getLeft())) {
				entry.getRight();
				this.versions.get(entry.getLeft());
				nonMatching.add(entry.getLeft());
			}
		}
		return nonMatching;
	}
}
