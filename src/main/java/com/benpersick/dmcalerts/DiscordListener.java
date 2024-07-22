package com.benpersick.dmcalerts;

import java.util.logging.Level;
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
        // ignore bot messages
        if (event.getAuthor().isBot()) {
            return;
        }
        
        // ensure server and channel id are set in prefs
        long serverID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_SERVER_ID_KEY, 0L);
        long channelID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_CHANNEL_ID_KEY, 0L);
        
        if (serverID == 0 || channelID == 0) {
            Bukkit.getLogger().log(Level.INFO, "Cannot send minecraft message: one of the IDs isn't set in prefs");
            
            return;
        }
        
        if (event.getGuild().getIdLong() == serverID && event.getChannel().getIdLong() == channelID) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE +  "[Discord] " + ChatColor.WHITE + ChatColor.BOLD + event.getAuthor().getEffectiveName() + ": " + ChatColor.RESET + event.getMessage().getContentStripped());
            
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F441")).queue();
        } else {
            Bukkit.getLogger().log(Level.INFO, "Cannot send minecraft message: Server ID or channel ID aren't set correctly");
        }
    }
}
