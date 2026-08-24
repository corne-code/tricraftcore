package nl.tricraft.tricraftcore.inventory;

import nl.tricraft.tricraftcore.TricraftCore;
import nl.tricraft.tricraftcore.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class WorldInventoryManager implements Listener {

    private final TricraftCore plugin;
    private final DatabaseManager database;

    public WorldInventoryManager(TricraftCore plugin) {
        this.plugin = plugin;
        this.database = plugin.getDatabaseManager();
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

        // Inventory van de vorige wereld opslaan
        saveInventory(player, event.getFrom());

        // Inventory van de nieuwe wereld laden
        Bukkit.getScheduler().runTask(plugin, () -> {
            loadInventory(player, player.getWorld());
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        saveInventory(player, player.getWorld());
    }

    public void savePlayer(Player player) {
        saveInventory(player, player.getWorld());
    }

    private void saveInventory(Player player, World world) {

        try {

            UUID uuid = player.getUniqueId();
            String worldName = world.getName();

            String inventory = serialize(
                    player.getInventory().getContents()
            );

            String armor = serialize(
                    player.getInventory().getArmorContents()
            );

            database.saveInventory(
                    uuid,
                    worldName,
                    inventory,
                    armor
            );

        } catch (Exception e) {

            plugin.getLogger().severe(
                    "Inventory van "
                            + player.getName()
                            + " kon niet worden opgeslagen."
            );

            e.printStackTrace();
        }
    }

    private void loadInventory(Player player, World world) {

        try {

            UUID uuid = player.getUniqueId();
            String worldName = world.getName();

            DatabaseManager.InventoryData data =
                    database.loadInventory(uuid, worldName);

            // Geen opgeslagen inventory?
            // Dan laten we de bestaande inventory gewoon staan.
            if (data == null) {
                return;
            }

            ItemStack[] contents =
                    deserialize(data.inventory());

            ItemStack[] armor =
                    deserialize(data.armor());

            player.getInventory().clear();

            if (contents != null) {
                player.getInventory().setContents(contents);
            }

            if (armor != null) {
                player.getInventory().setArmorContents(armor);
            }

            player.updateInventory();

        } catch (Exception e) {

            plugin.getLogger().severe(
                    "Inventory van "
                            + player.getName()
                            + " kon niet worden geladen."
            );

            e.printStackTrace();
        }
    }

    private String serialize(ItemStack[] items) throws IOException {

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        try (BukkitObjectOutputStream output =
                     new BukkitObjectOutputStream(outputStream)) {

            output.writeObject(items);
        }

        return Base64.getEncoder().encodeToString(
                outputStream.toByteArray()
        );
    }

    private ItemStack[] deserialize(String data)
            throws IOException, ClassNotFoundException {

        if (data == null || data.isEmpty()) {
            return null;
        }

        byte[] bytes =
                Base64.getDecoder().decode(data);

        try (BukkitObjectInputStream input =
                     new BukkitObjectInputStream(
                             new ByteArrayInputStream(bytes)
                     )) {

            return (ItemStack[]) input.readObject();
        }
    }
}
