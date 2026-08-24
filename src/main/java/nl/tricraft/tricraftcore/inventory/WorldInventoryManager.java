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

    private static final String SHARED_INVENTORY = "__SHARED__";

    private final TricraftCore plugin;
    private final DatabaseManager database;

    public WorldInventoryManager(TricraftCore plugin) {
        this.plugin = plugin;
        this.database = plugin.getDatabaseManager();
    }

    /**
     * Controleert of een wereld een eigen inventory heeft.
     */
    private boolean hasSeparateInventory(World world) {

        if (!plugin.getConfig().getBoolean(
                "world-inventory.enabled",
                true
        )) {
            return false;
        }

        return plugin.getConfig().getStringList(
                "world-inventory.worlds"
        ).contains(world.getName());
    }

    /**
     * Bepaalt waar de inventory van een wereld wordt opgeslagen.
     */
    private String getInventoryKey(World world) {

        if (hasSeparateInventory(world)) {
            return world.getName();
        }

        return SHARED_INVENTORY;
    }

    /**
     * Speler komt de server binnen.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTask(plugin, () -> {

            loadInventory(
                    player,
                    player.getWorld()
            );
        });
    }

    /**
     * Speler verandert van wereld.
     */
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {

        Player player = event.getPlayer();

        World oldWorld = event.getFrom();
        World newWorld = player.getWorld();

        // Oude inventory opslaan
        saveInventory(
                player,
                oldWorld
        );

        // Nieuwe inventory laden
        Bukkit.getScheduler().runTask(plugin, () -> {

            loadInventory(
                    player,
                    newWorld
            );
        });
    }

    /**
     * Speler verlaat de server.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        saveInventory(
                player,
                player.getWorld()
        );
    }

    /**
     * Wordt gebruikt wanneer de server wordt afgesloten.
     */
    public void savePlayer(Player player) {

        saveInventory(
                player,
                player.getWorld()
        );
    }

    /**
     * Inventory opslaan.
     */
    private void saveInventory(
            Player player,
            World world
    ) {

        try {

            UUID uuid = player.getUniqueId();

            String inventoryKey =
                    getInventoryKey(world);

            String inventory =
                    serialize(
                            player.getInventory().getContents()
                    );

            String armor =
                    serialize(
                            player.getInventory().getArmorContents()
                    );

            database.saveInventory(
                    uuid,
                    inventoryKey,
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

    /**
     * Inventory laden.
     */
    private void loadInventory(
            Player player,
            World world
    ) {

        try {

            UUID uuid = player.getUniqueId();

            String inventoryKey =
                    getInventoryKey(world);

            DatabaseManager.InventoryData data =
                    database.loadInventory(
                            uuid,
                            inventoryKey
                    );

            // Eerst huidige inventory verwijderen.
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);

            // Als er nog geen inventory bestaat,
            // krijgt de speler een lege inventory.
            if (data == null) {

                player.updateInventory();

                return;
            }

            ItemStack[] contents =
                    deserialize(
                            data.inventory()
                    );

            ItemStack[] armor =
                    deserialize(
                            data.armor()
                    );

            if (contents != null) {

                player.getInventory().setContents(
                        contents
                );
            }

            if (armor != null) {

                player.getInventory().setArmorContents(
                        armor
                );
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

    /**
     * ItemStack[] omzetten naar tekst voor SQLite.
     */
    private String serialize(
            ItemStack[] items
    ) throws IOException {

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        try (BukkitObjectOutputStream output =
                     new BukkitObjectOutputStream(
                             outputStream
                     )) {

            output.writeObject(items);
        }

        return Base64.getEncoder().encodeToString(
                outputStream.toByteArray()
        );
    }

    /**
     * Tekst uit SQLite terug omzetten naar ItemStack[].
     */
    private ItemStack[] deserialize(
            String data
    ) throws IOException, ClassNotFoundException {

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
