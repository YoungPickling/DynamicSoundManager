package net.denanu.dynamicsoundmanager.utils;

import net.minecraft.util.Identifier;

public class FileKey {
	Identifier id;
	String filename;

	public FileKey(final String file) {
		final String[] keys = file.split(":");

		this.id = Identifier.of(keys[0], keys[1]);
		this.filename = keys[2];
	}

	public Identifier getId() {
		return this.id;
	}

	public String getFileName() {
		return this.filename;
	}

	public String getDirName() {
		return this.id.toString().replace(":", "/");
	}
}
