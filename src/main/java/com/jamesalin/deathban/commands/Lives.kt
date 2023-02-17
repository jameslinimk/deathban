package com.jamesalin.deathban.commands

import com.jamesalin.deathban.Deathban
import com.jamesalin.deathban.toComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class Lives(private val plugin: Deathban) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val (uuid, name) = if (args?.size == 1) {
            val player = Bukkit.getPlayer(args[0]) ?: run {
                val offlinePlayer = Bukkit.getOfflinePlayer(args[0])
                if (offlinePlayer.hasPlayedBefore()) offlinePlayer else null
            }

            if (player == null) {
                sender.sendMessage("&cPlayer ${args[0]} not found!".toComponent())
                return false
            }

            val visibility = plugin.storage.visibility["${player.uniqueId}"] ?: true
            if (!visibility && !sender.hasPermission("deathban.seethrough")) {
                sender.sendMessage("&cPlayer ${args[0]}'s lives are not visible!".toComponent())
                return false
            }

            Pair(player.uniqueId, player.name ?: "Unknown")
        } else {
            if (sender !is Player) {
                sender.sendMessage("&cThe console can't have any lives!".toComponent())
                return false
            }

            Pair(sender.uniqueId, "You")
        }

        val lives = plugin.storage.lives["$uuid"]?.lives

        if (lives == null) {
            sender.sendMessage("&c$name have no lives!".toComponent())
            return false
        }

        sender.sendMessage(
            """
            &2$name have $lives/${plugin.conf.livesLimit} lives!
            &7-${plugin.conf.playerDeathCost} for death by player, -${plugin.conf.nonPlayerDeathCost} for other death
            """.trimIndent().toComponent()
        )
        return false
    }
}

class LivesCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String> {
        val completions = mutableListOf<String>()
        if (args?.size == 1) {
            val players = Bukkit.getServer().onlinePlayers.map { it.name }.toTypedArray().toMutableList()
            StringUtil.copyPartialMatches(args[0], players, completions)
        }

        return completions
    }
}
