package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

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

        if (!player.hasPermission("tricraft.heal")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om heal te gebruiken."
            );
            return true;
        }

        player.setHealth(
                player.getMaxHealth()
        );

        player.setFireTicks(0);

        player.sendMessage(
                ChatColor.GREEN
                        + "Je bent volledig geheald."
        );

        return true;
    }
}
