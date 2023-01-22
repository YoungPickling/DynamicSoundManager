package net.denanu.dynamicsoundmanager.gui.widgets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetContainer;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.gui.Icons;
import net.denanu.dynamicsoundmanager.player_api.DynamicWeightedSoundSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicType;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AudioPlayerWidget extends WidgetContainer {
	private ButtonGeneric playButton;
	private PlayStates state;
	private ButtonGeneric stopButton;
	private boolean shouldUpdate = false;

	private final List<AudioFileChangedCallback> audioFileChangedCallbacks = new ArrayList<>();

	enum PlayStates {
		PLAYING,
		STOPED;
	}

	public AudioPlayerWidget(final int x, final int y, final int width, final int height) {
		super(x, y, width, height);
		this.state = PlayStates.STOPED;
		this.initGui();

		final Map<Identifier, WeightedSoundSet> soundSets = ClientSoundGroupManager.getSounds();
		final DynamicWeightedSoundSet soundSet = new DynamicWeightedSoundSet(AudioPlayerWidgetGlobals.MUSIC_PREVIEW_ID, "Dynamic sound Preview sound");
		soundSet.setSound(AudioPlayerWidgetGlobals.PREVIEW_SOUND);
		soundSets.put(
				AudioPlayerWidgetGlobals.MUSIC_PREVIEW_ID,
				soundSet);
	}

	private void initGui() {
		int x = this.x;
		x = this.addPlayPauseButton(x);
		x = this.addStopButton(x);
	}

	private void resetGui() {
		this.shouldUpdate = true;
	}

	private void addPlayButton(final int x) {
		this.playButton = new ButtonGeneric(x, this.y, Icons.PLAY_BUTTON);
		this.playButton.setEnabled(this.hasAudioFile() && this.state == PlayStates.STOPED);
		this.addButton(this.playButton, (b, mouse) -> {
			MinecraftClient.getInstance().getMusicTracker().stop();
			MinecraftClient.getInstance().getMusicTracker().play(AudioPlayerWidgetGlobals.MUSIC);
			this.state = PlayStates.PLAYING;
			this.resetGui();
		});


	}

	public boolean hasAudioFile() {
		return AudioPlayerWidgetGlobals.PREVIEW_SOUND.getLocationStr() != null;
	}

	private int addStopButton(final int x) {
		this.stopButton = new ButtonGeneric(x, this.y, Icons.TERMINATE_BUTTON);
		this.stopButton.setEnabled(this.state == PlayStates.PLAYING);
		this.addButton(this.stopButton, (b, mouse) -> {
			AudioPlayerWidget.playMenuMusic();
			this.state = PlayStates.STOPED;
			this.resetGui();

		});

		return x + this.stopButton.getWidth() + 2;
	}

	public static void playMenuMusic() {
		MinecraftClient.getInstance().getMusicTracker().stop();
		MinecraftClient.getInstance().getMusicTracker().play(MusicType.MENU);
	}

	private int addPlayPauseButton(final int x) {
		this.addPlayButton(x);

		return x + this.playButton.getWidth() + 2;
	}

	public void load(final File file) {
		AudioPlayerWidgetGlobals.PREVIEW_SOUND.setLocation(file.getAbsolutePath());
		this.updateCallbacks();
	}

	public void enable() {
		this.playButton.setEnabled(true);
	}

	public void remove() {
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
		super.render(mouseX, mouseY, selected, matrixStack);
	}


	public void addAudioFileChangedCallbacks(final AudioFileChangedCallback calback) {
		this.audioFileChangedCallbacks.add(calback);
	}

	public void clearAudioFileChangedCallbacks() {
		this.audioFileChangedCallbacks.clear();
	}

	private void updateCallbacks() {
		for (final AudioFileChangedCallback calback : this.audioFileChangedCallbacks) {
			calback.update(AudioPlayerWidget.getAudioFile());
		}
	}

	public interface AudioFileChangedCallback {
		void update(File audiofile);
	}

	public static File getAudioFile() {
		return new File(AudioPlayerWidgetGlobals.PREVIEW_SOUND.getLocationStr());
	}
}
