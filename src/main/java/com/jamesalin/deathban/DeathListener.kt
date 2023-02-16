import com.jamesalin.deathban.Deathban
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
        val uuid = player.uniqueId.toString()

        val cost =
            if (killer != null && killer.type == EntityType.PLAYER) plugin.conf.playerDeathCost else plugin.conf.nonPlayerDeathCost

        val lives = plugin.config.getConfigurationSection("lives")!!
        val l = (if (lives.contains(uuid)) lives.getInt(uuid) else plugin.conf.livesLimit) - cost

        if (l <= 0) {
            Punishment.create(
                player.name,
                player.name.getUUID(),
                "reason",
                "admin",
                PunishmentType.TEMP_BAN,
                -1,
                plugin.conf.banLength,
                true
            )
        }

        lives.set(uuid, l)
        plugin.config.set("lives", lives)
        plugin.conf.save()
    }
}
