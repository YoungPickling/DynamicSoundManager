package net.denanu.dynamicsoundmanager.gui.widgets;

import java.io.File;
import java.io.FileFilter;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase;
import fi.dy.masa.malilib.util.StringUtils;
import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.gui.DataManager;
import net.denanu.dynamicsoundmanager.gui.GuiBrowserBase;
import net.denanu.dynamicsoundmanager.gui.Icons;

public class WidgetAudioBrowser extends WidgetFileBrowserBase {
	protected final GuiBrowserBase parent;
	protected final int infoWidth;
	protected final int infoHeight;

	public WidgetAudioBrowser(final int x, final int y, final int width, final int height, final GuiBrowserBase parent, @Nullable final ISelectionListener<DirectoryEntry> selectionListener)
	{
		super(x, y, width, height, DataManager.getDirectoryCache(), parent.getBrowserContext(),
				parent.getDefaultDirectory(), selectionListener, Icons.FILE_ICON_OGG);

		this.title = StringUtils.translate(DynamicSoundManager.MOD_ID + ".gui.title.browser");
		this.infoWidth = 170;
		this.infoHeight = 290;
		this.parent = parent;
	}

	@Override
	protected int getBrowserWidthForTotalWidth(final int width)
	{
		return super.getBrowserWidthForTotalWidth(width) - this.infoWidth;
	}

	@Override
	public void close()
	{
		super.close();
	}

	@Override
	protected File getRootDirectory()
	{
		return DataManager.getSchematicsBaseDirectory();
	}

	@Override
	protected FileFilter getFileFilter() {
		return null;
	}
}
