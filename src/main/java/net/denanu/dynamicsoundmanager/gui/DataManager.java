package net.denanu.dynamicsoundmanager.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.swing.filechooser.FileSystemView;

import fi.dy.masa.malilib.gui.interfaces.IDirectoryCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DataManager implements IDirectoryCache {
	private static final DataManager INSTANCE = new DataManager();

	private static final Map<String, File> LAST_DIRECTORIES = new HashMap<>();

	private static File currentWorkingDirectory = DataManager.getDefaultWorkingDirectory();

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

	public static File getBaseDirectory() {
		return DataManager.currentWorkingDirectory;
	}

	public static File getDefaultWorkingDirectory() {
		return FileSystemView.getFileSystemView().getHomeDirectory();
	}

	public static void setCurrentWorkingDirectory(final File currentWorkingDirectory) {
		DataManager.currentWorkingDirectory = currentWorkingDirectory;
	}
}
