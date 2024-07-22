package com.benpersick.dmcalerts;

import java.util.prefs.Preferences;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandServerID implements CommandExecutor {
    private Preferences prefs;
    
    public CommandServerID() {
        prefs = Preferences.userNodeForPackage(CommandServerID.class);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // ensure correct number of args
        if (args.length != 1) {
            sender.sendMessage(Color.RED + "Incorrect number of arguments");
            
            return false;
        }
        
        // attempt to convert provided id to long
        long serverID;
        
        try {
            serverID = Long.parseLong(args[0]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Please enter a valid long as the Server ID.");
            
            return false;
        }
        
        // store server ID
        prefs.putLong(DiscordMinecraftAlertsPlugin.DISCORD_SERVER_ID_KEY, serverID);
        
        sender.sendMessage(Color.GREEN + "Server ID successfully set.");
        
        return true;
    }
}
