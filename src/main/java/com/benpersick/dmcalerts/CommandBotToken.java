package com.benpersick.dmcalerts;

import java.util.prefs.Preferences;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBotToken implements CommandExecutor {
	private Preferences prefs;
	private DiscordMinecraftAlertsPlugin plugin;
	
	public CommandBotToken(DiscordMinecraftAlertsPlugin plugin) {
		this.plugin = plugin;
		prefs = Preferences.userNodeForPackage(CommandBotToken.class);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// ensure correct number of args
		if (args.length != 1) {
			sender.sendMessage(Color.RED + "Incorrect number of arguments");
			
			return false;
		}
		
		// store token and start bot
		prefs.put(DiscordMinecraftAlertsPlugin.DISCORD_TOKEN_KEY, args[0]);
		sender.sendMessage(Color.GREEN + "Token set successfully.");
		
		if (!plugin.initializeDiscordBot()) {
			sender.sendMessage(Color.ORANGE + "Bot could not be started.");
		}
		
		return true;
	}
}
