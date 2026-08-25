package nl.tricraft.tricraftcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvseeCommand implements CommandExecutor {

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

        if (!player.hasPermission("tricraft.invsee")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om invsee te gebruiken."
            );
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /invsee <speler>"
            );
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(
                    ChatColor.RED
                            + "Deze speler is niet online."
            );
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je kunt je eigen inventory niet bekijken."
            );
            return true;
        }

        Inventory gui = Bukkit.createInventory(
                null,
                54,
                ChatColor.DARK_AQUA
                        + "Inventory: "
                        + target.getName()
        );

        ItemStack[] contents =
                target.getInventory().getStorageContents();

        for (int slot = 0; slot < contents.length; slot++) {

            if (contents[slot] != null) {
                gui.setItem(
                        slot,
                        contents[slot].clone()
                );
            }
        }

        player.openInventory(gui);

        return true;
    }
}
