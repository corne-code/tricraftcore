package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {

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

        if (!player.hasPermission("tricraft.time")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om de tijd te veranderen."
            );
            return true;
        }

        if (args.length != 2 || !args[0].equalsIgnoreCase("set")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /time set <day|night|ticks>"
            );
            return true;
        }

        World world = player.getWorld();
        String value = args[1].toLowerCase();

        long time;

        switch (value) {

            case "day":
                time = 1000;
                break;

            case "night":
                time = 13000;
                break;

            default:
                try {
                    time = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    player.sendMessage(
                            ChatColor.RED
                                    + "Ongeldige tijd."
                    );
                    return true;
                }

                if (time < 0 || time > 24000) {
                    player.sendMessage(
                            ChatColor.RED
                                    + "De tijd moet tussen 0 en 24000 liggen."
                    );
                    return true;
                }
                break;
        }

        world.setTime(time);

        player.sendMessage(
                ChatColor.GREEN
                        + "De tijd is aangepast naar "
                        + ChatColor.YELLOW
                        + value
                        + ChatColor.GREEN
                        + "."
        );

        return true;
    }
}
