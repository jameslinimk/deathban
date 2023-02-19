package com.jamesalin.deathban

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

        val player = event.player
        val killer = player.killer
        val uuid = "${player.uniqueId}"

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
                player.name.getAdvancedUUID(),
                plugin.conf.banReason,
                "admin",
                PunishmentType.TEMP_BAN,
                -1,
                "${plugin.conf.banLength}h",
                true
            )

            plugin.storage.punishments.add(
                Punishment(
                    uuid,
                    cost,
                    currentTimeSeconds() + plugin.conf.updateInterval * 3600
                )
            )
        }

        // Saving the death
        plugin.storage.lives[uuid]!!.deaths.add(
            Death(
                event.deathMessage()?.toStr() ?: "NONE",
                currentTimeSeconds(),
                cost
            )
        )

        plugin.storage.lives[uuid]!!.lives = newLives
        plugin.storage.save()
    }
}
