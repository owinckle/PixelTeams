package me.yukinox.pixelteams;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.yukinox.pixelteams.commands.TeamCommand;

public final class PixelTeams extends JavaPlugin implements Listener {
    private Map<String, List<String>> teams;
    private File teamsFile;
    private FileConfiguration teamsConfig;
    @Override
    public void onEnable() {
        teams = new HashMap<String, List<String>>();
        teamsFile = new File(getDataFolder(), "teams.yml");
        teamsConfig = YamlConfiguration.loadConfiguration(teamsFile);

        if (!teamsFile.exists()) {
            teamsFile.getParentFile().mkdirs();
            saveResource("teams.yml", false);
        }

        loadTeams();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("team").setExecutor(new TeamCommand(this));
    }

    @Override
    public void onDisable() {
        saveTeams();
    }

    public boolean createTeam(String name, Player creator) {
        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            if (entry.getValue().contains(creator.getName())) {
                creator.sendMessage(ChatColor.RED + "You are already a member of another team. Please leave that team first.");
                return false;
            }
        }
        if (teams.containsKey(name)) {
            creator.sendMessage(ChatColor.RED + "A team with that name already exists");
            return false;
        }

        List<String> members = new ArrayList<String>();
        members.add(creator.getName());
        teams.put(name, members);
        teamsConfig.set(name + ".members", members);
        teamsConfig.set(name + ".owner", creator.getName());
        saveTeams();
        creator.sendMessage(ChatColor.GREEN + "Team " + name + " has been created!");
        return true;
    }

    public boolean invite(Player sender, String target) {
        String teamName = null;
        String owner = null;

        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            if (entry.getValue().contains(sender.getName())) {
                teamName = entry.getKey();
                owner = teamsConfig.getString(teamName + ".owner");
                break;
            }
        }
        if (teamName == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a team.");
            return false;
        }

        if (sender.getName() != (owner)) {
            sender.sendMessage(ChatColor.RED + "Only the owner can invite new members.");
        }

        if (teams.get(teamName).contains(target)) {
            sender.sendMessage(ChatColor.RED + "This player is already a member of your team.");
            return false;
        }
        if (teamsConfig.getStringList(teamName + ".invites").contains(target)) {
            sender.sendMessage(ChatColor.RED + "This player has already been invited.");
            return false;
        }
        List<String> invites = teamsConfig.getStringList(teamName + ".invites");
        invites.add(target);
        teamsConfig.set(teamName + ".invites", invites);
        saveTeams();

        sender.sendMessage(ChatColor.GREEN + target + "has been invited to your team.");

        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer != null) {
            targetPlayer.sendMessage(ChatColor.GREEN + "You've been invited to join the team " + teamName + ".");
        }
        return true;
    }

    public boolean join(Player player, String teamName) {
        if (teams.containsKey(teamName)) {
            if (teamsConfig.getStringList(teamName + ".invites").contains(player.getName())) {
                teams.get(teamName).add(player.getName());
                teamsConfig.set(teamName + ".members", teams.get(teamName).stream());
                teamsConfig.set(teamName + ".invites", teamsConfig.getStringList(teamName + ".invites").stream().filter(e -> !e.equals(player.getName())).collect(Collectors.toList()));
                saveTeams();
                player.sendMessage(ChatColor.GREEN + "You have joined the team " + teamName + ".");
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You have not been invited to this team.");
                return false;
            }
        } else{
            player.sendMessage(ChatColor.RED + "This team does not exist.");
            return false;
        }
    }

    public boolean leave(Player player) {
        String owner = null;
        String teamName = null;

        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            if (entry.getValue().contains(player.getName())) {
                teamName = entry.getKey();
                owner = teamsConfig.getString(teamName + ".owner");
                break;
            }
        }
        if (teamName == null) {
            player.sendMessage(ChatColor.RED + "You are not in any team.");
            return false;
        }

        if (player.getName() == owner) {
            player.sendMessage(ChatColor.RED + "You cannot leave this team as your are the owner.");
            return false;
        }

        teamsConfig.set(teamName + ".members", teamsConfig.getStringList(teamName + ".members").stream().filter(e -> !e.equals(player.getName())).collect(Collectors.toList()));
        saveTeams();
        player.sendMessage(ChatColor.RED + "You left " + teamName);

        for (String member : teams.get(teamName)) {
            Player memberPlayer = Bukkit.getPlayer((member));
            if (memberPlayer != null) {
                memberPlayer.sendMessage(ChatColor.RED + player.getName() + " left the team.");
            }
        }
        return true;
    }

    public boolean kick(Player player, String target) {
        String owner = null;

        String teamName = null;
        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            if (entry.getValue().contains(player.getName())) {
                teamName = entry.getKey();
                owner = teamsConfig.getString(teamName + ".owner");
                break;
            }
        }
        if (teamName == null) {
            player.sendMessage(ChatColor.RED + "You are not in any team.");
            return false;
        }

        if (player.getName() != owner) {
            player.sendMessage(ChatColor.RED + "Only the team owner can kick members the team.");
            return false;
        }

        for (String member : teams.get(teamName)) {
            Player memberPlayer = Bukkit.getPlayer(member);
            if (member == target) {
                teamsConfig.set(teamName + ".members", teamsConfig.getStringList(teamName + ".members").stream().filter(e -> !e.equals(member)).collect(Collectors.toList()));
                saveTeams();
                memberPlayer.sendMessage(ChatColor.RED + "You have been kicked from " + teamName + ".");
            } else {
                memberPlayer.sendMessage(ChatColor.RED + member + " has been kicked.");
            }
        }
        return true;
    }

    public boolean disbandTeam(Player player) {
        String teamName = null;
        String owner = null;

        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            if (entry.getValue().contains(player.getName())) {
                teamName = entry.getKey();
                owner = teamsConfig.getString(teamName + ".owner");
                break;
            }
        }

        if (teamName == null) {
            player.sendMessage(ChatColor.RED + "You are not in a team.");
            return false;
        }

        if (player.getName() != owner) {
            player.sendMessage(ChatColor.RED + "Only the team owner can disband the team.");
            return false;
        }

        for (String member : teams.get(teamName)) {
            Player memberPlayer = Bukkit.getPlayer(member);
            if (memberPlayer != null) {
                memberPlayer.sendMessage(ChatColor.RED + "Your team has been disbanded.");
            }
        }
        teams.remove(teamName);
        teamsConfig.set(teamName, null);
        saveTeams();

        return true;
    }

    public boolean showTeam(Player player) {
        String teamName = null;
        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            if (entry.getValue().contains(player.getName())) {
                teamName = entry.getKey();
                break;
            }
        }
        if (teamName == null) {
            player.sendMessage(ChatColor.RED + "You are not in any team.");
            return false;
        }

        String owner = teamsConfig.getString(teamName + ".owner");
        StringBuilder membersString = new StringBuilder(ChatColor.GREEN + "Members of team " + teamName + ": ");
        membersString.append(ChatColor.RED + owner + " ");
        for (String member : teams.get(teamName)) {
            if (member != owner) {
                membersString.append(ChatColor.WHITE + member + " ");
            }
        }
        player.sendMessage(membersString.toString());
        return true;
    }

    private void loadTeams() {
        for (String team: teamsConfig.getKeys(false)) {
            List<String> members = new ArrayList<String>();
            teamsConfig.getStringList(team + ".members").stream();
            teams.put(team, members);
        }
    }

    private void saveTeams() {
        try {
            teamsConfig.save(teamsFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}