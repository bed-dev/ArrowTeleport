package codes.bed;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ArrowTeleport extends JavaPlugin implements Listener {
    private static final ConsoleCommandSender LOGGER = Bukkit.getConsoleSender();
    FileConfiguration config = getConfig();

    private final String PERMISSION = config.getString("permission", "arrow.teleport");

    private String projectileType;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Check if "projectile-type" exists and set it; otherwise, default to null
        projectileType = config.contains("projectile-type") ? config.getString("projectile-type") : null;

        LOGGER.sendMessage("§aArrowTeleport v1.1 enabled");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        LOGGER.sendMessage("§cArrowTeleport v1.1 disabled");
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();

        if (isProjectile(entity)) {
            if (entity.getShooter() instanceof Player) {
                Player player = (Player) entity.getShooter();
                if (player.hasPermission(PERMISSION)) {
                    Location projectileLocation = entity.getLocation();
                    projectileLocation.setYaw(player.getLocation().getYaw());
                    projectileLocation.setPitch(player.getLocation().getPitch());
                    player.teleport(projectileLocation);
                }
            }
        }
    }

    private boolean isProjectile(Entity entity) {
        if (projectileType == null) {
            return entity instanceof Arrow || entity instanceof SpectralArrow || entity instanceof TippedArrow;
        }

        if (projectileType.equalsIgnoreCase("SpectralArrow") && entity instanceof SpectralArrow) {
            return true;
        } else if (projectileType.equalsIgnoreCase("Arrow") && entity instanceof Arrow) {
            return true;
        } else if (projectileType.equalsIgnoreCase("TippedArrow") && entity instanceof TippedArrow) {
            return true;
        }
        return false;
    }
}
