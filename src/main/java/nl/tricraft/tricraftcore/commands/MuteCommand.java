package nl.tricraft.tricraftcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MuteCommand implements CommandExecutor {

    private final Set<UUID> mutedPlayers = new HashSet<>();

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("tricraft.mute")) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om dit te gebruiken."
            );
            return true;
        }

        String commandName = command.getName().toLowerCase();

        if (args.length != 1) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /"
                            + commandName
                            + " <speler>"
            );

            return true;
        }

        Player target =
                Bukkit.getPlayerExact(args[0]);

        if (target == null) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Deze speler is niet online."
            );

            return true;
        }

        UUID uuid =
                target.getUniqueId();

        if (commandName.equals("mute")) {

            if (mutedPlayers.contains(uuid)) {

                sender.sendMessage(
                        ChatColor.YELLOW
                                + target.getName()
                                + " is al gemute."
                );

                return true;
            }

            mutedPlayers.add(uuid);

            target.sendMessage(
                    ChatColor.RED
                            + "Je bent gemute."
            );

            sender.sendMessage(
                    ChatColor.GREEN
                            + target.getName()
                            + " is gemute."
            );

            return true;
        }

        if (commandName.equals("unmute")) {

            if (!mutedPlayers.contains(uuid)) {

                sender.sendMessage(
                        ChatColor.YELLOW
                                + target.getName()
                                + " is niet gemute."
                );

                return true;
            }

            mutedPlayers.remove(uuid);

            target.sendMessage(
                    ChatColor.GREEN
                            + "Je bent niet meer gemute."
            );

            sender.sendMessage(
                    ChatColor.GREEN
                            + target.getName()
                            + " is niet meer gemute."
            );

            return true;
        }

        return false;
    }

    public boolean isMuted(Player player) {

        return mutedPlayers.contains(
                player.getUniqueId()
        );
    }
}
