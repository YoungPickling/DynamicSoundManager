package net.denanu.dynamicsoundmanager.gui.widgets;

import java.io.File;
import java.util.Locale;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntryType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DirectoryEntry implements Comparable<DirectoryEntry>
{
	private final DirectoryEntryType type;
	private final File dir;
	private final String name;
	@Nullable private final String displaynamePrefix;

	public DirectoryEntry(final DirectoryEntryType type, final File dir, final String name, @Nullable final String displaynamePrefix)
	{
		this.type = type;
		this.dir = dir;
		this.name = name;
		this.displaynamePrefix = displaynamePrefix;
	}

	public DirectoryEntryType getType()
	{
		return this.type;
	}

	public File getDirectory()
	{
		return this.dir;
	}

	public String getName()
	{
		return this.name;
	}

	@Nullable
	public String getDisplayNamePrefix()
	{
		return this.displaynamePrefix;
	}

	public String getDisplayName()
	{
		return this.displaynamePrefix != null ? this.displaynamePrefix + this.name : this.name;
	}

	public File getFullPath()
	{
		return new File(this.dir, this.name);
	}

	@Override
	public int compareTo(final DirectoryEntry other)
	{
		return this.name.toLowerCase(Locale.US).compareTo(other.getName().toLowerCase(Locale.US));
	}
}