package wueffi.MiniGameCore.utils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import wueffi.MiniGameCore.managers.GameManager;
import wueffi.MiniGameCore.managers.LobbyManager;


public class PlayerHandler implements Listener {

    public static void PlayerReset(Player player) {
        Lobby lobby = LobbyManager.getLobbyByPlayer(player);

        if (lobby != null) {
            Team team = lobby.getTeamByPlayer(player);
            if (team != null) team.removePlayer(player);
            lobby.removePlayer(player);
            if (lobby.getPlayers().isEmpty()) {
                LobbyHandler.LobbyReset(lobby);
            }
        }
        PlayerSoftReset(player);
        player.setGameMode(GameMode.CREATIVE);
        World mainWorld = Bukkit.getWorlds().getFirst();
        if (mainWorld != null) {
            Location spawn = mainWorld.getSpawnLocation();
            player.teleport(spawn);
        }
    }

    public static void PlayerSoftReset(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(5);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.setFireTicks(0);
        player.setExp(0);
        player.setLevel(69);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setItemOnCursor(null);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerReset(player);
        GameManager.frozenPlayers.remove(player);
    }
}
