package me.yukinox.pixelteams.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.yukinox.pixelteams.PixelTeams;

public class Create {
    private PixelTeams plugin;

    public Create(PixelTeams plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player creator, String name) {
        for (Map.Entry<String, List<String>> entry : plugin.teams.entrySet()) {
            if (entry.getValue().contains(creator.getName())) {
                creator.sendMessage(ChatColor.RED + plugin.config.getString("alreadyHaveTeam"));
                return false;
            }
        }
        if (plugin.teams.containsKey(name)) {
            creator.sendMessage(ChatColor.RED + plugin.config.getString("create.alreadyExists"));
            return false;
        }

        List<String> members = new ArrayList<String>();
        members.add(creator.getName());
        plugin.teams.put(name, members);
        plugin.teamsConfig.set(name + ".members", members);
        plugin.teamsConfig.set(name + ".owner", creator.getName());
        plugin.saveTeams();

        creator.sendMessage(ChatColor.GREEN + plugin.config.getString("create.success").replace("{team}", name));
        return true;
    }
}
