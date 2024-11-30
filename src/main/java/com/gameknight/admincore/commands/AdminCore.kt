package com.gkadmincore

import com.gkadmincore.commands.*
import com.gkadmincore.utils.ConfigUtils
import com.gkadmincore.completions.CommandTabCompleter
import org.bukkit.plugin.java.JavaPlugin
import com.gkadmincore.portals.PortalManager


class AdminCore : JavaPlugin() {

    override fun onEnable() {
        // Load configuration files
        saveDefaultConfig()
        ConfigUtils.loadConfig(this)

        // Register commands and events
        registerCommands()
        registerEvents()

        logger.info("GKAdminCore has been enabled successfully!")
    }

    override fun onDisable() {
        logger.info("GKAdminCore has been disabled!")
    }

    private fun registerCommands() {
        // Map commands to their executors
        val commands = mapOf(
            "fly" to FlyCommand(),
            "flyspeed" to FlySpeedCommand(),
            "sudo" to SudoCommand(),
            "enchant" to EnchantCommand(),
            "warp" to WarpCommand(),
            "rtp" to RTPCommand(),
            "tp" to TpCommand(),
            "tphere" to TpHereCommand(),
            "tpa" to TpaCommand(),
            "tpaccept" to TpAcceptCommand(),
            "tpdeny" to TpDenyCommand(),
            "gkportalwand" to PortalCommands.PortalWandCommand(),
            "pos1" to PortalCommands.SetPortalPositionsCommand("pos1"),
            "pos2" to PortalCommands.SetPortalPositionsCommand("pos2"),
            "setdesti" to PortalCommands.SetPortalDestinationCommand(),
            "gkportalregion" to PortalCommands.CreatePortalCommand(),
            "deleteportal" to PortalCommands.DeletePortalCommand(),
            "listportals" to PortalCommands.ListPortalsCommand()
        )

        // Register each command and its tab completer
        commands.forEach { (name, executor) ->
            val command = getCommand(name)
            if (command != null) {
                command.setExecutor(executor)
                command.tabCompleter = CommandTabCompleter()
            } else {
                logger.warning("Command $name is not defined in plugin.yml!")
            }
        }
    }

    private fun registerEvents() {
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(PortalCommands, this)
    }
}
