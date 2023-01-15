package net.denanu.dynamicsoundmanager.groups;

import java.nio.file.Path;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SoundGroup {
	private final Identifier id;

	public SoundGroup(final Identifier id) {
		this.id = id;
	}

	public SoundGroup(final PacketByteBuf buf) {
		this.id = buf.readIdentifier();
	}

	public void writeBuf(final PacketByteBuf buf) {
		buf.writeIdentifier(this.id);
	}

	public Identifier getId() {
		return this.id;
	}

	public Path getPath() {
		return ServerSoundGroups.path.resolve(this.id.toString());
	}
}
