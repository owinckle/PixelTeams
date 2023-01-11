package me.yukinox.pixelteams;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.yukinox.pixelteams.commands.Executor;
import me.yukinox.pixelteams.listeners.ChatListener;
import me.yukinox.pixelteams.listeners.FriendlyFireListener;

public final class PixelTeams extends JavaPlugin implements Listener {
    public Configuration config;
    public Map<String, List<String>> teams;
    public FileConfiguration teamsConfig;
    private File teamsFile;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        teams = new HashMap<String, List<String>>();
        teamsFile = new File(getDataFolder(), "teams.yml");
        teamsConfig = YamlConfiguration.loadConfiguration(teamsFile);

        if (!teamsFile.exists()) {
            teamsFile.getParentFile().mkdirs();
            saveResource("teams.yml", false);
        }

        loadTeams();

        // Events
        getServer().getPluginManager().registerEvents(new FriendlyFireListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        // Commands
        getCommand("team").setExecutor(new Executor(this));
    }

    @Override
    public void onDisable() {
        saveTeams();
    }

    public String getTeam(Player player) {
        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            if (entry.getValue().contains(player.getName())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void loadTeams() {
        teams.clear();
        Set<String> teamNames = teamsConfig.getKeys(false);
        for (String teamName : teamNames) {
            List<String> members = teamsConfig.getStringList(teamName + ".members");
            teams.put(teamName, members);
        }
    }

    public void saveTeams() {
        try {
            teamsConfig.save(teamsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}