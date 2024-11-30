package com.gameknight.admincore.utils

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class MessagesUtils(private val plugin: JavaPlugin) {

    private lateinit var messagesConfig: YamlConfiguration

    init {
        loadMessages()
    }

    // Load messages from the messages.yml file
    private fun loadMessages() {
        val messagesFile = File(plugin.dataFolder, "messages.yml")
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false) // Copy the default if not exists
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile)
    }

    // Get message by key with dynamic replacements (e.g., {0}, {1}, {player_name}, etc.)
    fun getMessage(key: String, vararg replacements: Any): String {
        val message = messagesConfig.getString(key)
            ?: return "§cError: Message key '$key' not found."

        // Replace placeholders like {0}, {1}, etc., with actual values
        var finalMessage = message
        replacements.forEachIndexed { index, replacement ->
            finalMessage = finalMessage.replace("{${index}}", replacement.toString())
        }

        // Optionally replace player-specific placeholders like {player_name}
        finalMessage = finalMessage.replace("{player_name}", replacements.firstOrNull()?.toString() ?: "")

        // Return the final formatted message, ensuring color codes are parsed
        return finalMessage.replace("&", "§")
    }

    // Example of getting a message specific to a player
    fun getPlayerMessage(key: String, player: Player, vararg replacements: Any): String {
        return getMessage(key, player.name, *replacements)
    }

    // Get a list of messages from the config file (for lists of commands or other multi-line content)
    fun getList(key: String): List<String> {
        return messagesConfig.getStringList(key).map { it.replace("&", "§") }  // Replace color codes
    }
}
