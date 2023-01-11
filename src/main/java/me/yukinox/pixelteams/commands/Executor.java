package me.yukinox.pixelteams.commands;

import me.yukinox.pixelteams.PixelTeams;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Executor implements CommandExecutor {
    PixelTeams plugin;

    public Executor(PixelTeams plugin) {
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
                        player.sendMessage(ChatColor.RED + plugin.config.getString("create.usage"));
                        return false;
                    }
                    Create create = new Create(plugin);
                    return create.execute(player, args[1]);
                case "disband":
                    Disband disband = new Disband(plugin);
                    return disband.execute(player);
                case "show":
                    Show show = new Show(plugin);
                    return show.execute(player);
                case "invite":
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + plugin.config.getString("invite.usage"));
                        return false;
                    }
                    Invite invite = new Invite(plugin);
                    return invite.execute(player, args[1]);
                case "join":
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + plugin.config.getString("join.usage"));
                        return false;
                    }
                    Join join = new Join(plugin);
                    return join.execute(player, args[1]);
                case "leave":
                    Leave leave = new Leave(plugin);
                    return leave.execute(player);
                case "kick":
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + plugin.config.getString("kick.usage"));
                        return false;
                    }
                    Kick kick = new Kick(plugin);
                    return kick.execute(player, args[1]);
                case "owner":
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + plugin.config.getString("owner.usage"));
                        return false;
                    }
                    Owner owner = new Owner(plugin);
                    return owner.execute(player, args[1]);
                case "chat":
                    ToggleChat toggleChat = new ToggleChat(plugin);
                    return toggleChat.execute(player);
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
        player.sendMessage(ChatColor.GOLD + "/team owner - Transfer the ownership of the team.");
        player.sendMessage(ChatColor.GOLD + "/team chat - Switch between team and public chat.");
    }
}
