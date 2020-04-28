package me.ep.extrakits.Commands;

import me.ep.extrakits.ExtraKits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Kit implements CommandExecutor {

    ExtraKits pl;
    public Kit(ExtraKits pl){
        this.pl = pl;
        Bukkit.getPluginCommand("kit").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (cmd.getName().equalsIgnoreCase("kit")){

            if (!(sender instanceof Player)){

            }

            Player p = (Player)sender;

            if (args.length == 0){

                p.sendMessage("Seus Kits: " + pl.kitManager.kitsList());

            }else if (args.length == 1){

                String kit = args[0];
                int delay = pl.kitManager.getDelay(kit);
                String prefixo = pl.getConfig().getString("Prefixo");

                if (pl.kitManager.kitExists(kit)){
                    if (p.hasPermission("extrakits.kit." + kit)){
                        if (!pl.c.temDelay(p.getName() + "." + kit)){
                            pl.c.addDelay(p.getName() + "." + kit);
                            pl.kitManager.darKit(p, args[0]);


                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    prefixo + pl.getConfig().getString("Pegou")
                                            .replace("{kit}", kit)));

                        }else {
                            if (pl.c.acabouDelay(p.getName() + "." + kit, TimeUnit.SECONDS.toMillis(delay))){
                                pl.c.removeDelay(p.getName() + "." + kit);
                                pl.c.addDelay(p.getName() + "." + kit);
                                pl.kitManager.darKit(p, args[0]);
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        prefixo + pl.getConfig().getString("Pegou")
                                                .replace("{kit}", kit)));
                            }else {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        prefixo + pl.getConfig().getString("Em_Delay")
                                                .replace("{kit}", kit)
                                                .replace("{delay}", pl.c.getDelayString(p.getName() + "." + kit,
                                                        TimeUnit.SECONDS.toMillis(delay)))));
                            }
                        }
                    }else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                prefixo + pl.getConfig().getString("Sem_Permissao")
                                        .replace("{kit}", kit)));
                    }
                }

            }else {
                p.sendMessage(ChatColor.RED + "Uso correto: /kit <nome>");
            }

        }


        return false;
    }
}
