package io.github.devrawr.practice.player.layout

import io.github.devrawr.practice.PracticeConfig
import io.github.devrawr.practice.kit.KitService
import io.github.devrawr.practice.kit.queue.menu.QueueMenu
import io.github.devrawr.practice.match.MatchType
import io.github.devrawr.practice.match.team.MatchTeam
import io.github.devrawr.practice.player.PlayerState
import io.github.devrawr.practice.util.ItemWrapper
import io.github.devrawr.tasks.Tasks
import org.bukkit.ChatColor
import org.bukkit.Material

val LOBBY_LAYOUT = Layout(
    "lobby-layout",
    listOf(
        ItemWrapper(Material.IRON_SWORD)
            .index(0)
            .displayName("${ChatColor.YELLOW}${ChatColor.BOLD}Unranked Queue")
            .action { event ->
                val player = event.player
                val kit = KitService.kits.first()

                // add player to queue... temporary for testing!
                QueueMenu(
                    player, kit.createTeamFromIds(listOf(player.uniqueId)), MatchType.Solo
                ).updateMenu()
            }
    ),
    spawnLocation = PracticeConfig.LOBBY_LOCATION
)

val MATCH_LAYOUT = Layout("match-layout")

object LayoutService
{
    private val layouts = hashMapOf(
        "lobby-layout" to LOBBY_LAYOUT,
        "match-layout" to MATCH_LAYOUT
    )

    private val profileStateLayouts = hashMapOf(
        PlayerState.Lobby to "lobby-layout",
        PlayerState.Queue to "queue-layout",
        PlayerState.Match to "match-layout"
    )

    fun retrieveByState(state: PlayerState): Layout
    {
        return layouts[profileStateLayouts[state]!!] ?: Layout("none")
    }
}