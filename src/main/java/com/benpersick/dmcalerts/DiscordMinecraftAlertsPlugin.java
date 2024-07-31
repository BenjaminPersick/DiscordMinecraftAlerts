package com.benpersick.dmcalerts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.util.EnumSet;
import java.util.prefs.*;
import javax.annotation.Nullable;

public class DiscordMinecraftAlertsPlugin extends JavaPlugin {
	public static final String DISCORD_TOKEN_KEY = "discord_token";
	public static final String DISCORD_SERVER_ID_KEY = "discord_server_id";
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
        this.getCommand("serverid").setExecutor(new CommandServerID());
        this.getCommand("channelid").setExecutor(new CommandChannelID());
        this.getCommand("discord").setExecutor(new CommandDiscord());
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
        	getServer().broadcastMessage(ChatColor.GOLD + "Discord bot token unset! Use /bottoken <token> to set it.");
        	
        	return false;
        }
        
        // get number of players and player limit
        int numOnline = getServer().getOnlinePlayers().size();
        int playerLimit = getServer().getMaxPlayers();
        
        discordBot = JDABuilder.createDefault(discordToken, EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                               .addEventListeners(new DiscordListener())
                               .setActivity(Activity.customStatus(numOnline + " / " + playerLimit + " players online"))
				   			   .build();
        
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
    
    /**
     * Updates the discord bot's status to display the current player
     * count
     */
    public static void updateBotStatus() {
        if (discordBot != null) {
            // get number of players and player limit
            int numOnline = Bukkit.getServer().getOnlinePlayers().size();
            int playerLimit = Bukkit.getServer().getMaxPlayers();
            
            discordBot.getPresence().setActivity(Activity.customStatus(numOnline + " / " + playerLimit + " players online"));
        }
    }
}
