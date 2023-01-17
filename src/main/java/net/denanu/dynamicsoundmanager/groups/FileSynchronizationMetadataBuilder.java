package net.denanu.dynamicsoundmanager.groups;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.denanu.dynamicsoundmanager.utils.FileModificationUtils;
import net.minecraft.util.Identifier;
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
		JSONObject jo = null;

		try {
			jo = this.read();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}

		if (jo == null) {
			this.mkfile();
		}
		else {
			this.versions.clear();
			for (final Object key : jo.keySet()) {
				final String key_str = (String)key;
				final long version = (long)jo.get(key);

				this.versions.put(key_str, version);
			}
		}
	}

	public void mkfile() {
		FileModificationUtils.mkdirIfAbsent(this.file.getParentFile());
		this.update();
	}

	@SuppressWarnings("unchecked")
	public void update() {
		final JSONObject jo = new JSONObject();
		jo.putAll(this.versions);
		this.write(jo);
	}

	private void write(final JSONObject jo) {
		try (PrintWriter out = new PrintWriter(new FileWriter(this.file))) {
			out.write(jo.toJSONString());
			out.close();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject read() {
		final JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader(this.file))
		{
			//Read JSON file
			final Object obj = jsonParser.parse(reader);

			return (JSONObject) obj;

		} catch (IOException | org.json.simple.parser.ParseException e) {
			//e.printStackTrace();
		}

		return null;
	}

	public void changeVersion(final Identifier id, final String file) {
		this.changeVersion(id, file, Instant.now().toEpochMilli());
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
