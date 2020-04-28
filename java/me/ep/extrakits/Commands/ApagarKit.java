package me.ep.extrakits.Commands;

import me.ep.extrakits.ExtraKits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ApagarKit implements CommandExecutor {

    ExtraKits pl;
    public ApagarKit(ExtraKits pl){
        this.pl = pl;
        Bukkit.getPluginCommand("apagarkit").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (cmd.getName().equalsIgnoreCase("apagarkit")){

            String prefixo = pl.getConfig().getString("Prefixo");

            if (!(sender instanceof Player)){

            }

            Player p = (Player)sender;

            if (p.hasPermission("extrakits.admin")) {

                if (args.length == 1) {

                    String kit = args[0];

                    if (pl.kitManager.kitExists(kit)) {

                        pl.kitManager.deletekit(kit);

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                prefixo + pl.getConfig().getString("Apagou")
                                        .replace("{kit}", kit)));

                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                prefixo + pl.getConfig().getString("Nao_Existe")
                                        .replace("{kit}", kit)));
                    }

                } else {
                    p.sendMessage(ChatColor.RED + "Uso correto: /apagarkit <nome>");
                }
            }else {
                p.sendMessage(ChatColor.RED + "Voce nao tem permissao para utilizar este comando");
            }

        }


        return false;
    }
}
