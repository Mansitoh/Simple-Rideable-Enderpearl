package dev.mansitoh.skilled.rideable;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class enderpearl extends JavaPlugin implements Listener {

    private final Set<UUID> allowedDismounts = new HashSet<>();
    private boolean checkSuffocationDamage;
    private boolean checkdismount;
    private boolean checkThrowingWhileRiding;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    //ENDER PEARL RIDE
    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player) || event.getEntityType() != EntityType.ENDER_PEARL) {
            return;
        }
        Player p = (Player) event.getEntity().getShooter();
        if(!p.hasPermission("mansitoh.rideableenderpearl.ride")) return;
        if(checkThrowingWhileRiding && p.isInsideVehicle() && p.getVehicle().getType() == EntityType.ENDER_PEARL && !p.getVehicle().isDead()) {
            event.setCancelled(true);
            return;
        }
        event.getEntity().addPassenger(p);
    }

    //REMOVE SUFFOCATION DAMAGE
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(checkSuffocationDamage && event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION &&
                event.getEntity().isInsideVehicle() && event.getEntity().getVehicle().getType() == EntityType.ENDER_PEARL) {
            event.setCancelled(true);
        }
    }

    //CANCEL TELEPORT ON COLLISION
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(!checkdismount || event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL)return;
        allowedDismounts.add(event.getPlayer().getUniqueId());
    }

}
