package com.gkadmincore.commands

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class EnchantCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("gkadmincore.enchant")) {
            sender.sendMessage("§cYou do not have permission to use this command!")
            return true
        }

        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command!")
            return true
        }

        if (args.size < 2) {
            sender.sendMessage("§cUsage: /enchant <enchantment> <level>")
            return true
        }

        val enchantment = Enchantment.getByName(args[0].uppercase())
        if (enchantment == null) {
            sender.sendMessage("§cInvalid enchantment name!")
            return true
        }

        val level = args[1].toIntOrNull()
        if (level == null || level <= 0) {
            sender.sendMessage("§cLevel must be a positive number!")
            return true
        }

        val item = sender.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            sender.sendMessage("§cYou must be holding an item to enchant it!")
            return true
        }

        // Apply enchantment without restrictions
        item.addUnsafeEnchantment(enchantment, level)
        sender.sendMessage("§aSuccessfully added ${enchantment.key.key} level $level to your ${item.type.name.lowercase()}.")
        return true
    }
}
