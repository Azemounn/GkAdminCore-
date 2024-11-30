package com.gkadmincore.utils

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

object MessagesUtils {
    private lateinit var plugin: JavaPlugin

    fun loadMessages(plugin: JavaPlugin) {
        this.plugin = plugin
    }

    fun sendNoPermissionMessage(sender: CommandSender) {
        sender.sendMessage("§6[GameKnight2k] §cYou do not have permission to execute this command.")
        sender.sendMessage("§7Plugin Version: ${plugin.description.version}")
        sender.sendMessage("§7Created by Azemounn.")
    }
}
