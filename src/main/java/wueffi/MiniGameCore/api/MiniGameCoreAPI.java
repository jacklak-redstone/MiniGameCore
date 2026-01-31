package wueffi.MiniGameCore.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import wueffi.MiniGameCore.managers.GameManager;
import wueffi.MiniGameCore.managers.LobbyManager;
import wueffi.MiniGameCore.utils.Lobby;
import wueffi.MiniGameCore.utils.Team;

import java.util.UUID;

public class MiniGameCoreAPI {
    private static final LobbyManager lobbyManager = LobbyManager.getInstance();

    public static LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public static void winPlayer(Lobby lobby, Player player) {
        GameManager.winGame(lobby, player, null);
    }

    public static void winTeam(Lobby lobby, Team team) {
        GameManager.winGame(lobby, null, team);
    }

    public static void playerDeath(UUID playerid) {
        GameManager.playerDeath(playerid);
    }

    public static void playerAlive(UUID playerid) {
        GameManager.playerAlive(playerid);
    }

    public static Location getRespawnLocation(UUID playerid) {
        return  GameManager.getRespawnPoint(playerid);
    }
}
