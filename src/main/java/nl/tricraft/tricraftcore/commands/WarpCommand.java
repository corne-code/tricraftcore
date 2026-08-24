package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

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

        String commandName = command.getName().toLowerCase();

        switch (commandName) {

            case "warp":

                if (args.length != 1) {
                    player.sendMessage(
                            ChatColor.RED
                                    + "Gebruik: /warp <naam>"
                    );
                    return true;
                }

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Warp-systeem wordt binnenkort gekoppeld."
                );

                break;

            case "warps":

                player.sendMessage(
                        ChatColor.YELLOW
                                + "De beschikbare warps worden binnenkort weergegeven."
                );

                break;

            case "setwarp":

                if (args.length != 1) {
                    player.sendMessage(
                            ChatColor.RED
                                    + "Gebruik: /setwarp <naam>"
                    );
                    return true;
                }

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Warp wordt opgeslagen zodra de database-koppeling klaar is."
                );

                break;

            case "delwarp":

                if (args.length != 1) {
                    player.sendMessage(
                            ChatColor.RED
                                    + "Gebruik: /delwarp <naam>"
                    );
                    return true;
                }

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Warp verwijderen wordt binnenkort toegevoegd."
                );

                break;

            default:

                player.sendMessage(
                        ChatColor.RED
                                + "Onbekend warp-commando."
                );
        }

        return true;
    }
}
