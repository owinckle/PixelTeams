package me.yukinox.pixelteams.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.yukinox.pixelteams.PixelTeams;

import java.util.List;
import java.util.Map;

public class Disband {
    private PixelTeams plugin;

    public Disband(PixelTeams plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player) {
        String teamName = null;
        String owner = null;

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

        if (!player.getName().equals(owner)) {
            player.sendMessage(ChatColor.RED + plugin.config.getString("notOwner"));
            return false;
        }

        for (String member : plugin.teams.get(teamName)) {
            Player memberPlayer = Bukkit.getPlayer(member);
            if (memberPlayer != null) {
                memberPlayer.sendMessage(ChatColor.RED + plugin.config.getString("disband.success"));
            }
        }
        plugin.teams.remove(teamName);
        plugin.teamsConfig.set(teamName, null);
        plugin.saveTeams();

        return true;
    }
}
