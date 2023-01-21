package net.denanu.dynamicsoundmanager.gui.widgets.fields;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import net.minecraft.client.font.TextRenderer;

public class GuiAudioIntConfigField extends GuiAudioConfigFieldBase<Integer> {
	private static final Pattern PATTER_NUMBER = Pattern.compile("-?[0-9]*");

	public GuiAudioIntConfigField(final int x, final int y, final int width, final int height, final TextRenderer textRenderer,
			final Consumer<Integer> listener, final Supplier<Integer> initValue) {
		super(x, y, width, height, textRenderer, listener);

		this.setTextPredicate(input -> {
			if (input.length() > 0 && !GuiAudioIntConfigField.PATTER_NUMBER.matcher(input).matches())
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
			final int val = Integer.parseInt(str);
			this.listener.accept(val);
		}
	}

	public static GuiAudioIntConfigField make(final int x, final int y, final int width, final int height, final TextRenderer txt, final Consumer<Integer> change, final Supplier<Integer> initValue) {
		return new GuiAudioIntConfigField(x, y, width, height, txt, change, initValue);
	}
}
