package com.gameknight.admincore

import com.gameknight.admincore.commands.*
import com.gameknight.admincore.utils.MessageUtils
import org.bukkit.plugin.java.JavaPlugin

class AdminCore : JavaPlugin() {

    override fun onEnable() {
        logger.info("Enabling GKAdminCore by Azemounn...")

        // Load messages configuration
        try {
            MessageUtils.loadMessages(dataFolder)
            logger.info("Messages loaded successfully.")
        } catch (e: Exception) {
            logger.severe("Failed to load messages.yml: ${e.message}")
        }

        // Register commands
        registerCommand("fly", FlyCommand())
        registerCommand("flyspeed", FlySpeedCommand())
        registerCommand("sudo", SudoCommand())
        registerCommand("enchant", EnchantCommand())
        registerCommand("warp", WarpCommand(), CommandTabCompleter())
        registerCommand("gkportalwand", PortalCommands())
        registerCommand("info", InfoCommand())
        registerCommand("publicinfo", PublicInfoCommand())
        registerCommand("rtp", RandomTeleportCommand())

        logger.info("GKAdminCore by Azemounn has been enabled!")
    }

    override fun onDisable() {
        logger.info("GKAdminCore by Azemounn has been disabled!")
    }

    /**
     * Helper method to register commands and optionally assign a tab completer.
     */
    private fun registerCommand(
        co
