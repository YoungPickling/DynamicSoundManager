package net.denanu.dynamicsoundmanager.player_api;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.FloatSupplier;

public class DynamicSound extends Sound {
	private final String key;

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
				"");
		DynamicSoundManager.LOGGER.info(id.toString());
		return id;
	}

	public String getKey() {
		return this.key;
	}
}
