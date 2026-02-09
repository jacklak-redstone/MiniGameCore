package wueffi.MiniGameCore.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import wueffi.MiniGameCore.utils.Lobby;
import wueffi.MiniGameCore.utils.Winner;

public class GameOverEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Lobby lobby;
    private final Winner winner;

    public GameOverEvent(Lobby lobby, Winner winner) {
        this.lobby = lobby;
        this.winner = winner;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public Winner getWinner() {
        return winner;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
