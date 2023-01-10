package net.denanu.dynamicsoundmanager.gui;

import java.util.List;

import com.google.common.collect.ImmutableList;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import net.denanu.dynamicsoundmanager.DynamicSoundManager;

public class GuiConfigs extends GuiConfigsBase {
	public GuiConfigs()
	{
		super(10, 50, DynamicSoundManager.MOD_ID, null, DynamicSoundManager.MOD_ID + ".gui.title.configs");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.clearOptions();

		this.initBottomBar();
	}

	private void initBottomBar() {
		final int x = this.width  - 7;
		final int y = this.height - 7;

		this.createButton(x, y, -1, GuiConfigs.getOpenBrowserListener(), "Import");
	}

	private int createButton(final int x, final int y, final int width, final IButtonActionListener listener, final String name) {
		final ButtonGeneric button = new ButtonGeneric(x, y, width, 20, name);
		button.setPosition(x - button.getWidth(), y - button.getHeight());
		this.addButton(button, listener);

		return button.getWidth() + 2;
	}

	public static IButtonActionListener getOpenBrowserListener() {
		return (button, mouseButton) -> {
			GuiBase.openGui(new GuiFileManager());
		};
	}

	@Override
	public List<ConfigOptionWrapper> getConfigs() {
		return ImmutableList.of();
	}
}
