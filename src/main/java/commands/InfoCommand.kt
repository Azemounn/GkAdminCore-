package com.gameknight.admincore.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class InfoCommand : CommandExecutor {

    // Define pages for commands
    private val pages = listOf(
        """
        ${ChatColor.DARK_AQUA}========= ${ChatColor.GOLD}GKAdminCore - General Commands ${ChatColor.DARK_AQUA}=========
        ${ChatColor.GREEN}/gka ${ChatColor.WHITE}- Displays plugin info for all players.
        ${ChatColor.GREEN}/warp <name> ${ChatColor.WHITE}- Teleport to a saved warp.
        ${ChatColor.GREEN}/rtp ${ChatColor.WHITE}- Random teleportation to a safe location.
        ${ChatColor.GREEN}/help ${ChatColor.WHITE}- Displays this help menu.
        ${ChatColor.GREEN}/fly ${ChatColor.WHITE}- Toggle flight mode.
        ${ChatColor.GREEN}/flyspeed <1-10> ${ChatColor.WHITE}- Adjust your flight speed.
        ${ChatColor.DARK_AQUA}==============================================
        """.trimIndent(),

        """
        ${ChatColor.DARK_AQUA}========= ${ChatColor.GOLD}GKAdminCore - Admin Commands ${ChatColor.DARK_AQUA}=========
        ${ChatColor.AQUA}/sudo <player> <command> ${ChatColor.WHITE}- Execute a command as another player.
        ${ChatColor.AQUA}/enchant <enchantment> <level> ${ChatColor.WHITE}- Add an enchantment to an item, bypass restrictions.
        ${ChatColor.AQUA}/pos1, /pos2 ${ChatColor.WHITE}- Set portal region positions.
        ${ChatColor.AQUA}/setpos3 <portalName> ${ChatColor.WHITE}- Set the destination for a portal region.
        ${ChatColor.AQUA}/gkportalwand ${ChatColor.WHITE}- Get a wand to select portal regions.
        ${ChatColor.AQUA}/tp <player>, /tpa <player> ${ChatColor.WHITE}- Teleportation commands.
        ${ChatColor.AQUA}/warp ${ChatColor.WHITE}- Manage and teleport to warps.
        ${ChatColor.DARK_AQUA}==============================================
        """.trimIndent(),

        """
        ${ChatColor.DARK_AQUA}========= ${ChatColor.GOLD}GKAdminCore - Plugin Info ${ChatColor.DARK_AQUA}=========
        ${ChatColor.GREEN}Author: ${ChatColor.YELLOW}Azemounn
        ${ChatColor.GREEN}Version: ${ChatColor.YELLOW}1.0
        ${ChatColor.GREEN}Website: ${ChatColor.YELLOW}https://github.com/Azemounn
        ${ChatColor.GREEN}Description: ${ChatColor.WHITE}GKAdminCore is a feature-packed plugin designed for managing 
          portals, teleportation, commands, and admin tools seamlessly.
        ${ChatColor.DARK_AQUA}==============================================
        """.trimIndent()
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val page = args.getOrNull(0)?.toIntOrNull() ?: 1
        val maxPage = pages.size

        if (page !in 1..maxPage) {
            sender.sendMessage("${ChatColor.RED}Invalid page number. Please use a number between 1 and $maxPage.")
            return true
        }

        val header = "${ChatColor.DARK_AQUA}========= ${ChatColor.GOLD}GKAdminCore Help (${ChatColor.YELLOW}Page $page/$maxPage${ChatColor.GOLD}) ${ChatColor.DARK_AQUA}========="
        val footer = "${ChatColor.DARK_AQUA}Type ${ChatColor.YELLOW}/info <page> ${ChatColor.DARK_AQUA}to view another page."

        sender.sendMessage(header)
        sender.sendMessage(pages[page - 1])
        sender.sendMessage(footer)

        return true
    }
}
