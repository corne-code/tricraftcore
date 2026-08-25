package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackCommand implements CommandExecutor {

    private final Map<UUID, Location> previousLocations =
            new HashMap<>();

    public void saveLocation(Player player) {

        previousLocations.put(
                player.getUniqueId(),
                player.getLocation().clone()
        );
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {

            sender.sendMessage(
                    "Dit commando kan alleen door een speler worden gebruikt."
            );

            return true;
        }

        Location location =
                previousLocations.get(
                        player.getUniqueId()
                );

        if (location == null) {

            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen vorige locatie."
            );

            return true;
        }

        player.teleport(location);

        player.sendMessage(
                ChatColor.GREEN
                        + "Je bent teruggeteleporteerd naar je vorige locatie."
        );

        return true;
    }
}
