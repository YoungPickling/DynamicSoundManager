package net.denanu.dynamicsoundmanager.groups;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.spongepowered.include.com.google.gson.Gson;
import org.spongepowered.include.com.google.gson.internal.LinkedTreeMap;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.denanu.dynamicsoundmanager.utils.FileModificationUtils;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class FileSynchronizationMetadataBuilder {
	private Map<String, Double> versions;
	private final File file;

	public FileSynchronizationMetadataBuilder(final File file) {
		this.versions = new HashMap<>();
		this.file = file;
		this.reload();
	}

	public void reload() {
		@SuppressWarnings("unchecked")
		final LinkedTreeMap<String, Double> jo = (LinkedTreeMap<String, Double>) this.read();
		if (jo == null) {
			this.mkfile();
		}
		else {
			this.versions = jo;
		}
	}

	public void mkfile() {
		FileModificationUtils.mkdirIfAbsent(this.file.getParentFile());
		this.update();
	}

	public void update() {
		this.write();
	}

	private void write() {
		final Gson gson = new Gson();
		try {
			//this.file.delete();
			final FileWriter out = new FileWriter(this.file);
			gson.toJson(this.versions, out);
			out.close();
		} catch (JsonIOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LinkedTreeMap<?, ?> read() {
		final Gson gson = new Gson();
		try {
			return gson.fromJson(new FileReader(this.file), LinkedTreeMap.class);
		} catch (JsonSyntaxException | JsonIOException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void changeVersion(final Identifier id, final File file) {
		final String key = new StringBuilder()
				.append(id.toString())
				.append(":")
				.append(file.getName())
				.toString();

		final long version = Instant.now().toEpochMilli();

		this.versions.put(key, (double)version);
		this.update();
	}

	public Map<String, Double> getVersions() {
		return this.versions;
	}

	public Map<String, Double> getNonMatchingVersions(final ArrayList<Pair<String, Double>> shouldBe) {
		final Map<String, Double> nonMatching = new TreeMap<>();
		for (final Pair<String, Double> entry : shouldBe) {
			if (!this.versions.containsKey(entry.getLeft()) || entry.getRight() != this.versions.get(entry.getLeft())) {
				nonMatching.put(entry.getLeft(), entry.getRight());
			}
		}
		return nonMatching;
	}
}
