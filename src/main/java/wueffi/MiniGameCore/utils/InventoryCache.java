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
                cloneContents(inventory.getContents()),
                cloneContents(inventory.getArmorContents()),
                cloneItem(inventory.getItemInOffHand()),
                cloneItem(player.getItemOnCursor())
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

    private static ItemStack[] cloneContents(ItemStack[] contents) {
        ItemStack[] cloned = new ItemStack[contents.length];

        for (int i = 0; i < contents.length; i++) {
            cloned[i] = cloneItem(contents[i]);
        }
        return cloned;
    }

    private static ItemStack cloneItem(ItemStack item) {
        return item == null ? null : item.clone();
    }

    public static void removeCache(Player player) {
        cache.remove(player);
    }

    private record InventoryData(ItemStack[] contents, ItemStack[] armor, ItemStack offHand, ItemStack cursor) {}
}