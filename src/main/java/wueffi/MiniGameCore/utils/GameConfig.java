package wueffi.MiniGameCore.utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GameConfig {
    private final GameMode gameMode;
    private final String gameName;
    private final String hostPerm;
    private final String joinPerm;
    private final int timeLimit;
    private final int maxPlayers;
    private final int teams;
    private final int minPlayers;
    private final int respawnDelay;
    private final List<SpawnPoint> spawnPoints = new ArrayList<>();
    private final List<TeamSpawnPoints> teamSpawnPoints = new ArrayList<>();
    private final List<Material> startInventory = new ArrayList<>();
    private final Set<Material> allowedBreakBlocks = new HashSet<>();
    private final Set<Material> allowedPlaceBlocks = new HashSet<>();
    private final Set<DamageCause> blockedDamageCauses = new HashSet<>();
    private final boolean respawnMode;
    private final boolean doDurability;
    private final boolean allowPVP;
    private final boolean respawnByAPI;
    private final boolean allowFriendlyFire;
    private final boolean allowCrafting;
    private final boolean silenceDeathMessages;
    private final boolean doHunger;
    private final boolean allowOpeningContainers;
    private final boolean allowedBreakBlocksExist;
    private final boolean allowedPlaceBlocksExist;
    private final boolean locatorBar;

    public GameConfig(File configFile) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.gameName = config.getString("game.name", "default_game_name");
        this.hostPerm = config.getString("game.hostPerm", "mgcore.host");
        this.joinPerm = config.getString("game.joinPerm", "mgcore.join");
        this.maxPlayers = config.getInt("game.maxPlayers", 8);
        this.teams = config.getInt("game.teams", 0);
        this.minPlayers = config.getInt("game.minPlayers", 2);
        this.respawnMode = config.getBoolean("game.respawnMode", false);
        this.respawnDelay = config.getInt("game.respawnDelay", 0);
        this.doDurability = config.getBoolean("game.doDurability", true);
        this.allowPVP = config.getBoolean("game.allowPVP", true);
        this.respawnByAPI = config.getBoolean("game.respawnByAPI", false);
        this.timeLimit = config.getInt("game.timeLimit", 600); //10 Minutes
        this.allowFriendlyFire = config.getBoolean("game.allowFriendlyFire", false);
        this.allowCrafting = config.getBoolean("game.allowCrafting", false);
        this.silenceDeathMessages = config.getBoolean("game.silenceDeathMessages", false);
        this.doHunger = config.getBoolean("game.doHunger", false);
        this.allowOpeningContainers = config.getBoolean("game.allowOpeningContainers", false);
        this.locatorBar = config.getBoolean("game.doLocatorBar", false);

        GameMode tempGm;

        try {
            tempGm = GameMode.valueOf(config.getString("game.gameMode", "survival").toUpperCase());
        } catch (IllegalArgumentException ignored) {
            tempGm = GameMode.SURVIVAL;
        }

        this.gameMode = tempGm;

        this.allowedBreakBlocksExist = config.contains("game.allowedBreakBlocks") || config.contains("game.allowed_break_blocks");
        this.allowedPlaceBlocksExist = config.contains("game.allowedPlaceBlocks") || config.contains("game.allowed_place_blocks");

        if (config.contains("game.spawnPoints")) {
            for (String key : config.getConfigurationSection("game.spawnPoints").getKeys(false)) {
                int x = config.getInt("game.spawnPoints." + key + ".x");
                int y = config.getInt("game.spawnPoints." + key + ".y");
                int z = config.getInt("game.spawnPoints." + key + ".z");
                spawnPoints.add(new SpawnPoint(x, y, z));
            }
        }

        if (config.contains("game.teamSpawnPoints")) {
            for (String team : config.getConfigurationSection("game.teamSpawnPoints").getKeys(false)) {
                List<TeamSpawnPoint> teamSpawns = new ArrayList<>();
                for (String key : config.getConfigurationSection("game.teamSpawnPoints." + team).getKeys(false)) {
                    int x = config.getInt("game.teamSpawnPoints." + team + "." + key + ".x");
                    int y = config.getInt("game.teamSpawnPoints." + team + "." + key + ".y");
                    int z = config.getInt("game.teamSpawnPoints." + team + "." + key + ".z");
                    teamSpawns.add(new TeamSpawnPoint(x, y, z));
                }
                teamSpawnPoints.add(new TeamSpawnPoints(team, teamSpawns));
            }
        }

        if (config.contains("game.inventory")) {
            for (String item : config.getStringList("game.inventory")) {
                Material material = Material.getMaterial(item.toUpperCase());
                if (material != null) {
                    startInventory.add(material);
                }
            }
        }

        // DEPRECATED
        if (config.contains("game.allowed_break_blocks")) {
            for (String block : config.getStringList("game.allowed_break_blocks")) {
                Material material = Material.getMaterial(block.toUpperCase());
                if (material != null) {
                    allowedBreakBlocks.add(material);
                }
            }
        }

        if (config.contains("game.allowedBreakBlocks")) {
            for (String block : config.getStringList("game.allowed_break_blocks")) {
                Material material = Material.getMaterial(block.toUpperCase());
                if (material != null) {
                    allowedBreakBlocks.add(material);
                }
            }
        }

        // DEPRECATED
        if (config.contains("game.allowed_place_blocks")) {
            for (String block : config.getStringList("game.allowed_place_blocks")) {
                Material material = Material.getMaterial(block.toUpperCase());
                if (material != null) {
                    allowedPlaceBlocks.add(material);
                }
            }
        }

        if (config.contains("game.allowedPlaceBlocks")) {
            for (String block : config.getStringList("game.allowed_place_blocks")) {
                Material material = Material.getMaterial(block.toUpperCase());
                if (material != null) {
                    allowedPlaceBlocks.add(material);
                }
            }
        }

        // DEPRECATED
        if (config.contains("game.blocked_damage_causes")) {
            for (String damCause : config.getStringList("game.blocked_damage_causes")) {
                try {
                    DamageCause damageCause = DamageCause.valueOf(damCause.toUpperCase());
                    blockedDamageCauses.add(damageCause);
                } catch (IllegalArgumentException ignored) {} // _ works in 22+, if we ever migrate to Java 22+, change this to _
            }
        }

        if (config.contains("game.blockedDamageCauses")) {
            for (String damCause : config.getStringList("game.blocked_damage_causes")) {
                try {
                    DamageCause damageCause = DamageCause.valueOf(damCause.toUpperCase());
                    blockedDamageCauses.add(damageCause);
                } catch (IllegalArgumentException ignored) {} // _ works in 22+, if we ever migrate to Java 22+, change this to _
            }
        }
    }

    public @NotNull String getHostPerm() {
        return hostPerm;
    }

    public @NotNull String getJoinPerm() {
        return joinPerm;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getTeams() {
        return teams;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public List<SpawnPoint> getSpawnPoints() {
        return spawnPoints;
    }

    public List<TeamSpawnPoints> getTeamSpawnPoints() {
        return teamSpawnPoints;
    }

    public List<Material> getStartInventory() {
        return startInventory;
    }

    public boolean allowedBreakBlocksExist() {
        return allowedBreakBlocksExist;
    }

    public boolean allowedPlaceBlocksExist() {
        return allowedPlaceBlocksExist;
    }

    public Set<Material> getAllowedBreakBlocks() {
        return allowedBreakBlocks;
    }

    public Set<Material> getAllowedPlaceBlocks() {
        return allowedPlaceBlocks;
    }

    public boolean getRespawnMode() {
        return respawnMode;
    }

    public boolean getDurabilityMode() {
        return doDurability;
    }

    public boolean getPVPMode() {
        return allowPVP;
    }

    public boolean getRespawnByAPI() {
        return respawnByAPI;
    }

    public boolean getAllowFriendlyFire() {
        return allowFriendlyFire;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public boolean getAllowCrafting() {
        return allowCrafting;
    }

    public Set<DamageCause> getBlockedDamageCauses() {
        return blockedDamageCauses;
    }

    public boolean getSilenceDeathMessages() {
        return silenceDeathMessages;
    }

    public boolean getDoHunger() {
        return doHunger;
    }

    public boolean getAllowOpeningContainers() {
        return allowOpeningContainers;
    }

    public boolean getLocatorBar() {
        return locatorBar;
    }

    public record SpawnPoint(int x, int y, int z) {
    }

    public record TeamSpawnPoint(int x, int y, int z) {
    }

    public record TeamSpawnPoints(String teamName, List<TeamSpawnPoint> spawnPoints) {
    }
}
