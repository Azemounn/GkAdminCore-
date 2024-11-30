package com.gkadmincore.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class HelpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val page = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 1 else 1

        when (page) {
            1 -> {
                sender.sendMessage("§6[GKAdminCore] §eHelp Menu §7(Page 1/3)")
                sender.sendMessage("§7Basic Commands:")
                sender.sendMessage("§a/fly §f- Toggle flight mode.")
                sender.sendMessage("§a/flyspeed §f- Set flight speed (0.1 - 1.0).")
                sender.sendMessage("§a/warp <name> §f- Teleport to a warp.")
                sender.sendMessage("§a/warps §f- List all available warps.")
                sender.sendMessage("§a/rtp §f- Random teleport.")
                sender.sendMessage("§7Type §e/help 2 §7for the next page.")
            }

            2 -> {
                sender.sendMessage("§6[GKAdminCore] §eHelp Menu §7(Page 2/3)")
                sender.sendMessage("§7Admin Commands:")
                sender.sendMessage("§a/sudo <player> <command> §f- Run a command as another player.")
                sender.sendMessage("§a/tp <player> §f- Teleport to a player.")
                sender.sendMessage("§a/tphere <player> §f- Bring a player to your location.")
                sender.sendMessage("§a/tpa <player> §f- Request to teleport to a player.")
                sender.sendMessage("§a/tpaccept, /tpdeny §f- Accept or deny teleport requests.")
                sender.sendMessage("§7Type §e/help 3 §7for the next page.")
            }

            3 -> {
                sender.sendMessage("§6[GKAdminCore] §eHelp Menu §7(Page 3/3)")
                sender.sendMessage("§7Portal Commands:")
                sender.sendMessage("§a/gkportalwand §f- Get the portal wand.")
                sender.sendMessage("§a/pos1, /pos2 §f- Set portal positions.")
                sender.sendMessage("§a/setdesti <name> §f- Set portal destination.")
                sender.sendMessage("§a/gkportalregion <create|delete|list> §f- Manage portal regions.")
                sender.sendMessage("§7End of help pages.")
            }

            else -> {
                sender.sendMessage("§cInvalid page number. Type §e/help 1 §cfor the first page.")
            }
        }

        return true
    }
}
