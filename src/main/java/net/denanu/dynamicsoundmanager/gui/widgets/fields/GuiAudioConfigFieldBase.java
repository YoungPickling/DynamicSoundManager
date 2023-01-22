package net.denanu.dynamicsoundmanager.gui.widgets.fields;

import java.util.function.Consumer;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;

@Environment(EnvType.CLIENT)
public abstract class GuiAudioConfigFieldBase<T> extends GuiTextFieldGeneric implements Consumer<String> {
	protected Consumer<T> listener;

	public GuiAudioConfigFieldBase(final int x, final int y, final int width, final int height, final TextRenderer textRenderer, final Consumer<T> listener) {
		super(x, y, width, height, textRenderer);
		this.listener = listener;
		this.setChangedListener(this);
	}

	@Override
	public abstract void accept(String t);
}
