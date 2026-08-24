package nl.tricraft.tricraftcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("tricraft.teleport")) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming voor dit commando."
            );
            return true;
        }

        if (args.length != 1 && args.length != 2) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Gebruik:"
            );

            sender.sendMessage(
                    ChatColor.GRAY
                            + "/tp <speler>"
            );

            sender.sendMessage(
                    ChatColor.GRAY
                            + "/tp <speler> <speler>"
            );

            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Speler "
                            + ChatColor.YELLOW
                            + args[0]
                            + ChatColor.RED
                            + " is niet online."
            );
            return true;
        }

        // /tp <speler>
        // Teleporteer jezelf naar de speler.
        if (args.length == 1) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(
                        ChatColor.RED
                                + "Console moet twee spelers opgeven."
                );
                return true;
            }

            player.teleport(target);

            player.sendMessage(
                    ChatColor.GREEN
                            + "Je bent naar "
                            + ChatColor.YELLOW
                            + target.getName()
                            + ChatColor.GREEN
                            + " geteleporteerd."
            );

            return true;
        }

        // /tp <speler> <speler>
        // Teleporteer speler 1 naar speler 2.
        Player destination =
                Bukkit.getPlayerExact(args[1]);

        if (destination == null) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Speler "
                            + ChatColor.YELLOW
                            + args[1]
                            + ChatColor.RED
                            + " is niet online."
            );
            return true;
        }

        target.teleport(destination);

        sender.sendMessage(
                ChatColor.GREEN
                        + target.getName()
                        + " is naar "
                        + destination.getName()
                        + " geteleporteerd."
        );

        target.sendMessage(
                ChatColor.GREEN
                        + "Je bent naar "
                        + ChatColor.YELLOW
                        + destination.getName()
                        + ChatColor.GREEN
                        + " geteleporteerd."
        );

        return true;
    }
}
