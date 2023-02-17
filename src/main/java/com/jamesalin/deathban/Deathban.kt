package com.jamesalin.deathban

import DeathListener
import com.jamesalin.deathban.commands.*
import me.leoko.advancedban.manager.UUIDManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.user.User
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.text.SimpleDateFormat
import java.util.*


// Utility functions
fun String.toComponent() = legacyAmpersand().deserialize(this)
fun Component.toStr() = legacyAmpersand().serialize(this)

fun String.getAdvancedUUID(): String = UUIDManager.get().getUUID(this)

fun currentTimeSeconds() = System.currentTimeMillis() / 1000
fun secondsToDate(timeSeconds: Long) = Date(timeSeconds * 1000)

val formatter = SimpleDateFormat("MMM dd, hh:mmaa")
fun Date.format(): String = formatter.format(this)

fun getPlayer(name: String): User? {
    val api = LuckPermsProvider.get()
    return api.userManager.getUser(name)
}

class Deathban : JavaPlugin() {
    val conf = Config(this)
    val storage = Storage(this)

    override fun onEnable() {
        logger.info("----------------------------------")
        logger.info("Starting DeathBan by James Linimik")
        logger.info("----------------------------------")

        // Listeners
        server.pluginManager.registerEvents(DeathListener(this), this)
        getCommand("deaths")!!.setExecutor(DeathsCommand(this))
        getCommand("deaths")!!.tabCompleter = DeathsCompleter(this)

        getCommand("lives")!!.setExecutor(Lives(this))
        getCommand("lives")!!.tabCompleter = LivesCompleter()

        getCommand("editlives")!!.setExecutor(Editlives(this))
        getCommand("editlives")!!.tabCompleter = EditlivesCompelter()

        getCommand("showlives")!!.setExecutor(ShowLives(this))
        getCommand("hidelives")!!.setExecutor(Hidelives(this))

        // onEnable
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
        storage.save()
    }
}
