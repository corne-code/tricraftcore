package nl.tricraft.tricraftcore.commands;

import nl.tricraft.tricraftcore.TricraftCore;
import nl.tricraft.tricraftcore.economy.EconomyManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    private final TricraftCore plugin;

    public BalanceCommand(TricraftCore plugin) {
        this.plugin = plugin;
    }

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

        EconomyManager economy =
                plugin.getEconomyManager();

        double balance =
                economy.getBalance(player);

        player.sendMessage(
                ChatColor.GREEN
                        + "Je saldo: "
                        + ChatColor.GOLD
                        + economy.format(balance)
        );

        return true;
    }
}
