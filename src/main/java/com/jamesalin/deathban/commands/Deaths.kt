package com.jamesalin.deathban.commands

import com.jamesalin.deathban.Deathban
import com.jamesalin.deathban.format
import com.jamesalin.deathban.secondsToDate
import com.jamesalin.deathban.toComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class DeathsCommand(private val plugin: Deathban) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage("&cThe console can't have any deaths!".toComponent())
            return false
        }

        val deaths = plugin.storage.lives["${sender.uniqueId}"]?.deaths
        if (deaths.isNullOrEmpty()) {
            sender.sendMessage("&cYou have no deaths. Congrats!".toComponent())
            return false
        }

        val chunks = deaths.chunked(5)

        val argPage = args?.get(0)?.toIntOrNull()
        val page = (argPage ?: 1) - 1

        if (page < 0 || page >= chunks.size) {
            sender.sendMessage("&c${argPage} is not a valid page! Select page 1-${chunks.size}".toComponent())
            return false
        }

        sender.sendMessage("&2Deaths for ${sender.name}")
        for ((i, death) in chunks[page].withIndex()) {
            sender.sendMessage(
                """
                &aDeath #${i} &7&o(${secondsToDate(death.time).format()}):
                 &8- Cause: &7${death.deathMessage}
                 &8- Lives lost: &7${death.livesLost}
                """.trimIndent()
            )
        }
        sender.sendMessage("&2Page ${page + 1}/${chunks.size}")
        return false
    }
}

class DeathsCompleter(private val plugin: Deathban) : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        leabel: String,
        args: Array<out String>?
    ): MutableList<String> {
        val completions: MutableList<String> = mutableListOf()

        if (args?.size == 1) {
            if (sender !is Player) return completions
            val deaths = plugin.storage.lives["${sender.uniqueId}"]?.deaths
            if (deaths.isNullOrEmpty()) return completions

            val numArray = mutableListOf<String>()
            for (i in 1..deaths.size.floorDiv(5)) {
                numArray.add("$i")
            }

            StringUtil.copyPartialMatches(args[0], numArray, completions)
        }

        return completions
    }
}
