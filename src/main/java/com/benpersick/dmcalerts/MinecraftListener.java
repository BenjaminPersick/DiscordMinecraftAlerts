package com.benpersick.dmcalerts;

import java.util.logging.Level;
import java.util.prefs.Preferences;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.dv8tion.jda.api.JDA;

public class MinecraftListener implements Listener {
	private Preferences prefs;
	
	public MinecraftListener() {
		// initialize preferences
		prefs = Preferences.userNodeForPackage(MinecraftListener.class);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// attempt to retrieve bot
		@Nullable
		JDA bot = DiscordMinecraftAlertsPlugin.getDiscordBot();
		
		if (bot == null) {
			Bukkit.getLogger().log(Level.WARNING, "Unable to send join message: JDA not initialized.");
			return;
		}
		
		// attempt to retrieve server id
		long serverID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_SERVER_ID_KEY, 0L);
		
		if (serverID == 0) {
		    Bukkit.getLogger().log(Level.WARNING, "Unable to send join message: Discord server ID not set.");
		    return;
		}
		
		// attempt to retrieve channel id
		long channelID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_CHANNEL_ID_KEY, 0L);
		
		if (channelID == 0) {
			Bukkit.getLogger().log(Level.WARNING, "Unable to send join message: Discord channel ID not set.");
			return;
		}
		
		// send message
		bot.getGuildById(serverID).getTextChannelById(channelID).sendMessage("*" + event.getPlayer().getName() + " joined the game*");
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// attempt to retrieve bot
		@Nullable
		JDA bot = DiscordMinecraftAlertsPlugin.getDiscordBot();
		
		if (bot == null) {
			Bukkit.getLogger().log(Level.WARNING, "Unable to send quit message: JDA not initialized.");
			return;
		}
		
		// attempt to retrieve server id
        long serverID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_SERVER_ID_KEY, 0L);
        
        if (serverID == 0) {
            Bukkit.getLogger().log(Level.WARNING, "Unable to send quit message: Discord server ID not set.");
            return;
        }
		
		// attempt to retrieve channel id
		long channelID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_CHANNEL_ID_KEY, 0L);
		
		if (channelID == 0) {
			Bukkit.getLogger().log(Level.WARNING, "Unable to send quit message: Discord channel ID not set.");
			return;
		}
		
		// send message
		bot.getGuildById(serverID).getTextChannelById(channelID).sendMessage("*" + event.getPlayer().getName() + " left the game*");
	}
}
