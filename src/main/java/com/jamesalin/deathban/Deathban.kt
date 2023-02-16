package com.jamesalin.deathban

import DeathListener
import kotlinx.serialization.Serializable
import me.leoko.advancedban.manager.UUIDManager
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

fun String.toComponent() = LegacyComponentSerializer.legacyAmpersand().deserialize(this)
fun String.getUUID(): String = UUIDManager.get().getUUID(this)

@Serializable
data class Death(val deathMessage: String, val time: Long, val livesLost: Int)

@Serializable
data class Lives(val lives: Int, val deaths: List<Death>)

@Serializable
data class Punishment(val uuid: String, val livesLost: Int, val expires: Long)

class Deathban : JavaPlugin() {
    val conf = Config(this)

    override fun onEnable() {
        logger.info("Starting DeathBan")

        // Listeners
        server.pluginManager.registerEvents(DeathListener(this), this)

        object : BukkitRunnable() {
            override fun run() {
                val currentTime = System.currentTimeMillis() / 1000

                // Reset lives and lastUpdated
                if (currentTime - conf.lastUpdated > conf.interval * 3600) {
                    val lives = config.getConfigurationSection("lives")!!
                    for (key in lives.getKeys(false)) lives.set(key, conf.livesLimit)
                    config.set("lastUpdated", currentTime)
                    conf.save()
                }
            }
        }.runTaskTimer(this, 0, 20)
    }

    override fun onDisable() {
        conf.save()
    }
}
