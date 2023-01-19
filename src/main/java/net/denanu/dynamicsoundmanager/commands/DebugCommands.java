package net.denanu.dynamicsoundmanager.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.dynamicsoundmanager.groups.ServerSoundGroups;
import net.denanu.dynamicsoundmanager.player_api.DebugSounds;
import net.denanu.dynamicsoundmanager.player_api.DynamicSoundConfigs;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

public class DebugCommands {
	public static void register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access, final CommandManager.RegistrationEnvironment env) {
		dispatcher.register(CommandManager.literal("testSound").executes(
				DebugCommands::playTest
				)
				);
	}

	private static int playTest(final CommandContext<ServerCommandSource> context) {
		final DynamicSoundConfigs config = ServerSoundGroups.playSound(
				context.getSource().getWorld(),
				null,
				context.getSource().getPlayer().getPos(),
				DebugSounds.TEST,
				SoundCategory.MASTER,
				1,
				1);

		context.getSource().sendFeedback(Text.of(config.getKey()), false);
		return 1;
	}
}
