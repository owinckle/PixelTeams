package me.yukinox.pixelteams.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.yukinox.pixelteams.PixelTeams;

import java.util.stream.Collectors;

public class Join {
    private PixelTeams plugin;

    public Join(PixelTeams plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player, String teamName) {
        if (plugin.teams.containsKey(teamName)) {
            if (plugin.teamsConfig.getStringList(teamName + ".invites").contains(player.getName())) {
                plugin.teams.get(teamName).add(player.getName());
                plugin.teamsConfig.set(teamName + ".members", plugin.teams.get(teamName));
                plugin.saveTeams();
                plugin.teamsConfig.set(teamName + ".invites", plugin.teamsConfig.getStringList(teamName + ".invites")
                        .stream().filter(e -> !e.equals(player.getName())).collect(Collectors.toList()));
                plugin.saveTeams();
                player.sendMessage(
                        ChatColor.GREEN + plugin.config.getString("join.success").replace("{team}", teamName));
                return true;
            } else {
                player.sendMessage(ChatColor.RED + plugin.config.getString("join.notInvited"));
                return false;
            }
        } else {
            player.sendMessage(ChatColor.RED + plugin.config.getString("teamNotExist"));
            return false;
        }
    }
}
