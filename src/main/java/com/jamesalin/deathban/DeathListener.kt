import com.jamesalin.deathban.Deathban
import me.leoko.advancedban.manager.TimeManager
import me.leoko.advancedban.manager.UUIDManager
import me.leoko.advancedban.utils.Punishment
import me.leoko.advancedban.utils.PunishmentType
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class DeathListener(private val plugin: Deathban) : Listener {
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val killer = event.entity.killer
        val player = event.player
        val uuid = player.uniqueId.toString()

        val cost = if (killer != null && killer.type == EntityType.PLAYER) plugin.playerDeathCost else plugin.nonPlayerDeathCost

        val lives = plugin.config.getConfigurationSection("lives")!!
        val l = (if (lives.contains(uuid)) lives.getInt(uuid) else plugin.livesLimit) - cost

        if (l <= 0) {
            Punishment(
                player.name,
                UUIDManager.get().getUUID(player.name),
                "reason",
                "admin",
                PunishmentType.TEMP_BAN,
                TimeManager.getTime(),
                -1,
                plugin.banLength,
                -1,
            ).create()
        }

        lives.set(uuid, l)
        plugin.config.set("lives", lives)
        plugin.save()
    }
}
