package com.gkadmincore.commands

import com.gameknight.admincore.utils.MessagesUtils // Ensure this import is correct
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin // Ensure this import is correct

class EnchantCommand(private val plugin: JavaPlugin) : CommandExecutor {

    private val messagesUtils = MessagesUtils(plugin) // Initialize MessagesUtils with the plugin instance

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("gkadmincore.enchant")) {
            sender.sendMessage(messagesUtils.getMessage("error-no-permission"))
            return true
        }

        if (sender !is Player) {
            sender.sendMessage(messagesUtils.getMessage("error-player-only"))
            return true
        }

        if (args.size < 2) {
            sender.sendMessage(messagesUtils.getMessage("enchant-usage"))
            return true
        }

        val enchantment = Enchantment.getByName(args[0].uppercase())
        if (enchantment == null) {
            sender.sendMessage(messagesUtils.getMessage("error-invalid-enchantment"))
            return true
        }

        val level = args[1].toIntOrNull()
        if (level == null || level <= 0) {
            sender.sendMessage(messagesUtils.getMessage("error-invalid-level"))
            return true
        }

        val item = sender.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            sender.sendMessage(messagesUtils.getMessage("error-no-item-in-hand"))
            return true
        }

        // Apply enchantment without restrictions
        item.addUnsafeEnchantment(enchantment, level)
        sender.sendMessage(messagesUtils.getPlayerMessage("enchant-success", sender, enchantment.key.key, level, item.type.name.lowercase()))
        return true
    }
}
