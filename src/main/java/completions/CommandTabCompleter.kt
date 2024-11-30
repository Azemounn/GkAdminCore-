package com.gkadmincore.completions

import com.gkadmincore.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.enchantments.Enchantment

class CommandTabCompleter : TabCompleter {

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
        val availableCommands = when (command.name.lowercase()) {
            "warp" -> ConfigUtils.getWarpList().toMutableList()

            "gkportalregion" -> when (args.size) {
                1 -> listOf("create", "delete", "list").toMutableList()
                2 -> if (args[0].lowercase() == "delete") ConfigUtils.getPortalList().toMutableList() else ConfigUtils.getWarpList().toMutableList()
                else -> mutableListOf()
            }

            "setdesti" -> ConfigUtils.getWarpList().toMutableList()

            "tpa", "tphere", "tp" -> Bukkit.getOnlinePlayers().map { it.name }.toMutableList()

            "enchant" -> when (args.size) {
                1 -> Enchantment.values().map { it.key.key }.toMutableList()
                2 -> listOf("<level>").toMutableList()
                else -> mutableListOf()
            }

            "deleteportal", "listportals" -> ConfigUtils.getPortalList().toMutableList()

            else -> null
        }

        return availableCommands?.filter { it.startsWith(args.last(), ignoreCase = true) }?.toMutableList()
    }
}
