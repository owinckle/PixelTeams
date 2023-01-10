package me.yukinox.pixelteams.commands;

import me.yukinox.pixelteams.PixelTeams;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {
    private PixelTeams plugin;

    public TeamCommand(PixelTeams plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                sendHelpMessage(player);
                return true;
            }

            switch (args[0].toLowerCase()) {
                default:
                    sendHelpMessage(player);
                    return true;
                case "create":
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + "Usage: /team create <name>");
                        return false;
                    }
                    return plugin.createTeam(args[1], player);
                case "disband":
                    return plugin.disbandTeam(player);
                case "show":
                    return plugin.showTeam(player);
                case "invite":
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + "Usage: /team invite <player>");
                        return false;
                    }
                    return plugin.invite(player, args[1]);
                case "join":
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + "Usage: /team join <name>");
                        return false;
                    }
                    return plugin.join(player, args[1]);
                case "leave":
                    return plugin.leave(player);
                case "kick":
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + "Usage: /team kick <player>");
                        return false;
                    }
                    return plugin.kick(player, args[1]);
            }
        }

        return true;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage(ChatColor.GOLD + "Team commands:");
        player.sendMessage(ChatColor.GOLD + "/team create <name> - Creates a new team with the given name.");
        player.sendMessage(ChatColor.GOLD + "/team invite <player> - Invites the given player to your team.");
        player.sendMessage(ChatColor.GOLD + "/team kick <player> - Kicks the given player from your team.");
        player.sendMessage(ChatColor.GOLD + "/team join <team> - Joins the given team.");
        player.sendMessage(ChatColor.GOLD + "/team leave - Leaves your current team.");
        player.sendMessage(ChatColor.GOLD + "/team disband - Disbands your current team.");
        player.sendMessage(ChatColor.GOLD + "/team show - Shows all the members of your current team.");
    }
}
