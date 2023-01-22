package net.denanu.dynamicsoundmanager.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.denanu.dynamicsoundmanager.gui.GuiDynamicConfigurer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuImpl implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		return screen -> {
			final GuiDynamicConfigurer gui = new GuiDynamicConfigurer();
			gui.setParent(screen);
			return gui;
		};
	}
}
