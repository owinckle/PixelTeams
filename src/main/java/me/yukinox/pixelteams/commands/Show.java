package me.yukinox.pixelteams.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.yukinox.pixelteams.PixelTeams;

import java.util.List;
import java.util.Map;

public class Show {
    private PixelTeams plugin;

    public Show(PixelTeams plugin) {
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

        String owner = plugin.teamsConfig.getString(teamName + ".owner");
        StringBuilder membersString = new StringBuilder(
                ChatColor.GREEN + plugin.config.getString("show.title").replace("{team}", teamName));
        membersString.append(ChatColor.RED + owner + " ");
        for (String member : plugin.teams.get(teamName)) {
            if (!member.equals(owner)) {
                membersString.append(ChatColor.WHITE + member + " ");
            }
        }
        player.sendMessage(membersString.toString());
        return true;
    }
}
