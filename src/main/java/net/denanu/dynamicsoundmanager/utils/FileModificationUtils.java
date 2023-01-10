package net.denanu.dynamicsoundmanager.utils;

import java.io.File;

public class FileModificationUtils {
	public static void mkdirIfAbsent(final File file) {
		if ((!file.exists() || !file.isDirectory()) && !file.mkdirs()) {
			throw new RuntimeException("unable to create directory " + file.toString());
		}
	}
}
