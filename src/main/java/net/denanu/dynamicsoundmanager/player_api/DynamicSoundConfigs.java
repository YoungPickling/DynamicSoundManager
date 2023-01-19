package net.denanu.dynamicsoundmanager.player_api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.sound.sampled.AudioFormat;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.networking.bidirectional.InitTransferBidirectionalPacket;
import net.denanu.dynamicsoundmanager.utils.FileKey;
import net.minecraft.client.sound.OggAudioStream;
import net.minecraft.client.sound.Sound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class DynamicSoundConfigs {
	private final Identifier id;
	private final String key;
	private float volume = 1f, pitch = 1f;
	private Sound.RegistrationType registrationType = Sound.RegistrationType.FILE;
	private boolean stream = false, preload = false;
	private int attenuation = 16;
	private final int weight = 1;
	private Optional<Integer> tickPlayTime;

	public DynamicSoundConfigs(final Identifier id, final String key) {
		this.id = id;
		this.key = key;
	}

	public DynamicSoundConfigs(final PacketByteBuf buf) {
		this.id 				= buf.readIdentifier();
		this.key 				= buf.readString();
		this.volume 			= buf.readFloat();
		this.pitch 				= buf.readFloat();
		//this.registrationType 	= buf.readEnumConstant(Sound.RegistrationType.class);
		this.stream 			= buf.readBoolean();
		this.preload 			= buf.readBoolean();
		this.attenuation 		= buf.readInt();
		this.setup();
	}

	public static DynamicSoundConfigs of(final Identifier id, final String key) {
		return new DynamicSoundConfigs(id, key);
	}

	public static DynamicSoundConfigs of(final FileKey key) {
		return DynamicSoundConfigs.of(key.getId(), key.getFileName());
	}

	public PacketByteBuf toBuf(final PacketByteBuf buf) {
		buf.writeIdentifier(this.id);
		buf.writeString(this.key);
		buf.writeFloat(this.volume);
		buf.writeFloat(this.pitch);
		//buf.writeEnumConstant(this.registrationType);
		buf.writeBoolean(this.stream);
		buf.writeBoolean(this.preload);
		buf.writeInt(this.attenuation);
		this.setup();
		return buf;
	}

	public void setVolume(final float volume) {
		this.volume = volume;
	}

	public float getVolume() {
		return this.volume;
	}

	public void setPitch(final float pitch) {
		this.pitch = pitch;
	}

	public float getPitch() {
		return this.pitch;
	}

	public void setRegistrationType(final Sound.RegistrationType type) {
		this.registrationType = type;
	}

	public Sound.RegistrationType getRegistrationType() {
		return this.registrationType;
	}

	public void setStream(final boolean stream) {
		this.stream = stream;
	}

	public boolean getStream() {
		return this.stream;
	}

	public void setPreload(final boolean preload) {
		this.preload = preload;
	}

	public boolean getPreload() {
		return this.preload;
	}

	public void setAttenuation(final int attenuation) {
		this.attenuation = attenuation;
	}

	public int getAttenuation() {
		return this.attenuation;
	}

	public Identifier getId() {
		return this.id;
	}

	public String getKey() {
		return this.key;
	}

	public int getWeight() {
		return this.weight;
	}

	public Optional<Integer> getTickPlayTime() {
		return this.tickPlayTime;
	}

	private void setup() {
		final File file = InitTransferBidirectionalPacket.toFile(ServerSoundGroups.path, this.id, this.key);
		this.computePlayTickTime(file);
	}

	private void computePlayTickTime(final File file) {
		CompletableFuture.runAsync(() -> {
			try (InputStream inputStream = new FileInputStream(file)){
				try (OggAudioStream oggAudioStream = new OggAudioStream(inputStream);){
					final ByteBuffer byteBuffer = oggAudioStream.getBuffer();

					final AudioFormat format = oggAudioStream.getFormat();
					final float frames = format.getFrameSize() * format.getFrameRate();
					final float plyTime = byteBuffer.limit() / frames;
					this.tickPlayTime = Optional.of((int)(plyTime * 20));
				}
			}
			catch (final IOException iOException) {
				throw new CompletionException(iOException);
			}
		}, Util.getIoWorkerExecutor());
	}

	@Override
	public String toString() {
		return this.id.toString() + ":" + this.key;
	}
}
