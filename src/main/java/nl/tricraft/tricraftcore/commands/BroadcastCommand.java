package nl.tricraft.tricraftcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("tricraft.broadcast")) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om dit te gebruiken."
            );
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /broadcast <bericht>"
            );
            return true;
        }

        String message = String.join(" ", args);

        Bukkit.broadcastMessage(
                ChatColor.DARK_AQUA
                        + "[Tricraft] "
                        + ChatColor.WHITE
                        + message
        );

        return true;
    }
}
