package io.github.devrawr.practice.arena.generation

import io.github.devrawr.practice.arena.Arena
import io.github.devrawr.practice.extensions.player
import io.github.devrawr.practice.match.Match
import io.github.devrawr.practice.match.team.MatchTeam
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

class PreparedArena(
    val arena: Arena,
    val offsetX: Int, // this will probably the only offset used. we can make every arena be on a z-grid, so every single arena-type is on a single row.
    val offsetZ: Int // read comment above, this might not get used. we just have this in case.
)
{
    var currentMatch: Match? = null

    // NOTE: despite its name, this should not be a finalize() block.
    fun destruct()
    {
        val match = currentMatch

        if (match != null)
        {
            for (block in match.trackedBlocks)
            {
                block
                    .location
                    .block
                    .type = Material.AIR
            }

//            this.currentMatch = null
        }
    }

    fun getScaledLocation(location: Location): Location
    {
        return location
            .clone()
            .add(
                offsetX.toDouble(),
                0.0,
                offsetZ.toDouble()
            )
    }

    fun teleport(player: Player, match: Match)
    {
        this.teleport(
            player = player,
            index = (match.firstTeam.ids.keys.contains(player.uniqueId)).compareTo(false)
        )
    }

    fun teleport(team: MatchTeam, match: Match)
    {
        this.teleport(
            team = team,
            index = (match.firstTeam == team).compareTo(false)
        )
    }

    fun teleport(team: MatchTeam, index: Int)
    {
        team.execute { current ->
            current.player?.let {
                teleport(it, index)
            }
        }
    }

    fun teleport(player: Player, index: Int)
    {
        val location = if (index == 0)
        {
            arena.firstLocation
        } else
        {
            arena.secondLocation
        }

        if (location != null)
        {
            player.teleport(
                getScaledLocation(location)
            )
        }
    }
}