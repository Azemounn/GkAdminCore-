package com.gkadmincore.completions

import com.gkadmincore.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.enchantments.Enchantment

class TabCompletionUtils : TabCompleter {

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
        val suggestions = when (command.name.lowercase()) {

            // Tab completion for /warp command
            "warp" -> ConfigUtils.getWarpList().toMutableList()

            // Tab completion for /gkportalregion
            "gkportalregion" -> when (args.size) {
                1 -> listOf("create", "delete", "list").toMutableList() // Subcommands
                2 -> if (args[0].equals("delete", ignoreCase = true)) {
                    ConfigUtils.getPortalList().toMutableList() // Suggest portal names for deletion
                } else if (args[0].equals("create", ignoreCase = true)) {
                    listOf("<portal_name>").toMutableList() // Suggest a placeholder for portal name
                } else {
                    mutableListOf()
                }
                3 -> if (args[0].equals("create", ignoreCase = true)) {
                    ConfigUtils.getWarpList().toMutableList() // Suggest warp names for destination
                } else {
                    mutableListOf()
                }
                else -> mutableListOf()
            }

            // Tab completion for /setdesti
            "setdesti" -> ConfigUtils.getWarpList().toMutableList() // Suggest warp names

            // Tab completion for /tpa, /tphere, /tp commands
            "tpa", "tphere", "tp" -> Bukkit.getOnlinePlayers().map { it.name }.toMutableList()

            // Tab completion for /enchant command
            "enchant" -> when (args.size) {
                1 -> Enchantment.values().map { it.key.key }.toMutableList() // Suggest enchantment names
                2 -> listOf("<level>").toMutableList() // Suggest a placeholder for level
                else -> mutableListOf()
            }

            // Tab completion for /deleteportal
            "deleteportal" -> ConfigUtils.getPortalList().toMutableList() // Suggest portal names for deletion

            // Tab completion for /listportals
            "listportals" -> ConfigUtils.getPortalList().toMutableList() // Suggest portal names

            else -> null
        }

        // Filter suggestions based on user input
        return suggestions?.filter { it.startsWith(args.last(), ignoreCase = true) }?.toMutableList()
    }
}
