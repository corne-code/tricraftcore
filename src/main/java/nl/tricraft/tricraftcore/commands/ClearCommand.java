package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCommand implements CommandExecutor {

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

        if (!player.hasPermission("tricraft.clear")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om je inventory te legen."
            );
            return true;
        }

        player.getInventory().clear();

        player.sendMessage(
                ChatColor.GREEN
                        + "Je inventory is geleegd."
        );

        return true;
    }
}
