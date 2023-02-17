package com.jamesalin.deathban.commands

import com.jamesalin.deathban.Deathban
import com.jamesalin.deathban.toComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ShowLives(private val plugin: Deathban) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage("&cThe console can't have any lives!".toComponent())
            return true
        }

        val uuid = "${sender.uniqueId}"
        plugin.storage.visibility[uuid] = true

        sender.sendMessage("&2Your lives are now public!".toComponent())
        return true
    }
}
