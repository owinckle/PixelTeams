package me.yukinox.pixelteams.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.yukinox.pixelteams.PixelTeams;

import java.util.List;
import java.util.Map;

public class Invite {
    private PixelTeams plugin;

    public Invite(PixelTeams plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player sender, String target) {
        String teamName = null;
        String owner = null;

        for (Map.Entry<String, List<String>> entry : plugin.teams.entrySet()) {
            if (entry.getValue().contains(sender.getName())) {
                teamName = entry.getKey();
                owner = plugin.teamsConfig.getString(teamName + ".owner");
                break;
            }
        }
        if (teamName == null) {
            sender.sendMessage(ChatColor.RED + plugin.config.getString("notInTeam"));
            return false;
        }

        if (!sender.getName().equals(owner)) {
            sender.sendMessage(ChatColor.RED + plugin.config.getString("notOwner"));
        }

        if (plugin.teams.get(teamName).contains(target)) {
            sender.sendMessage(ChatColor.RED + plugin.config.getString("invite.alreadyMember"));
            return false;
        }
        if (plugin.teamsConfig.getStringList(teamName + ".invites").contains(target)) {
            sender.sendMessage(ChatColor.RED + plugin.config.getString("invite.alreadyInvited"));
            return false;
        }

        List<String> invites = plugin.teamsConfig.getStringList(teamName + ".invites");
        invites.add(target);
        plugin.teamsConfig.set(teamName + ".invites", invites);
        plugin.saveTeams();

        sender.sendMessage(
                ChatColor.GREEN + plugin.config.getString("invite.senderSuccess").replace("{player}", target));

        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer != null) {
            targetPlayer.sendMessage(
                    ChatColor.GREEN + plugin.config.getString("invite.receiverSuccess").replace("{team}", teamName));
        }
        return true;
    }
}
