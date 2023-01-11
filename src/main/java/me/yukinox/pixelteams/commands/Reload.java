package me.yukinox.pixelteams.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.yukinox.pixelteams.PixelTeams;

public class Reload {
	PixelTeams plugin;

	public Reload(PixelTeams plugin) {
		this.plugin = plugin;
	}

	public boolean execute(Player player) {
		if (player.hasPermission("pixelteams.admin") || player.isOp()) {
			player.sendMessage(ChatColor.RED + "[PIXEL TEAMS] Reloading...");
			plugin.reloadConfig();
			plugin.config = plugin.getConfig();
			player.sendMessage(ChatColor.GREEN + "[PIXEL TEAMS] Reload complete.");
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "[PIXEL TEAMS] You do not have the permissions to use this command.");
			return false;
		}
	}
}
