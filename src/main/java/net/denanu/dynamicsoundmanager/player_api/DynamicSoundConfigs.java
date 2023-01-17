package net.denanu.dynamicsoundmanager.player_api;

import net.denanu.dynamicsoundmanager.utils.FileKey;
import net.minecraft.client.sound.Sound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class DynamicSoundConfigs {
	Identifier id;
	String key;
	float volume = 1f, pitch = 1f;
	Sound.RegistrationType registrationType = Sound.RegistrationType.FILE;
	boolean stream = false, preload = false;
	int attenuation = 16;

	public DynamicSoundConfigs(final Identifier id, final String key) {
		this.id = id;
		this.key = key;
	}

	public DynamicSoundConfigs(final PacketByteBuf buf) {
		this.id 				= buf.readIdentifier();
		this.key 				= buf.readString();
		this.volume 			= buf.readFloat();
		this.pitch 				= buf.readFloat();
		this.registrationType 	= buf.readEnumConstant(Sound.RegistrationType.class);
		this.stream 			= buf.readBoolean();
		this.preload 			= buf.readBoolean();
		this.attenuation 		= buf.readInt();
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
		buf.writeEnumConstant(this.registrationType);
		buf.writeBoolean(this.stream);
		buf.writeBoolean(this.preload);
		buf.writeInt(this.attenuation);
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
}
