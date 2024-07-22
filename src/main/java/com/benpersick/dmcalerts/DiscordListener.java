package com.benpersick.dmcalerts;

import java.util.prefs.Preferences;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {
    private Preferences prefs;
    
    public DiscordListener() {
        prefs = Preferences.userNodeForPackage(DiscordListener.class);
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // ensure server and channel id are set in prefs
        long serverID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_SERVER_ID_KEY, 0L);
        long channelID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_CHANNEL_ID_KEY, 0L);
        
        if (serverID == 0 || channelID == 0) {
            return;
        }
        
        if (event.getGuild().getIdLong() == serverID && event.getChannel().getIdLong() == channelID) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE +  "[Discord] " + ChatColor.WHITE + ChatColor.BOLD + event.getAuthor().getName() + ": " + ChatColor.RESET + event.getMessage().getContentDisplay());
            
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F441")).queue();
        }
    }
}
