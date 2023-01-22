package net.denanu.dynamicsoundmanager.gui;

import java.io.File;

import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetDirectoryEntry;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntry;
import net.denanu.dynamicsoundmanager.gui.widgets.WidgetAudioBrowser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class GuiBrowserBase extends GuiListBase<DirectoryEntry, WidgetDirectoryEntry, WidgetAudioBrowser>
{
	private WidgetAudioBrowser browser;

	public GuiBrowserBase(final int browserX, final int browserY)
	{
		super(browserX, browserY);
	}

	@Override
	protected WidgetAudioBrowser createListWidget(final int listX, final int listY)
	{
		this.browser = new WidgetAudioBrowser(listX, listY, 0, 0, this, this.getSelectionListener());
		return this.browser;
	}

	/**
	 * This is the string the DataManager uses for saving/loading/storing the last used directory
	 * for each browser GUI type/contet.
	 * @return
	 */
	public abstract String getBrowserContext();

	public abstract File getDefaultDirectory();

	@Override
	protected int getBrowserWidth()
	{
		return this.width + 150;
	}

	@Override
	protected int getBrowserHeight()
	{
		return this.height - 90;
	}

	public int getMaxInfoHeight()
	{
		return this.getBrowserHeight();
	}

	protected WidgetAudioBrowser getBrowser() {
		return this.browser;
	}
}
