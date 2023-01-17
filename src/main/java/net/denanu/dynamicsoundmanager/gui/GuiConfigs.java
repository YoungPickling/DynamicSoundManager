package net.denanu.dynamicsoundmanager.gui;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class GuiConfigs extends GuiConfigsBase {
	private static Identifier id = null;

	private final List<Pair<ButtonGeneric, Identifier>> groupButtons;

	public GuiConfigs()
	{
		super(10, 50, DynamicSoundManager.MOD_ID, null, DynamicSoundManager.MOD_ID + ".gui.title.configs");
		this.groupButtons = new LinkedList<>();
	}

	@Override
	public void initGui()
	{
		this.groupButtons.clear();
		super.initGui();
		this.clearOptions();

		this.initBottomBar();

		int x = 10;
		final int y = 26;

		for (final Identifier group : ClientSoundGroupManager.soundIds) {
			x = this.addGroupSelectionButton(x, y, group);
		}

		this.updateGroupButtonHighlighting();
	}

	private void updateGroupButtonHighlighting() {
		for (final Pair<ButtonGeneric, Identifier> button : this.groupButtons) {
			button.getLeft().setEnabled(GuiConfigs.id != button.getRight());
		}
	}

	private int addGroupSelectionButton(final int x, final int y, final Identifier id) {
		final ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, StringUtils.translate(id.toTranslationKey()));
		this.groupButtons.add(new Pair<>(button, id));
		this.addButton(button, (b, m) -> {
			GuiConfigs.id = id;
			this.updateGroupButtonHighlighting();
		});

		return x + 2 + button.getWidth();
	}

	private void initBottomBar() {
		final int x = this.width  - 2;
		final int y = this.height - 2;

		this.createButton(x, y, -1, this.getOpenBrowserListener(), StringUtils.translate(DynamicSoundManager.MOD_ID + ".button.import"));
	}

	private int createButton(final int x, final int y, final int width, final IButtonActionListener listener, final String name) {
		final ButtonGeneric button = new ButtonGeneric(x, y, width, 20, name);
		button.setPosition(x - button.getWidth(), y - button.getHeight());
		this.addButton(button, listener);

		return button.getWidth() + 2;
	}

	public IButtonActionListener getOpenBrowserListener() {
		return (button, mouseButton) -> {
			final GuiFileManager manager = new GuiFileManager();
			GuiBase.openGui(manager);
			manager.setParent(this);
		};
	}

	@Override
	public List<ConfigOptionWrapper> getConfigs() {
		return ImmutableList.of();
	}

	public static Identifier getId() {
		return GuiConfigs.id;
	}
}
