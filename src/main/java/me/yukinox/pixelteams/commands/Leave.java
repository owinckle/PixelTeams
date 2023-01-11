package me.yukinox.pixelteams.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.yukinox.pixelteams.PixelTeams;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Leave {
    private PixelTeams plugin;

    public Leave(PixelTeams plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player) {
        String owner = null;
        String teamName = null;

        for (Map.Entry<String, List<String>> entry : plugin.teams.entrySet()) {
            if (entry.getValue().contains(player.getName())) {
                teamName = entry.getKey();
                owner = plugin.teamsConfig.getString(teamName + ".owner");
                break;
            }
        }
        if (teamName == null) {
            player.sendMessage(ChatColor.RED + plugin.config.getString("notInTeam"));
            return false;
        }

        if (player.getName().equals(owner)) {
            player.sendMessage(ChatColor.RED + plugin.config.getString("notOwner"));
            return false;
        }

        plugin.teams.get(teamName).remove(player.getName());
        plugin.teamsConfig.set(teamName + ".members", plugin.teamsConfig.getStringList(teamName + ".members").stream()
                .filter(e -> !e.equals(player.getName())).collect(Collectors.toList()));
        plugin.saveTeams();
        player.sendMessage(ChatColor.RED + plugin.config.getString("leave.playerSuccess").replace("{team}", teamName));

        for (String member : plugin.teams.get(teamName)) {
            Player memberPlayer = Bukkit.getPlayer((member));
            if (memberPlayer != null) {
                memberPlayer.sendMessage(ChatColor.RED
                        + plugin.config.getString("leave.teamMessage").replace("{player}", player.getName()));
            }
        }
        return true;
    }
}
