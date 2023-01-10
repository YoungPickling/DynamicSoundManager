package net.denanu.dynamicsoundmanager.gui;

import fi.dy.masa.malilib.config.IConfigHandler;

public class Config implements IConfigHandler {
	/*private static final String CONFIG_FILE_NAME = DynamicSoundManager.MOD_ID + ".json";


	public static void loadFromFile()
	{
		final File configFile = new File(FileUtils.getConfigDirectory(), Config.CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead())
		{
			final JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject())
			{
				element.getAsJsonObject();
			}
		}
	}

	public static void saveToFile()
	{
		final File dir = FileUtils.getConfigDirectory();

		if (dir.exists() && dir.isDirectory() || dir.mkdirs())
		{
			final JsonObject root = new JsonObject();
			JsonUtils.writeJsonToFile(root, new File(dir, Config.CONFIG_FILE_NAME));
		}
	}*/

	@Override
	public void load()
	{
		//Config.loadFromFile();
	}

	@Override
	public void save()
	{
		//Config.saveToFile();
	}
}
