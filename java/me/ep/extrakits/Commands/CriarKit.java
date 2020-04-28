package me.ep.extrakits.Commands;

import me.ep.extrakits.ExtraKits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CriarKit implements CommandExecutor {

    ExtraKits pl;
    public CriarKit(ExtraKits pl){
        this.pl = pl;
        Bukkit.getPluginCommand("criarkit").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (cmd.getName().equalsIgnoreCase("criarkit")){
            String prefixo = pl.getConfig().getString("Prefixo");
            if (!(sender instanceof Player)){

            }

            Player p = (Player)sender;

            if (p.hasPermission("extrakits.admin")){

                if (args.length == 1) {
                    p.sendMessage(ChatColor.RED + "Uso correto: /criarkit <nome> <cd>");
                }else if (args.length == 2){

                    String kit = args[0];

                    if (!pl.kitManager.kitExists(kit)){
                        int cd = Integer.parseInt(args[1]);
                        pl.kitManager.createKit(p, kit, cd);

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                prefixo + pl.getConfig().getString("Criado")
                                        .replace("{kit}", kit)));

                    }else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                prefixo + pl.getConfig().getString("Ja_Existe")
                                        .replace("{kit}", kit)));
                    }

                }else {
                    p.sendMessage(ChatColor.RED + "Uso correto: /criarkit <nome> <cd>");
                }

            }else {
                p.sendMessage(ChatColor.RED + "Voce nao tem permissao para utilizar este comando");
            }

        }


        return false;
    }
}
