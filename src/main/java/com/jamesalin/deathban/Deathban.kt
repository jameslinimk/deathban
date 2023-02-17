package com.jamesalin.deathban

import DeathListener
import com.jamesalin.deathban.commands.DeathsCommand
import com.jamesalin.deathban.commands.DeathsCompleter
import me.leoko.advancedban.manager.UUIDManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.text.SimpleDateFormat
import java.util.*

fun String.toComponent() = LegacyComponentSerializer.legacyAmpersand().deserialize(this)
fun Component.toStr() = LegacyComponentSerializer.legacyAmpersand().serialize(this)

fun String.getUUID(): String = UUIDManager.get().getUUID(this)
fun currentTimeSeconds(): Long = System.currentTimeMillis() / 1000
fun secondsToDate(timeSeconds: Long): Date = Date(timeSeconds * 1000)

val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
fun Date.format(): String = formatter.format(this)

class Deathban : JavaPlugin() {
    val conf = Config(this)
    val storage = Storage(this)

    override fun onEnable() {
        logger.info(
            """
            -----------------
            Starting DeathBan
            -----------------
            """.trimIndent()
        )

        // Listeners
        server.pluginManager.registerEvents(DeathListener(this), this)
        getCommand("deaths")!!.setExecutor(DeathsCommand(this))
        getCommand("deaths")!!.tabCompleter = DeathsCompleter(this)

        conf.onEnable()
        storage.onEnable()

        object : BukkitRunnable() {
            override fun run() {
                var save = false
                val currentTime = currentTimeSeconds()

                // Reset lives and lastUpdated
                if (currentTime - storage.lastUpdated > conf.updateInterval * 3600) {
                    storage.lives.values.forEach { it.lives = conf.livesLimit }
                    storage.lastUpdated = currentTime
                    save = true
                }

                // Lives lost cooldown
                storage.punishments.filter {
                    if (currentTime >= it.expires) {
                        storage.lives[it.uuid]!!.lives += it.livesLost
                        save = true
                        return@filter false
                    }
                    true
                }

                if (save) storage.save()
            }
        }.runTaskTimer(this, 0, 100)
    }

    override fun onDisable() {
        conf.save()
    }
}
