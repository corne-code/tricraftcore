package nl.tricraft.tricraftcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("tricraft.kick")) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om spelers te kicken."
            );
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /kick <speler> [reden]"
            );
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Deze speler is niet online."
            );
            return true;
        }

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

        target.kickPlayer(
                ChatColor.RED
                        + "Je bent van de server gekickt.\n\n"
                        + ChatColor.GRAY
                        + "Reden: "
                        + ChatColor.WHITE
                        + reason
        );

        sender.sendMessage(
                ChatColor.GREEN
                        + target.getName()
                        + " is gekickt."
        );

        return true;
    }
}
