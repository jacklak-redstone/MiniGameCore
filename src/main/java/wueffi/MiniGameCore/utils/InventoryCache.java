package wueffi.MiniGameCore.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wueffi.MiniGameCore.MiniGameCore;

import java.util.HashMap;
import java.util.Map;

public class InventoryCache {
    private static final Map<Player, InventoryData> cache = new HashMap<>();
    private static final MiniGameCore plugin = MiniGameCore.getPlugin();

    public static void saveInventory(Player player) {
        if (!plugin.getResetInventories()) return;
        PlayerInventory inventory = player.getInventory();

        cache.put(player, new InventoryData(
                inventory.getContents().clone(),
                inventory.getArmorContents().clone(),
                inventory.getItemInOffHand().clone(),
                player.getItemOnCursor().clone()
        ));
    }

    public static void loadInventory(Player player) {
        if (!plugin.getResetInventories()) return;
        InventoryData data = cache.get(player);

        if (data == null) {
            return;
        }

        PlayerInventory inventory = player.getInventory();

        inventory.setContents(data.contents.clone());
        inventory.setArmorContents(data.armor.clone());
        inventory.setItemInOffHand(data.offHand.clone());
        player.setItemOnCursor(data.cursor.clone());

        player.updateInventory();
    }

    private record InventoryData(ItemStack[] contents, ItemStack[] armor, ItemStack offHand, ItemStack cursor) {}
}