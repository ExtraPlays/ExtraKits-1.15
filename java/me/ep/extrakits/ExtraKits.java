package me.ep.extrakits;

import me.ep.extrakits.Commands.ApagarKit;
import me.ep.extrakits.Commands.CriarKit;
import me.ep.extrakits.Commands.Kit;
import me.ep.extrakits.Cooldown.Cooldown;
import me.ep.extrakits.Kits.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ExtraKits extends JavaPlugin {

    public KitManager kitManager;
    public Cooldown c;

    @Override
    public void onEnable() {


        saveDefaultConfig();
        this.kitManager = new KitManager(this);

        new CriarKit(this);
        new Kit(this);
        new ApagarKit(this);

        File f = new File(getDataFolder(), "cooldown.yml");
        if (!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {}
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        c = new Cooldown(config, f);


    }

    @Override
    public void onDisable() {

    }
}
