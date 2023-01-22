package net.denanu.dynamicsoundmanager.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetLabel;
import fi.dy.masa.malilib.util.StringUtils;
import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.gui.widgets.AudioOptionsConfig;
import net.denanu.dynamicsoundmanager.gui.widgets.AudioOptionsConfig.AudioOptionsEntry;
import net.denanu.dynamicsoundmanager.gui.widgets.AudioOptionsList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class GuiDynamicConfigurer extends GuiListBase<AudioOptionsEntry, AudioOptionsConfig, AudioOptionsList> {
	private static Identifier id;

	public GuiDynamicConfigurer() {
		super(10, 50);
	}
	public GuiDynamicConfigurer(final int listX, final int listY) {
		super(listX, listY);
	}

	@Override
	public void initGui() {
		super.initGui();

		this.title = StringUtils.translate(DynamicSoundManager.MOD_ID + ".gui.title.dynamic-configurer");
		this.addSoundGroupSelection();
	}

	private void addSoundGroupSelection() {
		int x = 20;
		final int y = 20;
		final int h = 20;
		final MinecraftClient client = MinecraftClient.getInstance();
		if (client.world == null) {
			x = Utils.buildNextX(x, this.addWidget(new WidgetLabel(
					x, y, -1, h, 0xFF0000, StringUtils.translate(DynamicSoundManager.MOD_ID + ".gui.title.not-in-game")
					)));
		}
		else if (ClientSoundGroupManager.soundIds.isEmpty()) {
			x = Utils.buildNextX(x, this.addWidget(new WidgetLabel(
					x, y, -1, h, 0xFF0000, StringUtils.translate(DynamicSoundManager.MOD_ID + ".gui.title.no-sounds")
					)));
		}
		else {
			for (final Identifier id: ClientSoundGroupManager.soundIds) {
				x = Utils.buildNextX(x, this.getIdChooseButton(id, x, y, -1, h));
			}
			if (Utils.hasModificationPermission()) {
				this.addImportButton(this.width - 2, this.height - 2, h);
			}
		}
	}


	private ButtonGeneric getIdChooseButton(final Identifier id, final int x, final int y, final int width, final int height) {
		final ButtonGeneric button = new ButtonGeneric(x, y, width, height, StringUtils.translate(id.toTranslationKey()));
		button.setEnabled(GuiDynamicConfigurer.id != id);
		return this.addButton(button, (b, mouse) -> {
			this.setId(id);
		});
	}

	private ButtonGeneric addImportButton(final int x, final int y, final int height) {
		final ButtonGeneric button = new ButtonGeneric(x, y - height, -1, height, StringUtils.translate(DynamicSoundManager.MOD_ID + ".button.import"));
		button.setX(x - button.getWidth());

		return this.addButton(button, (b, m) -> {
			final GuiFileManager gui = new GuiFileManager();
			gui.setParent(this);
			GuiBase.openGui(gui);
		});
	}

	private void setId(final Identifier id2) {
		GuiDynamicConfigurer.id = id2;
		this.resize(this.client, this.width, this.height);
	}

	public static Identifier getId() {
		return GuiDynamicConfigurer.id;
	}


	@Override
	protected AudioOptionsList createListWidget(final int listX, final int listY) {
		return new AudioOptionsList(listX, listY, 0, 0);
	}


	@Override
	protected int getBrowserWidth() {
		return this.width;
	}


	@Override
	protected int getBrowserHeight() {
		return this.height - 75;
	}
}
