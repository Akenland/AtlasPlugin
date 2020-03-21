package com.kylenanakdewa.atlasmap;

import java.util.Arrays;
import java.util.List;

import com.kylenanakdewa.atlasmap.serializers.world.worlddata.ChunkData;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

/**
 * Commands for the Atlas plugin.
 *
 * @author Kyle Nanakdewa
 */
final class AtlasCommands implements TabExecutor {

    private final AtlasPlugin plugin;

    AtlasCommands(AtlasPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Version command
        if (args.length == 0 || args[0].equalsIgnoreCase("version")) {
            sender.sendMessage("Atlas " + plugin.getDescription().getVersion() + " by Kyle Nanakdewa");
            sender.sendMessage("- Interactive Map");
            sender.sendMessage("- Website: https://plugins.akenland.com");
            return true;
        }

        // Reload command
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage("Atlas reloaded.");
            return true;
        }

        // Chunk send command
        if (args.length == 1 && args[0].equalsIgnoreCase("sendchunk") && sender instanceof Player) {
            Player player = (Player) sender;
            plugin.getWsClient().sendMessage(new ChunkData(player.getLocation().getChunk()).toJson());
            sender.sendMessage("Chunk update sent.");
            return true;
        }

        // Invalid command
        sender.sendMessage("Invalid arguments.");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Main command - return each sub-command
        if (args.length == 1)
            return Arrays.asList("version", "reload", "sendchunk");
        // Otherwise return nothing
        return Arrays.asList("");
    }

}