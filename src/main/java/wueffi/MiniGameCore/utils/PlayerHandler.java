package wueffi.MiniGameCore.utils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import wueffi.MiniGameCore.managers.GameManager;
import wueffi.MiniGameCore.managers.LobbyManager;

public class PlayerHandler implements Listener {

    private static Plugin plugin;

    public PlayerHandler(Plugin plugin2) {
        plugin = plugin2;
    }

    public static void PlayerReset(Player player) {
        Lobby lobby = LobbyManager.getLobbyByPlayer(player);

        if (lobby != null) {
            GameConfig config = GameManager.loadGameConfigFromWorld(lobby.getWorldFolder());
            if (config.getTeams() > 0) {
                Team team = lobby.getTeamByPlayer(player);
                if (team != null) {
                    team.removePlayer(player);
                    team.decreaseAlive();
                }
            }

            if (lobby.getOwner() == player) {
                lobby.removePlayer(player);
                for (Player player1 : lobby.getPlayers()) {
                    player1.sendMessage("§8[§6MiniGameCore§8]§c Owner of the lobby left... resetting");
                }
                LobbyHandler.LobbyReset(lobby);
            } else {
                GameManager.playerDeath(player.getUniqueId());
                lobby.removePlayer(player);
                if (lobby.getPlayers().isEmpty()) {
                    LobbyHandler.LobbyReset(lobby);
                }
            }
        }
        PlayerSoftReset(player);
        player.setGameMode(Bukkit.getDefaultGameMode());
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
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            PlayerReset(player);
        }, 5L);
        GameManager.frozenPlayers.remove(player);
    }
}
