package wueffi.MiniGameCore.api;

import org.bukkit.entity.Player;
import wueffi.MiniGameCore.managers.GameManager;
import wueffi.MiniGameCore.managers.LobbyManager;
import wueffi.MiniGameCore.utils.Lobby;

public class MiniGameCoreAPI {
    private static final LobbyManager lobbyManager = LobbyManager.getInstance();

    public static LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public static void winPlayer(Lobby lobby, Player player) {
        GameManager.winGame(lobby, player);
    }
}
