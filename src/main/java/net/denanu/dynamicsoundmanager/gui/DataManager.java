package net.denanu.dynamicsoundmanager.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.gui.interfaces.IDirectoryCache;
import fi.dy.masa.malilib.util.FileUtils;

public class DataManager implements IDirectoryCache {
	private static final DataManager INSTANCE = new DataManager();

	private static final Map<String, File> LAST_DIRECTORIES = new HashMap<>();

	public static DataManager getInstance() {
		return DataManager.INSTANCE;
	}

	public static IDirectoryCache getDirectoryCache()
	{
		return DataManager.INSTANCE;
	}

	@Override
	@Nullable
	public File getCurrentDirectoryForContext(final String context)
	{
		return DataManager.LAST_DIRECTORIES.get(context);
	}

	@Override
	public void setCurrentDirectoryForContext(final String context, final File dir)
	{
		DataManager.LAST_DIRECTORIES.put(context, dir);
	}

	public static File getSchematicsBaseDirectory() {
		return FileUtils.getMinecraftDirectory();
	}
}
