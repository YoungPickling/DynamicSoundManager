package net.denanu.dynamicsoundmanager.gui;

import java.io.File;

import javax.annotation.Nullable;
import javax.swing.filechooser.FileSystemView;

import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.gui.widgets.AudioPlayerWidget;
import net.denanu.dynamicsoundmanager.utils.FileType;

public class GuiFileManager extends GuiBrowserBase implements ISelectionListener<DirectoryEntry>
{
	private AudioPlayerWidget audioPlayer;

	public GuiFileManager()
	{
		super(10, 50);

		this.title = StringUtils.translate(DynamicSoundManager.MOD_ID + ".gui.title.schematic_manager");
	}

	@Override
	public String getBrowserContext()
	{
		return "schematic_manager";
	}

	@Override
	public File getDefaultDirectory()
	{
		return DataManager.getBaseDirectory();
	}

	@Override
	protected int getBrowserHeight()
	{
		return this.height - 80;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.createButtons();
	}

	private void createFileSystemSelectionButtons() {
		int x = 20;
		final int y = 20;

		x += this.createFileSystemButton(x, y, FileSystemView.getFileSystemView().getHomeDirectory(), "home");

		for (final File system : FileSystemView.getFileSystemView().getRoots()) {
			x += this.createFileSystemButton(x, y, system, system.toString()) + 2;
		}

		this.addWidget(this.audioPlayer = new AudioPlayerWidget(10, this.height - 20, this.getBrowserWidth(), 20));

	}

	private int createFileSystemButton(final int x, final int y, final File file, final String name) {
		final ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, name);
		this.addButton(button, (a, b) -> {
			DataManager.setCurrentWorkingDirectory(file);
			this.getBrowser().switchToRootDirectory();
		});
		return button.getWidth() + 2;
	}

	private void createButtons()
	{
		this.createFileSystemSelectionButtons();
		final DirectoryEntry selected = this.getListWidget().getLastSelectedEntry();


		if (selected != null)
		{
			FileType.fromFile(selected.getFullPath());
		}
	}

	@Override
	public void onSelectionChange(@Nullable final DirectoryEntry entry)
	{
		this.clearButtons();
		this.createButtons();
	}

	@Override
	protected ISelectionListener<DirectoryEntry> getSelectionListener()
	{
		return entry -> {
			if (entry.getFullPath().isFile()) {
				this.audioPlayer.load(entry.getFullPath());
				this.audioPlayer.enable();
			}
		};
	}

	@Override
	public void closeGui(final boolean showParent) {
		super.closeGui(showParent);
		this.audioPlayer.remove();
	}
}
