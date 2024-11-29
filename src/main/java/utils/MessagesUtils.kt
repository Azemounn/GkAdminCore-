package com.gameknight.admincore.utils

import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object MessageUtils {
    private lateinit var messages: YamlConfiguration

    fun loadMessages(dataFolder: File) {
        val file = File(dataFolder, "messages.yml")
        if (!file.exists()) {
            dataFolder.mkdirs()
            file.createNewFile()
        }
        messages = YamlConfiguration.loadConfiguration(file)
    }

    fun getMessage(key: String): String {
        return messages.getString(key, ChatColor.RED.toString() + "Message not found: $key")!!
    }
}
