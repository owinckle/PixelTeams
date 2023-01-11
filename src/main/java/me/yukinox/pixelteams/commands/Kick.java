package me.yukinox.pixelteams.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.yukinox.pixelteams.PixelTeams;

import java.util.List;
import java.util.Map;

public class Kick {
    private PixelTeams plugin;

    public Kick(PixelTeams plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player, String target) {
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

        if (!player.getName().equals(owner)) {
            player.sendMessage(ChatColor.RED + plugin.config.getString("notOwner"));
            return false;
        }

        if (plugin.teams.get(teamName).contains(target)) {
            plugin.teams.get(teamName).remove(target);
            plugin.teamsConfig.set(teamName + ".members", plugin.teams.get(teamName));
            plugin.saveTeams();

            Player targetPlayer = Bukkit.getPlayer(target);
            if (targetPlayer != null) {
                targetPlayer.sendMessage(
                        ChatColor.RED + plugin.config.getString("kick.kickMessage").replace("{team}", teamName));
            }

            for (String member : plugin.teams.get(teamName)) {
                Player memberPlayer = Bukkit.getPlayer(member);
                if (memberPlayer != null) {
                    memberPlayer.sendMessage(
                            ChatColor.RED + plugin.config.getString("kick.teamMessage").replace("{player}", target));
                }
            }
            return true;
        }

        player.sendMessage(ChatColor.RED + plugin.config.getString("playerNotMember").replace("{player}", target));
        return false;
    }
}
