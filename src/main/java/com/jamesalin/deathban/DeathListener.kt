import com.jamesalin.deathban.Deathban
import com.jamesalin.deathban.LivesInfo
import com.jamesalin.deathban.currentTimeSeconds
import com.jamesalin.deathban.getUUID
import me.leoko.advancedban.utils.Punishment
import me.leoko.advancedban.utils.PunishmentType
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class DeathListener(private val plugin: Deathban) : Listener {
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.player.hasPermission("deathban.bypass")) return

        val killer = event.entity.killer
        val player = event.player
        val uuid = player.name.getUUID()

        val cost =
            if (killer != null && killer.type == EntityType.PLAYER) plugin.conf.playerDeathCost else plugin.conf.nonPlayerDeathCost

        // Calculate new lives
        if (plugin.storage.lives[uuid] == null) plugin.storage.lives[uuid] =
            LivesInfo(plugin.conf.livesLimit, mutableListOf())
        val newLives = plugin.storage.lives[uuid]!!.lives - cost

        // Banning the player
        if (newLives <= 0) {
            Punishment.create(
                player.name,
                uuid,
                plugin.conf.banReason,
                "admin",
                PunishmentType.TEMP_BAN,
                -1,
                "${plugin.conf.banLength}h",
                true
            )

            plugin.storage.punishments.add(
                com.jamesalin.deathban.Punishment(
                    uuid,
                    cost,
                    currentTimeSeconds() + plugin.conf.updateInterval * 3600
                )
            )
        }

        plugin.storage.lives[uuid]!!.lives = newLives
        plugin.storage.save()
    }
}
