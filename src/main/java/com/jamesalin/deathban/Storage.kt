package com.jamesalin.deathban

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Death(val deathMessage: String, val time: Long, val livesLost: Int)

@Serializable
data class LivesInfo(var lives: Int, val deaths: MutableList<Death>)

@Serializable
data class Punishment(val uuid: String, val livesLost: Int, val expires: Long)

@Serializable
data class StorageSchema(
    var lastUpdated: Long = 0,
    var lives: MutableMap<String, LivesInfo> = mutableMapOf(),
    var punishments: MutableList<Punishment> = mutableListOf(),
    var visibility: MutableMap<String, Boolean> = mutableMapOf()
)

class Storage(plugin: Deathban) {
    private val file = File(plugin.dataFolder, "data.json")
    private var data = StorageSchema()

    var lastUpdated: Long
        get() = data.lastUpdated
        set(value) = run { data.lastUpdated = value }
    var lives: MutableMap<String, LivesInfo>
        get() = data.lives
        set(value) = run { data.lives = value }
    var punishments: MutableList<Punishment>
        get() = data.punishments
        set(value) = run { data.punishments = value }
    var visibility: MutableMap<String, Boolean>
        get() = data.visibility
        set(value) = run { data.visibility = value }

    fun onEnable() {
        if (!file.exists()) {
            file.createNewFile()
            file.writeText(Json.encodeToString(StorageSchema()))
        }

        data = Json.decodeFromString(file.readText())
    }

    fun save() {
        file.writeText(Json.encodeToString(data))
    }
}