package net.denanu.dynamicsoundmanager.gui.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.gui.GuiDynamicConfigurer;
import net.denanu.dynamicsoundmanager.gui.Utils;
import net.denanu.dynamicsoundmanager.gui.widgets.AudioOptionsConfig.AudioOptionsEntry;
import net.denanu.dynamicsoundmanager.mixin.client.IWeightedSoundSetMixin;
import net.denanu.dynamicsoundmanager.player_api.DynamicSound;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundContainer;
import net.minecraft.util.Identifier;

public class AudioOptionsList extends WidgetListBase<AudioOptionsEntry, AudioOptionsConfig>
{
	public AudioOptionsList(final int x, final int y, final int width, final int height)
	{
		super(x, y, width, height, null);

		this.allowMultiSelection = true;
		this.browserEntryHeight = Utils.hasModificationPermission() ? 70 : 22;
	}

	@Override
	protected Collection<AudioOptionsEntry> getAllEntries()
	{
		final Identifier id = GuiDynamicConfigurer.getId();
		if (id!= null) {
			final List<SoundContainer<Sound>> sounds = ((IWeightedSoundSetMixin)ClientSoundGroupManager.getSounds().get(id)).getSounds();

			final ArrayList<AudioOptionsEntry> configs = new ArrayList<>();
			for (final SoundContainer<Sound> sound: sounds) {
				final Sound audio = sound.getSound(null);
				if (audio instanceof final DynamicSound dynamic_sound) {
					configs.add(AudioOptionsEntry.of(dynamic_sound.getConfig()));
				}
			}

			return configs;
		}
		return List.of();
	}

	@Override
	public boolean onMouseScrolled(final int mouseX, final int mouseY, final double mouseWheelDelta)
	{
		if (mouseX >= this.posX && mouseX <= this.posX + this.browserWidth && mouseY >= this.posY && mouseY <= this.posY + this.browserHeight)
		{
			this.offsetSelectionOrScrollbar(mouseWheelDelta < 0 ? 1 : -1, false);
			return true;
		}
		return super.onMouseScrolled(mouseX, mouseY, mouseWheelDelta);
	}

	@Override
	protected AudioOptionsConfig createListEntryWidget(final int x, final int y, final int listIndex, final boolean isOdd,
			final AudioOptionsEntry entry) {
		return new AudioOptionsConfig(x, y, this.browserEntryWidth, this.getBrowserEntryHeightFor(entry), entry, listIndex);
	}

	@Override
	public boolean onMouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		for (final AudioOptionsConfig widget : this.listWidgets) {
			widget.onMouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}

	@Override
	public boolean onKeyTyped(final int keyCode, final int scanCode, final int modifiers)
	{
		if (super.onKeyTyped(keyCode, scanCode, modifiers)) {
			return true;
		}
		for (final AudioOptionsConfig entry : this.listWidgets) {
			if (entry.onKeyTyped(keyCode, scanCode, modifiers)) {
				return true;
			}
		}
		return false;
	}
}
