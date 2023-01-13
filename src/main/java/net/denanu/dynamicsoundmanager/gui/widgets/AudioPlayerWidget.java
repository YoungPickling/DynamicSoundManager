package net.denanu.dynamicsoundmanager.gui.widgets;

import java.io.File;
import java.util.Optional;

import com.puttysoftware.audio.ogg.OggFile;

import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetContainer;
import net.denanu.dynamicsoundmanager.gui.Icons;
import net.minecraft.client.util.math.MatrixStack;

public class AudioPlayerWidget extends WidgetContainer {
	File audioFile;

	Optional<OggFile> player;

	ButtonGeneric playButton;

	public AudioPlayerWidget(final int x, final int y, final int width, final int height) {
		super(x, y, width, height);

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
			this.player = Optional.of(new OggFile(this.audioFile.getAbsolutePath()));
			this.player.get().run();
		});
	}

	public void load(final File file) {
		this.audioFile = file;
	}

	public void enable() {
		this.playButton.setEnabled(true);
	}
}
