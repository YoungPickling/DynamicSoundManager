package net.denanu.dynamicsoundmanager.gui.widgets.fields;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import net.minecraft.client.font.TextRenderer;

public class GuiAudioFloatConfigField extends GuiAudioConfigFieldBase<Float> {
	private static final Pattern PATTER_NUMBER = Pattern.compile("^-?([0-9]+(\\.[0-9]*)?)?");

	public GuiAudioFloatConfigField(final int x, final int y, final int width, final int height, final TextRenderer textRenderer,
			final Consumer<Float> listener, final Supplier<Float> initValue) {
		super(x, y, width, height, textRenderer, listener);

		this.setTextPredicate(input -> {
			if (input.length() > 0 && !GuiAudioFloatConfigField.PATTER_NUMBER.matcher(input).matches())
			{
				return false;
			}

			return true;
		});

		this.setText(initValue.get().toString());
	}

	@Override
	public void accept(final String str) {
		if (!str.isBlank()) {
			final float val = Float.parseFloat(str);
			this.listener.accept(val);
		}
	}

	public static GuiAudioFloatConfigField make(final int x, final int y, final int width, final int height, final TextRenderer txt, final Consumer<Float> change, final Supplier<Float> initValue) {
		return new GuiAudioFloatConfigField(x, y, width, height, txt, change, initValue);
	}
}
