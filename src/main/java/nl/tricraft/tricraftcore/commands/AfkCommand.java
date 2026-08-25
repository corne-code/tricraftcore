package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AfkCommand implements CommandExecutor {

    private final Set<UUID> afkPlayers = new HashSet<>();

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

        UUID uuid = player.getUniqueId();

        if (afkPlayers.contains(uuid)) {

            afkPlayers.remove(uuid);

            player.setPlayerListName(
                    player.getName()
            );

            player.sendMessage(
                    ChatColor.GREEN
                            + "Je bent niet langer AFK."
            );

        } else {

            afkPlayers.add(uuid);

            player.setPlayerListName(
                    ChatColor.GRAY
                            + "[AFK] "
                            + player.getName()
            );

            player.sendMessage(
                    ChatColor.YELLOW
                            + "Je bent nu AFK."
            );
        }

        return true;
    }

    public boolean isAfk(Player player) {
        return afkPlayers.contains(
                player.getUniqueId()
        );
    }

    public void removeAfk(Player player) {
        afkPlayers.remove(
                player.getUniqueId()
        );

        player.setPlayerListName(
                player.getName()
        );
    }
}
