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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class DynamicSoundConfigs {
	private Identifier id;
	private String key;
	private float volume = 1f, pitch = 1f;
	private Sound.RegistrationType registrationType = Sound.RegistrationType.FILE;
	private boolean stream = false, preload = false;
	private int attenuation = 16;
	private int weight = 1;
	private Optional<Integer> tickPlayTime;

	private boolean modified = false;

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
		this.weight				= buf.readInt();
		this.setup();
	}

	public void load(final NbtCompound nbt) {
		this.key 				= nbt.getString("key");
		this.volume				= nbt.getFloat("volume");
		this.pitch				= nbt.getFloat("pitch");
		this.stream				= nbt.getBoolean("stream");
		this.preload			= nbt.getBoolean("preload");
		this.attenuation		= nbt.getInt("attenuation");
		this.weight				= nbt.getInt("weight");
		this.setup();
	}

	public void set(final DynamicSoundConfigs config) {
		this.id 				= config.id;
		this.key 				= config.key;
		this.volume 			= config.volume;
		this.pitch 				= config.pitch;
		this.registrationType	= config.registrationType;
		this.stream 			= config.stream;
		this.preload 			= config.preload;
		this.attenuation 		= config.attenuation;
		this.weight				= config.weight;
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
		buf.writeInt(this.weight);
		this.setup();
		return buf;
	}

	public NbtCompound toNbt() {
		final NbtCompound nbt = new NbtCompound();

		nbt.putString("key", 		this.key);
		nbt.putFloat("volume", 		this.volume);
		nbt.putFloat("pitch", 		this.pitch);
		nbt.putBoolean("stream",	this.stream);
		nbt.putBoolean("preload",	this.preload);
		nbt.putInt("attenuation",	this.attenuation);
		nbt.putInt("weight", this.weight);

		return nbt;
	}

	public void setVolume(final float volume) {
		if (volume != this.volume) {
			this.volume = volume;
			this.change();
		}
	}

	public float getVolume() {
		return this.volume;
	}

	public void setPitch(final float pitch) {
		if (this.pitch != pitch) {
			this.pitch = pitch;
			this.change();
		}
	}

	public float getPitch() {
		return this.pitch;
	}

	public void setRegistrationType(final Sound.RegistrationType type) {
		if (this.registrationType != type) {
			this.registrationType = type;
			this.change();
		}
	}

	public Sound.RegistrationType getRegistrationType() {
		return this.registrationType;
	}

	public void setStream(final boolean stream) {
		if (this.stream != stream) {
			this.stream = stream;
			this.change();
		}
	}

	public boolean getStream() {
		return this.stream;
	}

	public void setPreload(final boolean preload) {
		if (this.preload != preload) {
			this.preload = preload;
			this.change();
		}
	}

	public boolean getPreload() {
		return this.preload;
	}

	public void setAttenuation(final int attenuation) {
		if (this.attenuation != attenuation) {
			this.attenuation = attenuation;
			this.change();
		}
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

	public void setWeight(final int weight) {
		if (this.weight != weight) {
			this.weight = weight;
			this.change();
		}
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

	private void change() {
		this.modified = true;
	}

	public boolean isModified() {
		return this.modified;
	}

	// client only
	public void update() {
		this.modified = false;
	}
}
