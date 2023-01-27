# Fabric Example Mod

## Install

``` gradle
repositories {
    maven {
        name = 'Denanu Mods'
        url = 'https://wandhoven.ddns.net/maven/'
    }
}

dependencies {
    modImplementation "net.denanu.DynamicSoundManager:DynamicSoundManager-<Minecraft_Version>:<StoppableSound_version>"
}
```

## Usage

In order to use this mod in your own mod, register a new Dynamic Sound Manager as following:
``` java
ServerSoundGroups.register(id);
```
where ```id``` is the SoundEvent Identifier.

### Example
``` java
package net.denanu.dynamicsoundmanager.player_api;

import net.denanu.dynamicsoundmanager.DynamicSoundManager;
import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DebugSounds {
	public static Identifier TEST_ID = Identifier.of(DynamicSoundManager.MOD_ID, "test");

	public static SoundEvent TEST = DebugSounds.register(DebugSounds.TEST_ID);

	private static SoundEvent register(final Identifier id) {
		final SoundEvent event = new SoundEvent(id);
		ServerSoundGroups.register(id);
		return Registry.register(Registry.SOUND_EVENT, id, event);
	}

	public static void setup() {}
}
```
