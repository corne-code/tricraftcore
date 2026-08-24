package nl.tricraft.tricraftcore.inventory;

import nl.tricraft.tricraftcore.TricraftCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldInventoryManager implements Listener {

    private final TricraftCore plugin;

    private final Map<UUID, Map<String, ItemStack[]>> inventories = new HashMap<>();
    private final Map<UUID, Map<String, ItemStack[]>> armor = new HashMap<>();

    public WorldInventoryManager(TricraftCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTask(plugin, () -> {
            loadInventory(player, player.getWorld());
        });
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        saveInventory(player, event.getFrom());

        Bukkit.getScheduler().runTask(plugin, () -> {
            loadInventory(player, player.getWorld());
        });
    }

    private void saveInventory(Player player, World world) {

        UUID uuid = player.getUniqueId();
        String worldName = world.getName();

        inventories
                .computeIfAbsent(uuid, key -> new HashMap<>())
                .put(worldName, player.getInventory().getContents().clone());

        armor
                .computeIfAbsent(uuid, key -> new HashMap<>())
                .put(worldName, player.getInventory().getArmorContents().clone());
    }

    private void loadInventory(Player player, World world) {

        UUID uuid = player.getUniqueId();
        String worldName = world.getName();

        player.getInventory().clear();

        Map<String, ItemStack[]> playerInventories =
                inventories.get(uuid);

        if (playerInventories != null &&
                playerInventories.containsKey(worldName)) {

            ItemStack[] contents = playerInventories.get(worldName);

            if (contents != null) {
                player.getInventory().setContents(contents.clone());
            }
        }

        Map<String, ItemStack[]> playerArmor =
                armor.get(uuid);

        if (playerArmor != null &&
                playerArmor.containsKey(worldName)) {

            ItemStack[] armorContents = playerArmor.get(worldName);

            if (armorContents != null) {
                player.getInventory().setArmorContents(
                        armorContents.clone()
                );
            }
        }

        player.updateInventory();
    }

    public void savePlayer(Player player) {
        saveInventory(player, player.getWorld());
    }
}
