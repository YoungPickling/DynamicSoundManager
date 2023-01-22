package net.denanu.dynamicsoundmanager.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetLabel;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import fi.dy.masa.malilib.util.StringUtils;
import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.gui.Utils;
import net.denanu.dynamicsoundmanager.gui.widgets.AudioOptionsConfig.AudioOptionsEntry;
import net.denanu.dynamicsoundmanager.gui.widgets.fields.GuiAudioFloatConfigField;
import net.denanu.dynamicsoundmanager.gui.widgets.fields.GuiAudioIntConfigField;
import net.denanu.dynamicsoundmanager.networking.c2s.DeleteSoundInGroupC2SPacket;
import net.denanu.dynamicsoundmanager.networking.c2s.UpdatePlayConfigsC2SPacket;
import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AudioOptionsConfig extends WidgetListEntryBase<AudioOptionsEntry> {
	List<TextFieldWrapper<? extends GuiTextFieldGeneric>> textFields;
	ButtonGeneric upload;

	public AudioOptionsConfig(final int x, final int y, final int width, final int height, final AudioOptionsEntry entry, final int listIndex) {
		super(x, y, width, height, entry, listIndex);
		this.textFields = new ArrayList<>();
		this.initGui();
	}

	private void initGui() {
		int x = this.x;
		int h = 20;
		final int xBack = this.x + this.width-104;
		int y = this.y;
		int w = 51;

		x = Utils.buildNextX(x, this.addNameWidget(x, y, h));

		if (Utils.hasModificationPermission()) {
			this.addDeleteButton(xBack - 2,     y, h, w);
			y = Utils.buildNextY(this.addUploadButton(xBack+w, y, h, w));

			h = 10;
			w = 100;

			y = Utils.buildNextY(this.addPitchControll(xBack, y, h, w));
			y = Utils.buildNextY(this.addVolumeControll(xBack, y, h, w));
			y = Utils.buildNextY(this.addAttenuationControll(xBack, y, h, w));
			y = Utils.buildNextY(this.addWeightControll(xBack, y, h, w));
		}
	}

	private WidgetBase addUploadButton(final int x, final int y, final int h, final int w) {
		this.upload = this.addButton(
				new ButtonGeneric(x, y, w, h, StringUtils.translate(DynamicSoundManager.MOD_ID + ".gui.button.upload")),
				(b, m) -> this.doUpload()
				);
		this.upload.setEnabled(false);
		return this.upload;
	}

	private void doUpload() {
		UpdatePlayConfigsC2SPacket.send(this.getEntry().config);
		this.getEntry().config.update();
		this.update();
	}

	private void deleteFile() {
		DeleteSoundInGroupC2SPacket.send(this.getEntry().config.getId(), this.getEntry().config.getKey());
	}

	private WidgetBase addNameWidget(final int x, final int y, final int h) {
		return this.addWidget(
				new WidgetLabel(x, y, -1, h, 0xFFFFFF, this.getEntry().getKey())
				);
	}

	private ButtonGeneric addDeleteButton(final int x, final int y, final int h, final int w) {
		return this.addButton(
				new ButtonGeneric(x, y, w, h, StringUtils.translate(DynamicSoundManager.MOD_ID + ".gui.button.delete")),
				(b, m) -> this.deleteFile()
				);
	}

	private void addLabel(final int x, final int y, final int h, final String key) {
		final String str = StringUtils.translate(DynamicSoundManager.MOD_ID + "." + key)+":";
		final WidgetLabel label = new WidgetLabel(x, y, -1, h, 0xFFFFFF, str);
		label.setWidth(label.getStringWidth(str));
		label.setX(x-label.getWidth()-2);
		this.addWidget(label);
	}

	private NumberInputFieldWidget<Float> addVolumeControll(final int x, final int y, final int h, final int w) {
		this.addLabel(x, y, h, "gui.slider.volume");
		return this.addWidget(
				new NumberInputFieldWidget<>(x, y, w, h, this::setVolume, this::getVolume, GuiAudioFloatConfigField::make)
				);
	}

	private NumberInputFieldWidget<Float> addPitchControll(final int x, final int y, final int h, final int w) {
		this.addLabel(x, y, h, "gui.slider.pitch");
		return this.addWidget(
				new NumberInputFieldWidget<>(x, y, w, h, this::setPitch, this::getPitch, GuiAudioFloatConfigField::make)
				);
	}

	private NumberInputFieldWidget<Integer> addAttenuationControll(final int x, final int y, final int h, final int w) {
		this.addLabel(x, y, h, "gui.slider.attenuation");
		return this.addWidget(
				new NumberInputFieldWidget<>(x, y, w, h, this::setAttenuation, this::getAttenuation, GuiAudioIntConfigField::make)
				);
	}

	private NumberInputFieldWidget<Integer> addWeightControll(final int x, final int y, final int h, final int w) {
		this.addLabel(x, y, h, "gui.slider.weight");
		return this.addWidget(
				new NumberInputFieldWidget<>(x, y, w, h, this::setWeight, this::getWeight, GuiAudioIntConfigField::make)
				);
	}


	private void setVolume(final float val) {
		this.getEntry().setVolume(val);
		this.update();
	}

	private float getVolume() {
		return this.getEntry().getVolume();
	}

	private void setPitch(final float val) {
		this.getEntry().setPitch(val);
		this.update();
	}

	private float getPitch() {
		return this.getEntry().getPitch();
	}

	private void setAttenuation(final int val) {
		this.entry.setAttenuation(val);
		this.update();
	}

	private int getAttenuation() {
		return this.getEntry().getAttenuation();
	}

	private void setWeight(final int val) {
		this.entry.setWeight(val);
		this.update();
	}

	private int getWeight() {
		return this.getEntry().getWeight();
	}

	private void update() {
		this.upload.setEnabled(this.getEntry().isModified());
	}

	@Override
	public boolean onMouseClicked(final int mouseX, final int mouseY, final int mouseButton)
	{
		if (!this.subWidgets.isEmpty())
		{
			for (final WidgetBase widget : this.subWidgets)
			{
				widget.onMouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		this.onMouseClickedImpl(mouseX, mouseY, mouseButton);
		return false;
	}

	public static class AudioOptionsEntry implements Comparable<AudioOptionsEntry>
	{
		private final DynamicSoundConfigs config;

		public AudioOptionsEntry(final DynamicSoundConfigs config)
		{
			this.config = config;
		}

		public boolean isModified() {
			return this.config.isModified();
		}

		public String getKey() {
			return this.config.getKey();
		}

		public static AudioOptionsEntry of(final DynamicSoundConfigs config) {
			return new AudioOptionsEntry(config);
		}

		@Override
		public int compareTo(final AudioOptionsEntry other)
		{
			return this.config.getKey().toLowerCase(Locale.US).compareTo(other.config.getKey().toLowerCase(Locale.US));
		}

		public void setVolume(final float volume) {
			this.config.setVolume(volume);
		}

		public float getVolume() {
			return this.config.getVolume();
		}

		public void setPitch(final float pitch) {
			this.config.setPitch(pitch);
		}

		public float getPitch() {
			return this.config.getPitch();
		}

		public void setAttenuation(final int val) {
			this.config.setAttenuation(val);
		}

		public int getAttenuation() {
			return this.config.getAttenuation();
		}

		public void setWeight(final int val) {
			this.config.setWeight(val);
		}

		public int getWeight() {
			return this.config.getWeight();
		}
	}
}
