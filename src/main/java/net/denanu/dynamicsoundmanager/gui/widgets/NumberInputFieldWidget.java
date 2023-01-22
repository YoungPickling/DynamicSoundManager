package net.denanu.dynamicsoundmanager.gui.widgets;

import java.util.function.Consumer;
import java.util.function.Supplier;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.KeyCodes;
import net.denanu.dynamicsoundmanager.gui.widgets.fields.GuiAudioConfigFieldBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class NumberInputFieldWidget<E> extends WidgetBase {
	protected final GuiAudioConfigFieldBase<E> txtBox;
	protected boolean txtActive;

	public interface FiledBaseProvider<E> {
		GuiAudioConfigFieldBase<E> make(int x, int y, int width, int height, TextRenderer txt, Consumer<E> change, Supplier<E> initValue);
	}

	public NumberInputFieldWidget(final int x, final int y, final int width, final int height, final Consumer<E> change, final Supplier<E> initValue, final FiledBaseProvider<E> maker) {
		super(x, y, width, height);
		this.txtActive = false;

		this.txtBox = maker.make(x, y, width, height, this.textRenderer, change, initValue);
		this.txtBox.setZLevel(this.zLevel);
	}

	@Override
	public void setPosition(final int x, final int y)
	{
		super.setPosition(x, y);
		this.txtBox.setX(x);
		this.txtBox.setY(y);
	}

	@Override
	public boolean onMouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		return this.onMouseClickedImpl(mouseX, mouseY, mouseButton);
	}

	@Override
	protected boolean onMouseClickedImpl(final int mouseX, final int mouseY, final int mouseButton)
	{
		if (this.txtBox.mouseClicked(mouseX, mouseY, mouseButton)) {
			if (!this.txtActive)
			{
				this.setTextActive(true);
			}

		}
		else {
			this.setTextActive(false);
		}

		return false;
	}

	private void setTextActive(final boolean open) {
		this.txtActive = open;
		this.txtBox.setFocused(open);
		this.txtBox.setEditable(open);
	}

	@Override
	protected boolean onKeyTypedImpl(final int keyCode, final int scanCode, final int modifiers)
	{
		if (this.txtActive)
		{
			if (this.txtBox.keyPressed(keyCode, scanCode, modifiers))
			{
				return true;
			}
			if (keyCode == KeyCodes.KEY_ESCAPE)
			{
				if (GuiBase.isShiftDown())
				{
					this.mc.currentScreen.close();
				}

				this.txtActive = false;
				this.txtBox.setFocused(false);
				return true;
			}
		}

		return false;
	}

	@Override
	protected boolean onCharTypedImpl(final char charIn, final int modifiers)
	{
		if (this.txtActive && this.txtBox.charTyped(charIn, modifiers))
		{
			return true;
		}

		return false;
	}

	@Override
	public void render(final int mouseX, final int mouseY, final boolean selected, final MatrixStack matrixStack)
	{
		RenderUtils.color(1f, 1f, 1f, 1f);

		this.txtBox.render(matrixStack, mouseX, mouseY, 0);
	}
}
