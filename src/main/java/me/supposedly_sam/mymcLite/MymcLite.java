package me.supposedly_sam.mymcLite;

import me.supposedly_sam.mymcLite.Commands.Build;
import me.supposedly_sam.mymcLite.Commands.SetSpawn;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MymcLite extends JavaPlugin {

    @Override
    public void onEnable() {

        // Config.yml
        saveDefaultConfig();

        // Command Registers
        registerCommand("setspawn", new SetSpawn(this));
        registerCommand("build", new Build());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // Command/Tab completer register
    private void registerCommand(String name, Object handler) {
        PluginCommand command = getCommand(name);
        if (command != null) {
            if (handler instanceof CommandExecutor executor)
                command.setExecutor(executor);
            if (handler instanceof TabCompleter completer)
                command.setTabCompleter(completer);
        }
    }

    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
