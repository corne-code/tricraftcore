package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand implements CommandExecutor {

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

        if (!player.hasPermission("tricraft.give")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om dit commando te gebruiken."
            );
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            player.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /give <item> [aantal]"
            );
            return true;
        }

        Material material =
                Material.matchMaterial(args[0]);

        if (material == null || material == Material.AIR) {
            player.sendMessage(
                    ChatColor.RED
                            + "Dit item bestaat niet."
            );
            return true;
        }

        int amount = 1;

        if (args.length == 2) {

            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(
                        ChatColor.RED
                                + "Het aantal moet een heel getal zijn."
                );
                return true;
            }

            if (amount <= 0) {
                player.sendMessage(
                        ChatColor.RED
                                + "Het aantal moet groter zijn dan 0."
                );
                return true;
            }

            if (amount > 2304) {
                player.sendMessage(
                        ChatColor.RED
                                + "Je kunt maximaal 2304 items tegelijk geven."
                );
                return true;
            }
        }

        ItemStack item =
                new ItemStack(material, amount);

        player.getInventory().addItem(item);

        player.sendMessage(
                ChatColor.GREEN
                        + "Je hebt "
                        + ChatColor.YELLOW
                        + amount
                        + "x "
                        + material.name()
                        + ChatColor.GREEN
                        + " gekregen."
        );

        return true;
    }
}
