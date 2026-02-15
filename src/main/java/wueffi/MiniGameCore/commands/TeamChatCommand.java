package wueffi.MiniGameCore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import wueffi.MiniGameCore.managers.LobbyManager;
import wueffi.MiniGameCore.utils.Lobby;
import wueffi.MiniGameCore.utils.Team;

public class TeamChatCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }

        if (!(player.hasPermission("mgcore.teamchat"))) {
            player.sendMessage("§cYou do not have permission to use this command!");
        }

        if (args.length < 1) return false; // Bukkit handles the usage message for us

        Lobby lobby = LobbyManager.getLobbyByPlayer(player);

        if (lobby == null) {
            player.sendMessage("§cYou're not in a lobby!");
            return true;
        }

        Team team = lobby.getTeamByPlayer(player);

        if (team == null) {
            player.sendMessage("§cYou're not on a team!");
            return true;
        }

        String msg = String.join(" ", args);

        for (Player target : team.getPlayers()) {
            target.sendMessage("§bTeam §7|§e " + player.getName() + ": §f" + msg);
        }

        return true;
    }
}
