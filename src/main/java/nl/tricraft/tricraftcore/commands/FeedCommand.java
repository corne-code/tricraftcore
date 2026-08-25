package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

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

        if (!player.hasPermission("tricraft.feed")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om feed te gebruiken."
            );
            return true;
        }

        player.setFoodLevel(20);
        player.setSaturation(20);

        player.sendMessage(
                ChatColor.GREEN
                        + "Je honger is volledig hersteld."
        );

        return true;
    }
}
