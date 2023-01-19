package net.denanu.dynamicsoundmanager.player_api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.sound.sampled.AudioFormat;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.OggAudioStream;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.StaticSound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.floatprovider.FloatSupplier;

public class DynamicSound extends Sound {
	private final String key;

	public static final String SHOULD_LOAD_DYNAMICLY = DynamicSoundManager.MOD_ID + ".dynamic_loader";

	public DynamicSound(final Identifier id, final FloatSupplier volume, final FloatSupplier pitch, final int weight,
			final RegistrationType registrationType, final boolean stream, final boolean preload, final int attenuation, final String key) {
		super(id.toString(), volume, pitch, weight, registrationType, stream, preload, attenuation);
		this.key = key;
	}

	@Override
	public Identifier getLocation() {
		final Identifier id = new Identifier(
				ClientSoundGroupManager.getChach(MinecraftClient.getInstance()).toAbsolutePath().toString()
				+ "/"
				+ this.getIdentifier().getNamespace()
				+ "/"
				+ this.getIdentifier().getPath()
				+ "/"
				+ this.key,
				DynamicSound.SHOULD_LOAD_DYNAMICLY);
		DynamicSoundManager.LOGGER.info(id.toString());
		return id;
	}

	public String getKey() {
		return this.key;
	}

	public static CompletableFuture<StaticSound> loadDynamicSound(final Identifier id) {
		return CompletableFuture.supplyAsync(() -> {
			try (InputStream inputStream = new FileInputStream(id.getNamespace())){
				StaticSound staticSound;
				try (OggAudioStream oggAudioStream = new OggAudioStream(inputStream);){
					final ByteBuffer byteBuffer = oggAudioStream.getBuffer();

					final AudioFormat format = oggAudioStream.getFormat();
					final float frames = format.getFrameSize() * format.getFrameRate();
					final float plyTime = byteBuffer.limit() / frames;
					DynamicSoundManager.LOGGER.info(Float.toString(plyTime));

					staticSound = new StaticSound(byteBuffer, oggAudioStream.getFormat());
				}
				return staticSound;
			}
			catch (final IOException iOException) {
				throw new CompletionException(iOException);
			}
		}, Util.getMainWorkerExecutor());
	}
}
