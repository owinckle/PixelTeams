package me.yukinox.pixelteams.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.yukinox.pixelteams.PixelTeams;

public class ChatListener implements Listener {
	private PixelTeams plugin;

	public ChatListener(PixelTeams plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String teamName = plugin.getTeam(player);
		String message = event.getMessage();
		String newFormat;
		String displayName = player.getDisplayName();

		if (teamName != null) {
			Boolean inTeamChat = false;
			List<String> teamChatList = plugin.teamsConfig.getStringList(teamName + ".chat");
			String messageFormat = plugin.config.getString("prefix");

			if (teamChatList.contains(player.getName())) {
				inTeamChat = true;
				message = ChatColor.GREEN + message;
			}

			newFormat = messageFormat.replace("{team}", teamName).replace("{player}", displayName)
					+ " §f\u00BB " + message;

			if (inTeamChat) {
				List<String> members = plugin.teamsConfig.getStringList(teamName + ".members");
				for (String member : members) {
					Player memberPlayer = plugin.getServer().getPlayer(member);
					if (memberPlayer != null) {
						newFormat = messageFormat.replace("{team}", teamName).replace("{player}",
								displayName)
								+ " §f\u00BB " + message;
						memberPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', newFormat));
					}
				}
				event.setCancelled(true);
				return;
			}
		} else {
			newFormat = displayName + " §f\u00BB " + message;
		}

		event.setFormat(ChatColor.translateAlternateColorCodes('&', newFormat));
	}
}
