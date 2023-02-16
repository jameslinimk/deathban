package com.jamesalin.deathban

class Config(private val plugin: Deathban) {
    val livesLimit: Int
        get() = plugin.config.getInt("livesLimit")
    val updateInterval: Int
        get() = plugin.config.getInt("updateInterval")
    val banLength: Int
        get() = plugin.config.getInt("banLength")
    val banReason: String
        get() = plugin.config.getString("banReason")!!
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
        plugin.config.addDefault("livesLimit", 4)
        plugin.config.addDefault("updateInterval", 24)
        plugin.config.addDefault("banLength", 24)
        plugin.config.addDefault("banReason", "@deathban")
        plugin.config.addDefault("playerDeathCost", 2)
        plugin.config.addDefault("nonPlayerDeathCost", 1)
    }
}