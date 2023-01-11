package me.yukinox.pixelteams.listeners;

import me.yukinox.pixelteams.PixelTeams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FriendlyFireListener implements Listener {
    private PixelTeams plugin;

    public FriendlyFireListener(PixelTeams plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager() == null || event.getEntity() == null) {
            return;
        }

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        Player defender = (Player) event.getEntity();

        String attackerTeam = plugin.getTeam(attacker);
        String defenderTeam = plugin.getTeam(defender);

        if (attackerTeam != null && attackerTeam.equals(defenderTeam)) {
            event.setCancelled(true);
            if (plugin.config.getBoolean("friendlyFire.messageEnabled")) {
                attacker.sendMessage(ChatColor.RED + "Friendly fire is disabled");
            }
        }

    }
}
