package com.benpersick.dmcalerts;

import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;
import net.dv8tion.jda.api.*;
import java.util.prefs.*;
import javax.annotation.Nullable;

public class DiscordMinecraftAlertsPlugin extends JavaPlugin {
	public static final String DISCORD_TOKEN_KEY = "discord_token";
	public static final String DISCORD_CHANNEL_ID_KEY = "discord_channel_id";
	
	private Preferences prefs;
	private static JDA discordBot;
	
	@Override
    public void onEnable() {
        getLogger().info("Discord Minecraft Alerts starting up!");
        
        // initialize preferences
        prefs = Preferences.userNodeForPackage(DiscordMinecraftAlertsPlugin.class);
        
        // attempt to create discord bot
        initializeDiscordBot();
        
        // initialize Minecraft event listener
        getServer().getPluginManager().registerEvents(new MinecraftListener(), this);
        
        // register commands
        this.getCommand("bottoken").setExecutor(new CommandBotToken(this));
        this.getCommand("channelid").setExecutor(new CommandChannelID());
    }
	
	/**
	 * Attempts to initialize the JDA instance based on the currently saved bot token
	 * @return true if the JDA instance is successfully created, false otherwise
	 */
	public boolean initializeDiscordBot() {
		// attempt to get token
        String discordToken = prefs.get(DISCORD_TOKEN_KEY, "");
        
        // initialize JDA, if applicable
        if (discordToken.equals("")) {
        	getServer().broadcastMessage(Color.ORANGE + "Discord bot token unset! Use /bottoken <token> to set it.");
        	
        	return false;
        }
        
        discordBot = JDABuilder.createDefault(discordToken)
				   			   .build();
        
        // TODO add event listener
        
        return true;
	}
	
    @Override
    public void onDisable() {
        getLogger().info("Discord Minecraft Alerts disabled!");
    }
    
    @Nullable
    public static JDA getDiscordBot() {
    	return discordBot;
    }
}
