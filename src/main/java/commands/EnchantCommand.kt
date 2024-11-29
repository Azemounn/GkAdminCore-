package com.gameknight.admincore.commands

import com.gameknight.admincore.utils.MessagesUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import com.gameknight.admincore.utils.MessageUtils
import com.gameknight.admincore.portals.PortalUtils


class EnchantCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(MessageUtils.getMessage("errors.player_only"))
            return true
        }

        if (!sender.hasPermission("gkadmincore.enchant")) {
            sender.sendMessage(MessageUtils.getMessage("errors.no_permission"))
            return true
        }

        if (args.size < 2) {
            sender.sendMessage(
                MessageUtils.getMessage(
                    "errors.invalid_args",
                    mapOf("usage" to "/enchant <enchantment> <level>")
                )
            )
            return true
        }

        val enchantName = args[0].uppercase()
        val level = args[1].toIntOrNull()

        if (level == null || level <= 0) {
            sender.sendMessage(MessageUtils.getMessage("errors.invalid_number"))
            return true
        }

        val enchantment = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft(enchantName.lowercase()))
        if (enchantment == null) {
            sender.sendMessage(MessageUtils.getMessage("errors.invalid_args", mapOf("usage" to "Unknown enchantment: $enchantName")))
            return true
        }

        val item = sender.inventory.itemInMainHand
        if (item.type.isAir) {
            sender.sendMessage(MessageUtils.getMessage("errors.no_item"))
            return true
        }

        item.addUnsafeEnchantment(enchantment, level)
        sender.sendMessage(
            MessageUtils.getMessage(
                "success.enchant_applied",
                mapOf("enchant" to enchantName, "level" to level.toString())
            )
        )

        return true
    }
}
