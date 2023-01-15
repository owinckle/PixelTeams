package me.yukinox.pixelteams.listeners;

import me.yukinox.pixelteams.PixelTeams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class FriendlyFireListener implements Listener {
    private PixelTeams plugin;

    public FriendlyFireListener(PixelTeams plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player attacker = null;

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (!(arrow.getShooter() instanceof Player)) {
                return;
            } else {
                attacker = (Player) arrow.getShooter();
            }
        } else if (event.getDamager() instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion) event.getDamager();
            if (!(potion.getShooter() instanceof Player)) {
                return;
            } else {
                attacker = (Player) potion.getShooter();
            }
        }

        if (attacker == victim || attacker == null) {
            return;
        }

        String attackerTeam = plugin.getTeam(attacker);
        String victimTeam = plugin.getTeam(victim);
        if (attackerTeam != null && attackerTeam.equals(victimTeam)) {
            event.setCancelled(true);
            if (plugin.config.getBoolean("friendlyFire.messageEnabled")) {
                attacker.sendMessage(ChatColor.RED + "Friendly fire is disabled");
            }
        }
    }
}
