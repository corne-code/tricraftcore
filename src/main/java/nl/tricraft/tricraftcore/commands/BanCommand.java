package nl.tricraft.tricraftcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("tricraft.ban")) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om spelers te bannen."
            );
            return true;
        }

        String commandName = command.getName().toLowerCase();

        // =========================
        // /ban
        // =========================

        if (commandName.equals("ban")) {

            if (args.length < 1) {
                sender.sendMessage(
                        ChatColor.RED
                                + "Gebruik: /ban <speler> [reden]"
                );
                return true;
            }

            OfflinePlayer target =
                    Bukkit.getOfflinePlayer(args[0]);

            String reason = "Geen reden opgegeven.";

            if (args.length > 1) {
                reason = String.join(
                        " ",
                        java.util.Arrays.copyOfRange(
                                args,
                                1,
                                args.length
                        )
                );
            }

            target.setBanned(true);

            Player onlineTarget =
                    target.getPlayer();

            if (onlineTarget != null) {

                onlineTarget.kickPlayer(
                        ChatColor.RED
                                + "Je bent verbannen.\n\n"
                                + ChatColor.GRAY
                                + "Reden: "
                                + ChatColor.WHITE
                                + reason
                );
            }

            sender.sendMessage(
                    ChatColor.GREEN
                            + target.getName()
                            + " is verbannen."
            );

            return true;
        }

        // =========================
        // /unban
        // =========================

        if (commandName.equals("unban")) {

            if (args.length != 1) {
                sender.sendMessage(
                        ChatColor.RED
                                + "Gebruik: /unban <speler>"
                );
                return true;
            }

            OfflinePlayer target =
                    Bukkit.getOfflinePlayer(args[0]);

            if (!target.isBanned()) {
                sender.sendMessage(
                        ChatColor.YELLOW
                                + target.getName()
                                + " is niet verbannen."
                );
                return true;
            }

            target.setBanned(false);

            sender.sendMessage(
                    ChatColor.GREEN
                            + target.getName()
                            + " is niet meer verbannen."
            );

            return true;
        }

        return false;
    }
}
