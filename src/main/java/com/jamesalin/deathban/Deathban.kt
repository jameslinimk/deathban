package com.jamesalin.deathban

import DeathListener
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

fun String.toComponent() = LegacyComponentSerializer.legacyAmpersand().deserialize(this)

class Deathban : JavaPlugin() {
    fun save() {
        this.config.options().copyDefaults(true)
        saveConfig()
    }

    var interval = 0
    var lastUpdated: Long = 0
    var livesLimit = 0

    var banLength = ""
    var playerDeathCost = 0
    var nonPlayerDeathCost = 0

    override fun onEnable() {
        logger.info("Starting DeathBan")

        // Listeners
        server.pluginManager.registerEvents(DeathListener(this), this)

        // Config stuff
        this.saveDefaultConfig()

        // Config default values
        config.addDefault("livesLimit", "4")
        config.addDefault("updateInterval", "24")
        config.addDefault("lastUpdated", "0")
        config.addDefault("lives", "{}")
        config.addDefault("playerDeathCost ","2")
        config.addDefault("nonPlayerDeathCost", "1")

        // Config values
        interval = config.getInt("updateInterval")
        lastUpdated = config.getLong("lastUpdated")
        livesLimit = config.getInt("livesLimit")
        banLength = config.getString("banLength")!!
        playerDeathCost = config.getInt("playerDeathCost")
        nonPlayerDeathCost = config.getInt("nonPlayerDeathCost")

        object : BukkitRunnable() {
            override fun run() {
                val currentTime = System.currentTimeMillis() / 1000

                // Reset lives and lastUpdated
                if (currentTime - lastUpdated > interval * 3600) {
                    val lives = config.getConfigurationSection("lives")!!
                    for (key in lives.getKeys(false)) lives.set(key, livesLimit)
                    config.set("lastUpdated", currentTime)
                    save()
                }
            }
        }.runTaskTimer(this, 0, 20)
    }

    override fun onDisable() {
        save()
    }
}
