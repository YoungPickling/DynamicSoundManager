package net.denanu.dynamicsoundmanager.gui.widgets;

import java.io.File;

import fi.dy.masa.malilib.gui.interfaces.IDirectoryNavigator;
import fi.dy.masa.malilib.gui.interfaces.IFileBrowserIconProvider;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntry;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntryType;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.FileUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class WidgetDirectoryEntry extends WidgetListEntryBase<DirectoryEntry>
{
	protected final IDirectoryNavigator navigator;
	protected final DirectoryEntry entry;
	protected final IFileBrowserIconProvider iconProvider;
	protected final boolean isOdd;

	public WidgetDirectoryEntry(final int x, final int y, final int width, final int height, final boolean isOdd, final DirectoryEntry entry,
			final int listIndex, final IDirectoryNavigator navigator, final IFileBrowserIconProvider iconProvider)
	{
		super(x, y, width, height, entry, listIndex);

		this.isOdd = isOdd;
		this.entry = entry;
		this.navigator = navigator;
		this.iconProvider = iconProvider;
	}

	public DirectoryEntry getDirectoryEntry()
	{
		return this.entry;
	}

	@Override
	protected boolean onMouseClickedImpl(final int mouseX, final int mouseY, final int mouseButton)
	{
		if (this.entry.getType() != DirectoryEntryType.DIRECTORY) {
			return super.onMouseClickedImpl(mouseX, mouseY, mouseButton);
		}
		this.navigator.switchToDirectory(new File(this.entry.getDirectory(), this.entry.getName()));

		return true;
	}

	@Override
	public void render(final int mouseX, final int mouseY, final boolean selected, final MatrixStack matrixStack)
	{
		// Draw a lighter background for the hovered and the selected entry
		if (selected || this.isMouseOver(mouseX, mouseY))
		{
			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x70FFFFFF);
		}
		else if (this.isOdd)
		{
			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x20FFFFFF);
		}
		// Draw a slightly lighter background for even entries
		else
		{
			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x38FFFFFF);
		}

		final IGuiIcon icon = switch (this.entry.getType()) {
		case DIRECTORY -> this.iconProvider.getIconDirectory();
		default -> this.iconProvider.getIconForFile(this.entry.getFullPath());
		};

		final int iconWidth = icon != null ? icon.getWidth() : 0;
		final int xOffset = iconWidth + 2;

		if (icon != null)
		{
			RenderUtils.color(1f, 1f, 1f, 1f);
			this.bindTexture(icon.getTexture());
			icon.renderAt(this.x, this.y + (this.height - icon.getHeight()) / 2, this.zLevel + 10, false, false);
		}

		// Draw an outline if this is the currently selected entry
		if (selected)
		{
			RenderUtils.drawOutline(this.x, this.y, this.width, this.height, 0xEEEEEEEE);
		}

		final int yOffset = (this.height - this.fontHeight) / 2 + 1;
		this.drawString(this.x + xOffset + 2, this.y + yOffset, 0xFFFFFFFF, this.getDisplayName(), matrixStack);

		super.render(mouseX, mouseY, selected, matrixStack);
	}

	protected String getDisplayName()
	{
		if (this.entry.getType() == DirectoryEntryType.DIRECTORY)
		{
			return this.entry.getDisplayName();
		}
		return FileUtils.getNameWithoutExtension(this.entry.getDisplayName());
	}
}
