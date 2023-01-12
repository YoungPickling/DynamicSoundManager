package net.denanu.dynamicsoundmanager.gui.widgets;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetContainer;
import net.denanu.dynamicsoundmanager.gui.Icons;
import net.denanu.dynamicsoundmanager.utils.SimpleAudioPlayer;
import net.minecraft.client.util.math.MatrixStack;

public class AudioPlayerWidget extends WidgetContainer {
	private final SimpleAudioPlayer player;

	ButtonGeneric playButton;

	public AudioPlayerWidget(final int x, final int y, final int width, final int height) {
		super(x, y, width, height);

		this.player = new SimpleAudioPlayer();

		this.initGui();
	}

	private void initGui() {
		this.addPlayButton();
	}

	@Override
	public void render(final int mouseX, final int mouseY, final boolean selected, final MatrixStack matrixStack) {
		super.render(mouseX, mouseY, selected, matrixStack);
	}

	private void addPlayButton() {
		this.playButton = new ButtonGeneric(this.x, this.y, Icons.PLAY_BUTTON, "test");
		this.playButton.setEnabled(false);
		this.addButton(this.playButton, (b, mouse) -> {
			this.player.play();
		});
	}

	public void load(final File file) {
		try {
			this.player.load(file);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void enable() {
		this.playButton.setEnabled(true);
	}
}
