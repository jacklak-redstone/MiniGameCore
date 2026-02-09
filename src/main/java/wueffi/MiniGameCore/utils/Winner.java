package wueffi.MiniGameCore.utils;

import org.bukkit.entity.Player;

public sealed interface Winner
        permits Winner.PlayerWinner, Winner.TeamWinner {

    final class PlayerWinner implements Winner {
        private final Player player;

        public PlayerWinner(Player player) {
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }
    }

    final class TeamWinner implements Winner {
        private final Team team;

        public TeamWinner(Team team) {
            this.team = team;
        }

        public Team getTeam() {
            return team;
        }
    }
}
