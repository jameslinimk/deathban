package com.jamesalin.deathban.commands

import com.jamesalin.deathban.Deathban
import com.jamesalin.deathban.getUUID
import com.jamesalin.deathban.toComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Lives(private val plugin: Deathban) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val lives = plugin.storage.lives[sender.name.getUUID()]?.lives

        if (lives == null) {
            sender.sendMessage("&cYou have no lives!".toComponent())
            return false
        }

        sender.sendMessage(
            """
            &2You have $lives/${plugin.conf.livesLimit} lives!
            &7-${plugin.conf.playerDeathCost} for death by player, -${plugin.conf.nonPlayerDeathCost} for other death
            """.trimIndent().toComponent()
        )
        return false
    }
}
