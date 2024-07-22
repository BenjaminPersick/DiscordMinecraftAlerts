package com.benpersick.dmcalerts;

import java.util.prefs.Preferences;
import javax.annotation.Nullable;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.dv8tion.jda.api.JDA;

public class CommandChannelID implements CommandExecutor {
	private Preferences prefs;
	
	public CommandChannelID() {
		prefs = Preferences.userNodeForPackage(CommandChannelID.class);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// ensure correct number of arguments
		if (args.length != 1) {
			sender.sendMessage(Color.RED +  "Incorrect number of arguments");
			
			return false;
		}
		
		// attempt to convert id to long
		long channelID;
		
		try {
			channelID = Long.parseLong(args[0]);
		} catch (NumberFormatException ex) {
			sender.sendMessage("Please enter a valid long as the channel ID.");
			
			return false;
		}
		
		// store channel ID
		prefs.putLong(DiscordMinecraftAlertsPlugin.DISCORD_CHANNEL_ID_KEY, channelID);
		
		sender.sendMessage(Color.GREEN + "Channel ID successfully set.");
		
		// attempt to send confirmation message
		@Nullable
		JDA bot = DiscordMinecraftAlertsPlugin.getDiscordBot();
		
		long serverID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_SERVER_ID_KEY, 0L);
		
		if (bot == null) {
			sender.sendMessage("Bot is null tho...");
		} else if (serverID == 0) {
		    sender.sendMessage("Server ID is null tho...");
		} else {
			bot.getTextChannelById(channelID).sendMessage("Synced with Minecraft server!").queue();
		}
		
		return true;
	}
}
