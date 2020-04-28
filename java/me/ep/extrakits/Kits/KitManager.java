package me.ep.extrakits.Kits;

import me.ep.extrakits.ExtraKits;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KitManager {


    private ExtraKits pl;
    private File folder, kitConfig;
    FileConfiguration config;
    public KitManager(ExtraKits pl){
        this.pl = pl;
        createFolder();
    }

    private void createFolder(){
        this.folder = new File(pl.getDataFolder() + File.separator + "kits");
        if (!folder.exists()){
            folder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("[ExtraKits+] pasta dos kits criada");
        }
    }

    private String getFolder() {
        return folder.getAbsolutePath();
    }

    public void createKit(Player p, String nome, int cooldown){
        this.kitConfig = new File(getFolder(), nome + ".yml");
        if (!kitConfig.exists()){
            try {
                kitConfig.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }


        this.config = YamlConfiguration.loadConfiguration(kitConfig);

        for (int i = 0; i < 36; i++){
            ItemStack item = p.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;

            String path = "Items." + i;

            config.set(path + ".Tipo", item.getType().toString().toLowerCase());
            config.set(path + ".Data", item.getData().getData());
            config.set(path + ".Quantidade", item.getAmount());
            config.set("Cooldown", cooldown);

            if (item.hasItemMeta()){
                if (item.getItemMeta().hasDisplayName()){
                    config.set(path + ".Nome", item.getItemMeta().getDisplayName());
                    save();
                }

                if (item.getItemMeta().hasLore()){
                    config.set(path + ".Lore", item.getItemMeta().getLore());
                    save();
                }

                if (item.getItemMeta().hasEnchants()){
                    Map<Enchantment, Integer> enchantmentMap = item.getEnchantments();
                    List<String> enchantList = new ArrayList<>();
                    for (Enchantment e : enchantmentMap.keySet()){
                        int level = enchantmentMap.get(e);
                        enchantList.add(e.getName().toLowerCase() + ":" + level);
                    }
                    config.set(path + ".Encantamentos", enchantList);
                    save();
                }
            }

            save();



        }


    }


    public void darKit(Player p, String nome){


        this.kitConfig = new File(getFolder(), nome + ".yml");
        this.config = YamlConfiguration.loadConfiguration(kitConfig);
        ConfigurationSection section = this.config.getConfigurationSection("Items");

        for (String s : section.getKeys(false)){

            String path = "Items." + s + ".";
            String tipo = config.getString(path + "Tipo");
            byte data = (byte)config.getInt(path + "Data");
            String name = config.getString(path + "Nome");
            List<String> lore = config.getStringList(path + "Lore");
            List<String> enchants = config.getStringList(path + "Encantamentos");
            int quantidade = config.getInt(path + "Quantidade");

            ItemStack item = new ItemStack(Material.matchMaterial(tipo.toUpperCase()), quantidade, (short)data);
            ItemMeta meta = item.getItemMeta();

            if (meta == null) return;
            if (name != null) meta.setDisplayName(name);
            if (lore != null) meta.setLore(lore);
            if (enchants != null) {
                for (String e : enchants){
                    String[] split = e.split(":");
                    meta.addEnchant(Enchantment.getByName(split[0].toUpperCase()), Integer.parseInt(split[1]), true);
                }
            }

            item.setItemMeta(meta);
            p.getInventory().addItem(item);



        }

    }

    public void deletekit(String nome){
        this.kitConfig = new File(getFolder(), nome + ".yml");
        if (kitConfig.exists()){
            kitConfig.delete();
        }
    }

    public boolean kitExists(String nome){

        this.kitConfig = new File(getFolder(), nome + ".yml");

        return kitConfig.exists();

    }

    public String kitsList(){

        StringBuilder kits = new StringBuilder();

        File[] f = new File(getFolder()).listFiles();

        for (int i = 0; i < f.length; i++){
            kits.append(f[i].getName().replace(".yml", "")).append(" ");
        }

        return kits.toString();
    }

    public void save(){
        try{
           this.config.save(this.kitConfig);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public int getDelay(String nome) {

        this.kitConfig = new File(getFolder(), nome + ".yml");
        this.config = YamlConfiguration.loadConfiguration(kitConfig);

        return config.getInt("Cooldown");
    }
}
