package com.jamesalin.deathban

import DeathListener
import me.leoko.advancedban.manager.UUIDManager
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

fun String.toComponent() = LegacyComponentSerializer.legacyAmpersand().deserialize(this)
fun String.getUUID(): String = UUIDManager.get().getUUID(this)
fun currentTimeSeconds(): Long = System.currentTimeMillis() / 1000

class Deathban : JavaPlugin() {
    val conf = Config(this)
    val storage = Storage(this)

    override fun onEnable() {
        logger.info("Starting DeathBan")

        // Listeners
        server.pluginManager.registerEvents(DeathListener(this), this)
        conf.onEnable()
        storage.onEnable()

        object : BukkitRunnable() {
            override fun run() {
                val currentTime = currentTimeSeconds()

                // Reset lives and lastUpdated
                if (currentTime - storage.lastUpdated > conf.updateInterval * 3600) {
                    for (key in storage.lives.keys) storage.lives[key]!!.lives = conf.livesLimit
                    storage.lastUpdated = currentTime
                    storage.save()
                }
            }
        }.runTaskTimer(this, 0, 20)
    }

    override fun onDisable() {
        conf.save()
    }
}
