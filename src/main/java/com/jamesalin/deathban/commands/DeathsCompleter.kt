package com.jamesalin.deathban.commands

import com.jamesalin.deathban.Deathban
import com.jamesalin.deathban.getUUID
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class DeathsCompleter(private val plugin: Deathban) : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String> {
        val completions: MutableList<String> = mutableListOf()

        if (args?.size == 1) {
            if (sender !is Player) return completions
            val deaths = plugin.storage.lives[sender.name.getUUID()]?.deaths
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