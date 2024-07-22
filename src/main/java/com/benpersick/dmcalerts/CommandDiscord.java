package com.benpersick.dmcalerts;

import java.util.prefs.Preferences;
import javax.annotation.Nullable;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.dv8tion.jda.api.JDA;

public class CommandDiscord implements CommandExecutor {
    private Preferences prefs;
    
    public CommandDiscord() {
        prefs = Preferences.userNodeForPackage(CommandDiscord.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // ensure a message was provided
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Please provide a message to send");
            
            return false;
        }
        
        // attempt to retrieve server and channel IDs
        long serverID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_SERVER_ID_KEY, 0L);
        
        if (serverID == 0) {
            sender.sendMessage(ChatColor.GOLD + "Unable to sned message: Discord server ID not set");
            
            return false;
        }
            
        long channelID = prefs.getLong(DiscordMinecraftAlertsPlugin.DISCORD_CHANNEL_ID_KEY, 0L);
        
        if (channelID == 0) {
            sender.sendMessage(ChatColor.GOLD + "Unable to send message: Discord channel ID not set");
            
            return false;
        }
        
        // build message string
        String msg = "";
        
        for (int i = 0; i < args.length; i++) {
            msg += args[i];
            
            if (i != args.length - 1) {
                msg += " ";
            }
        }
        
        // send message
        @Nullable
        JDA bot = DiscordMinecraftAlertsPlugin.getDiscordBot();
        
        if (bot == null) {
            sender.sendMessage(ChatColor.GOLD + "Bot is null...");
            
            return true;
        }
        
        bot.getGuildById(serverID).getTextChannelById(channelID).sendMessage("**" + sender.getName() + ":** " + msg).queue();
        
        return true;
    }
}
