package net.denanu.dynamicsoundmanager.gui;

import java.io.File;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.denanu.dynamicsoundmanager.utils.FileType;

public class GuiFileManager extends GuiBrowserBase implements ISelectionListener<DirectoryEntry>
{
	public GuiFileManager()
	{
		super(10, 50);

		this.title = StringUtils.translate("litematica.gui.title.schematic_manager");
	}

	@Override
	public String getBrowserContext()
	{
		return "schematic_manager";
	}

	@Override
	public File getDefaultDirectory()
	{
		return DataManager.getSchematicsBaseDirectory();
	}

	@Override
	protected int getBrowserHeight()
	{
		return this.height - 60;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.createButtons();
	}

	private void createButtons()
	{
		final DirectoryEntry selected = this.getListWidget().getLastSelectedEntry();

		if (selected != null)
		{
			FileType.fromFile(selected.getFullPath());
		}

		/*final ButtonListenerChangeMenu.ButtonType type = ButtonListenerChangeMenu.ButtonType.MAIN_MENU;
		final String label = StringUtils.translate(type.getLabelKey());
		final int buttonWidth = this.getStringWidth(label) + 20;
		this.addButton(new ButtonGeneric(this.width - buttonWidth - 10, y, buttonWidth, 20, label), new ButtonListenerChangeMenu(type, null));*/
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
		return this;
	}
}
