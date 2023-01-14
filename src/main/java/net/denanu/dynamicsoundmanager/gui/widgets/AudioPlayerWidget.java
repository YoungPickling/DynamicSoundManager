package net.denanu.dynamicsoundmanager.gui.widgets;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.puttysoftware.audio.ogg.OggFile;

import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.ISliderCallback;
import fi.dy.masa.malilib.gui.widgets.WidgetContainer;
import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.gui.Icons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicType;
import net.minecraft.client.util.math.MatrixStack;

public class AudioPlayerWidget extends WidgetContainer implements ISliderCallback {
	private File audioFile = null;
	private Optional<OggFile> player;
	private ButtonGeneric playButton;
	private PlayStates state;
	private ButtonGeneric stopButton;
	private boolean shouldUpdate = false;

	enum PlayStates {
		PLAYING,
		STOPED;
	}

	public AudioPlayerWidget(final int x, final int y, final int width, final int height) {
		super(x, y, width, height);
		this.state = PlayStates.STOPED;
		this.player = Optional.empty();
		this.initGui();
	}

	private void initGui() {
		int x = this.x;
		x = this.addPlayPauseButton(x);
		x = this.addStopButton(x);
	}

	private void resetGui() {
		this.shouldUpdate = true;
	}

	@SuppressWarnings("removal")
	private void addPlayButton(final int x) {
		this.playButton = new ButtonGeneric(x, this.y, Icons.PLAY_BUTTON);
		this.playButton.setEnabled(this.audioFile != null);
		this.addButton(this.playButton, (b, mouse) -> {
			if (this.player.isPresent()) {
				this.player.get().resume();
			}
			else {
				this.player = Optional.of(new OggFile(this.audioFile.getAbsolutePath()));
				this.player.get().start();
			}
			this.state = PlayStates.PLAYING;
			MinecraftClient.getInstance().getMusicTracker().stop();
			this.resetGui();
		});


	}

	@SuppressWarnings("removal")
	private void addPauseButton(final int x) {
		this.playButton = new ButtonGeneric(this.x, this.y, Icons.PAUSE_BUTTON);
		this.addButton(this.playButton, (b, mouse) -> {
			this.player.get().suspend();
			AudioPlayerWidget.playMenuMusic();
			this.state = PlayStates.STOPED;
			this.resetGui();
		});
	}

	private int addStopButton(final int x) {
		this.stopButton = new ButtonGeneric(x, this.y, Icons.TERMINATE_BUTTON);
		this.stopButton.setEnabled(this.state == PlayStates.PLAYING);
		this.addButton(this.stopButton, (b, mouse) -> {
			if (this.player.isPresent()) {
				this.player.get().stopPlayer();
				AudioPlayerWidget.playMenuMusic();
				this.state = PlayStates.STOPED;
				this.player = Optional.empty();
				this.resetGui();
			}
		});

		return x + this.stopButton.getWidth() + 2;
	}

	private static void playMenuMusic() {
		MinecraftClient.getInstance().getMusicTracker().play(MusicType.MENU);
	}

	private int addPlayPauseButton(final int x) {
		switch(this.state) {
		case STOPED -> 	this.addPlayButton(x);
		case PLAYING -> this.addPauseButton(x);
		}

		return x + this.playButton.getWidth() + 2;
	}

	public void load(final File file) {
		this.audioFile = file;
	}

	public void enable() {
		this.playButton.setEnabled(true);
	}

	public void remove() {
		if (this.player.isPresent()) {
			this.player.get().stopPlayer();
		}

		if (this.state == PlayStates.PLAYING) {
			AudioPlayerWidget.playMenuMusic();
		}
	}

	@Override
	public void render(final int mouseX, final int mouseY, final boolean selected, final MatrixStack matrixStack) {
		if (this.shouldUpdate) {
			this.subWidgets.clear();
			this.initGui();
			this.shouldUpdate = false;
		}
		if (this.player.isPresent()) {
			try {
				DynamicSoundManager.LOGGER.info(Float.toString(this.player.get().secondsRemaining()));
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		super.render(mouseX, mouseY, selected, matrixStack);
	}

	@Override
	public int getMaxSteps() {
		if (this.player.isPresent()) {
			return (int)this.player.get().getAudioLength();
		}
		return 10;
	}

	@Override
	public double getValueRelative() {
		if (this.player.isPresent()) {
			try {
				return 1-this.player.get().secondsRemaining() / this.player.get().getAudioLength();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public void setValueRelative(final double relativeValue) {
	}

	@Override
	public String getFormattedDisplayValue() {
		return null;
	}
}
