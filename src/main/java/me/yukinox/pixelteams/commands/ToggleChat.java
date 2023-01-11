package me.yukinox.pixelteams.commands;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.yukinox.pixelteams.PixelTeams;

public class ToggleChat {
	PixelTeams plugin;

	public ToggleChat(PixelTeams plugin) {
		this.plugin = plugin;
	}

	public boolean execute(Player player) {
		String teamName = null;

		for (Map.Entry<String, List<String>> entry : plugin.teams.entrySet()) {
			if (entry.getValue().contains(player.getName())) {
				teamName = entry.getKey();
				break;
			}
		}

		if (teamName == null) {
			player.sendMessage(ChatColor.RED + plugin.config.getString("notInTeam"));
			return false;
		}

		List<String> chatList = plugin.teamsConfig.getStringList(teamName + ".chat");
		if (chatList.contains(player.getName())) {
			chatList.remove(player.getName());
			player.sendMessage(ChatColor.GREEN + plugin.config.getString("chat.disabled"));
		} else {
			chatList.add(player.getName());
			player.sendMessage(ChatColor.GREEN + plugin.config.getString("chat.enabled"));
		}
		plugin.teamsConfig.set(teamName + ".chat", chatList);
		plugin.saveTeams();

		return true;
	}
}
