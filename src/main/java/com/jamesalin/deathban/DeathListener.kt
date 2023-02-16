import com.jamesalin.deathban.Deathban
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class DeathListener(private val plugin: Deathban) : Listener {
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val killer = event.entity.killer

        val cost = if (killer != null && killer.type == EntityType.PLAYER) {
            plugin.playerDeathCost
        } else {
            plugin.nonPlayerDeathCost
        }

        val lives = plugin.config.getConfigurationSection("lives")!!

        val uuid = event.player.uniqueId.toString()
        val l = (if (lives.contains(uuid)) lives.getInt(uuid) else plugin.livesLimit) - cost

        lives.set(uuid, l)
        plugin.config.set("lives", lives)
        plugin.save()
    }
}
