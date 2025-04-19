package me.supposedly_sam.mymcLite.Utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class SpawnFile {

    private static File file;
    private static FileConfiguration spawnFile;
    private final Plugin plugin;

    public SpawnFile(Plugin plugin){
        this.plugin = plugin;

        setup();
        try {
            saveSpawnFile();
        } catch (IOException ex) {
            ex.printStackTrace();
            plugin.getLogger().warning("Error saving spawn.yml");
        }
    }

    public void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("MymcLite").getDataFolder(), "spawn.yml");
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException ex){
                ex.printStackTrace();
                plugin.getLogger().warning("Error while creating spawn.yml!");
            }
        }
        spawnFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveSpawnFile() throws IOException{
        spawnFile.save(file);
    }

    public static FileConfiguration getSpawnFile(){
        return spawnFile;
    }
}
