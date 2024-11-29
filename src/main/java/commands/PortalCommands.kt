package com.gameknight.admincore.commands

import com.gameknight.admincore.utils.MessageUtils
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PortalCommands : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(MessageUtils.getMessage("errors.player_only"))
            return true
        }

        if (!sender.hasPermission("gkadmincore.portal")) {
            sender.sendMessage(MessageUtils.getMessage("errors.no_permission"))
            return true
        }

        when (label.lowercase()) {
            "pos1" -> {
                // Assume position is set via utility method
                PortalUtils.setPosition(sender, 1)
                sender.sendMessage(MessageUtils.getMessage("success.portal_pos1_set", mapOf("location" to sender.location.toString())))
            }

            "pos2" -> {
                PortalUtils.setPosition(sender, 2)
                sender.sendMessage(MessageUtils.getMessage("success.portal_pos2_set", mapOf("location" to sender.location.toString())))
            }

            "setpos3" -> {
                if (args.isEmpty()) {
                    sender.sendMessage(
                        MessageUtils.getMessage(
                            "errors.invalid_args",
                            mapOf("usage" to "/setpos3 <portal name>")
                        )
                    )
                    return true
                }
                val portalName = args[0]
                PortalUtils.setDestination(sender, portalName)
                sender.sendMessage(
                    MessageUtils.getMessage(
                        "success.portal_destination_set",
                        mapOf("portal" to portalName, "location" to sender.location.toString())
                    )
                )
            }

            "gkportalwand" -> {
                val wand = ItemStack(Material.STICK)
                val meta = wand.itemMeta
                meta?.setDisplayName("Portal Wand")
                wand.itemMeta = meta

                sender.inventory.addItem(wand)
                sender.sendMessage(MessageUtils.getMessage("success.portal_wand_received"))
            }
        }

        return true
    }
}
