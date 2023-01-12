package net.denanu.dynamicsoundmanager.utils;

// Java program to play an Audio
// file using Clip Object
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SimpleAudioPlayer
{
	public enum States {
		PLAY,
		PAUSED,
	}

	// to store current position
	Long currentFrame;
	Clip clip;

	// current status of clip
	States status;

	AudioInputStream audioInputStream;
	SourceDataLine line;
	AudioFormat outFormat;
	File filePath;

	// constructor to initialize streams and clip
	public SimpleAudioPlayer() {}

	public void load(final File filePath)
			throws UnsupportedAudioFileException,
			IOException, LineUnavailableException
	{
		// create AudioInputStream object
		this.filePath = filePath;
		this.audioInputStream = AudioSystem.getAudioInputStream(filePath);
		this.outFormat = this.getOutFormat(this.audioInputStream.getFormat());
		final Info info = new Info(SourceDataLine.class, this.outFormat);
		this.line = (SourceDataLine) AudioSystem.getLine(info);
		this.line.open(this.outFormat);
	}

	private AudioFormat getOutFormat(final AudioFormat inFormat) {
		final int ch = inFormat.getChannels();
		final float rate = inFormat.getSampleRate();
		return new AudioFormat(Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	}

	// Work as the user enters his choice

	public void gotoChoice(final int c)
			throws IOException, LineUnavailableException, UnsupportedAudioFileException
	{
		switch (c)
		{
		case 1:
			this.pause();
			break;
		case 2:
			this.resumeAudio();
			break;
		case 3:
			this.restart();
			break;
		case 4:
			this.stop();
			break;
		case 5:
			System.out.println("Enter time (" + 0 +
					", " + this.clip.getMicrosecondLength() + ")");
			final Scanner sc = new Scanner(System.in);
			final long c1 = sc.nextLong();
			this.jump(c1);
			break;

		}

	}

	// Method to play the audio
	public void play()
	{
		//start the clip
		this.clip.start();

		this.status = States.PLAY;
	}

	// Method to pause the audio
	public void pause()
	{
		if (States.PAUSED.equals(this.status))
		{
			System.out.println("audio is already paused");
			return;
		}
		this.currentFrame =
				this.clip.getMicrosecondPosition();
		this.clip.stop();
		this.status = States.PAUSED;
	}

	// Method to resume the audio
	public void resumeAudio() throws UnsupportedAudioFileException,
	IOException, LineUnavailableException
	{
		if (States.PLAY.equals(this.status))
		{
			System.out.println("Audio is already "+
					"being played");
			return;
		}
		this.clip.close();
		this.resetAudioStream();
		this.clip.setMicrosecondPosition(this.currentFrame);
		this.play();
	}

	// Method to restart the audio
	public void restart() throws IOException, LineUnavailableException,
	UnsupportedAudioFileException
	{
		this.clip.stop();
		this.clip.close();
		this.resetAudioStream();
		this.currentFrame = 0L;
		this.clip.setMicrosecondPosition(0);
		this.play();
	}

	// Method to stop the audio
	public void stop() throws UnsupportedAudioFileException,
	IOException, LineUnavailableException
	{
		this.currentFrame = 0L;
		this.clip.stop();
		this.clip.close();
	}

	// Method to jump over a specific part
	public void jump(final long c) throws UnsupportedAudioFileException, IOException,
	LineUnavailableException
	{
		if (c > 0 && c < this.clip.getMicrosecondLength())
		{
			this.clip.stop();
			this.clip.close();
			this.resetAudioStream();
			this.currentFrame = c;
			this.clip.setMicrosecondPosition(c);
			this.play();
		}
	}

	// Method to reset audio stream
	public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
	LineUnavailableException
	{
		this.audioInputStream = AudioSystem.getAudioInputStream(
				this.filePath.getAbsoluteFile());
		this.clip.open(this.audioInputStream);
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

}
