package net.denanu.dynamicsoundmanager.gui;

import java.io.File;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetDirectoryEntry;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntry;
import net.denanu.dynamicsoundmanager.gui.widgets.WidgetAudioBrowser;

public abstract class GuiBrowserBase extends GuiListBase<DirectoryEntry, WidgetDirectoryEntry, WidgetAudioBrowser>
{
	public GuiBrowserBase(final int browserX, final int browserY)
	{
		super(browserX, browserY);
	}

	@Override
	protected WidgetAudioBrowser createListWidget(final int listX, final int listY)
	{
		// The width and height will be set to the actual values in initGui()
		return new WidgetAudioBrowser(listX, listY, 100, 100, this, this.getSelectionListener());
	}

	/**
	 * This is the string the DataManager uses for saving/loading/storing the last used directory
	 * for each browser GUI type/contet.
	 * @return
	 */
	public abstract String getBrowserContext();

	public abstract File getDefaultDirectory();

	@Override
	@Nullable
	protected ISelectionListener<DirectoryEntry> getSelectionListener()
	{
		return null;
	}

	@Override
	protected int getBrowserWidth()
	{
		return this.width - 20;
	}

	@Override
	protected int getBrowserHeight()
	{
		return this.height - 70;
	}

	public int getMaxInfoHeight()
	{
		return this.getBrowserHeight();
	}
}
