package com.jamesalin.deathban

class Config(private val plugin: Deathban) {
    val interval: Int
        get() = plugin.config.getInt("updateInterval")
    val lastUpdated: Long
        get() = plugin.config.getLong("lastUpdated")
    val livesLimit: Int
        get() = plugin.config.getInt("livesLimit")
    val banLength: String
        get() = plugin.config.getString("banLength")!!
    val playerDeathCost: Int
        get() = plugin.config.getInt("playerDeathCost")
    val nonPlayerDeathCost: Int
        get() = plugin.config.getInt("nonPlayerDeathCost")

    fun save() {
        plugin.config.options().copyDefaults(true)
        plugin.saveConfig()
    }

    fun onEnable() {
        plugin.saveDefaultConfig()

        // Config default values
        plugin.config.addDefault("livesLimit", "4")
        plugin.config.addDefault("updateInterval", "24")
        plugin.config.addDefault("lastUpdated", "0")
        plugin.config.addDefault("lives", "{}")
        plugin.config.addDefault("playerDeathCost ", "2")
        plugin.config.addDefault("nonPlayerDeathCost", "1")
    }
}