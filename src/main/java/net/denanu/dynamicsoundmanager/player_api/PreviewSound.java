package net.denanu.dynamicsoundmanager.player_api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.Sound;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class PreviewSound extends Sound {
	private String location;

	public PreviewSound(final String id) {
		super(id, r -> 1f, r -> 1f, 1, RegistrationType.FILE, false, false, 16);
	}

	@Override
	public Identifier getLocation() {
		return new Identifier(
				this.location,
				DynamicSound.SHOULD_LOAD_DYNAMICLY);
	}

	public String getLocationStr() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}
}