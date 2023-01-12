package net.denanu.dynamicsoundmanager.utils;

import java.io.File;

public enum FileType
{
	INVALID,
	UNKNOWN,
	OGG;

	public static FileType fromFile(final File file)
	{
		if (!file.isFile() || !file.canRead()) {
			return INVALID;
		}
		final String name = file.getName();

		if (name.endsWith(".ogg")) {
			return OGG;
		}

		return UNKNOWN;
	}

	public static boolean isValid(final File file) {
		final FileType type = FileType.fromFile(file);
		return switch (type) {
		case INVALID, UNKNOWN -> false;
		default -> true;
		};
	}
}
