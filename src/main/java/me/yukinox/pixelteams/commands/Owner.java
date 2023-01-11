package me.yukinox.pixelteams.commands;

import me.yukinox.pixelteams.PixelTeams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class Owner {
    PixelTeams plugin;

    public Owner(PixelTeams plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player sender, String target) {
        String currentOwner = null;
        String teamName = null;

        for (Map.Entry<String, List<String>> entry : plugin.teams.entrySet()) {
            if (entry.getValue().contains(sender.getName())) {
                teamName = entry.getKey();
                currentOwner = plugin.teamsConfig.getString(teamName + ".owner");
                break;
            }
        }

        if (teamName == null) {
            sender.sendMessage(ChatColor.RED + plugin.config.getString("notInTeam"));
            return false;
        }

        if (!currentOwner.equals(sender.getName())) {
            sender.sendMessage(ChatColor.RED + plugin.config.getString("notOwner"));
            return false;
        }

        String newOwner = target;
        if (!plugin.teams.get(teamName).contains(newOwner)) {
            sender.sendMessage(
                    ChatColor.RED + plugin.config.getString("playerNotMember").replace("{player}", newOwner));
            return false;
        }

        plugin.teamsConfig.set(teamName + ".owner", newOwner);
        plugin.saveTeams();
        sender.sendMessage(ChatColor.GREEN + plugin.config.getString("owner.success").replace("{player}", newOwner));

        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer != null) {
            targetPlayer
                    .sendMessage(ChatColor.GREEN + plugin.config.getString("owner.newOwnerSuccess").replace("{player}",
                            sender.getName()));
        }
        return true;
    }
}
