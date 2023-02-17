package com.jamesalin.deathban.commands

import com.jamesalin.deathban.Deathban
import com.jamesalin.deathban.getUUID
import com.jamesalin.deathban.toComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class Editlives(private val plugin: Deathban) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (!sender.hasPermission("deathban.editlives")) {
            sender.sendMessage("&cYou don't have permission to use this command!".toComponent())
            return false
        }

        if (args?.size != 2) {
            sender.sendMessage("&cInvalid usage! Use /editlives <player> <number>".toComponent())
            return false
        }

        val player = Bukkit.getPlayer(args[0])
        if (player == null) {
            sender.sendMessage("&cPlayer ${args[0]} not found!".toComponent())
            return false
        }

        val lives = args[1].toIntOrNull()
        if (lives == null) {
            sender.sendMessage("&c${args[1]} is not a valid number!".toComponent())
            return false
        }

        if (!plugin.storage.lives.containsKey(player.name.getUUID())) {
            sender.sendMessage("&cPlayer ${player.name} has no entry in lives!".toComponent())
            return false
        }

        plugin.storage.lives[player.name.getUUID()]!!.lives = lives
        plugin.storage.save()

        sender.sendMessage("&aSuccessfully set ${player.name}'s lives to $lives!".toComponent())
        return false
    }
}

class EditlivesCompelter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String> {
        when (args?.size) {
            1 -> {
                val completions = mutableListOf<String>()

                val players = Bukkit.getServer().onlinePlayers.map { it.name }.toTypedArray().toMutableList()
                StringUtil.copyPartialMatches(args[0], players, completions)
                return completions
            }

            2 -> {
                return mutableListOf("<number>")
            }

            else -> {
                return mutableListOf()
            }
        }
    }
}
