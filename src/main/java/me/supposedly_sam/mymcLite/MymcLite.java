package me.supposedly_sam.mymcLite;

import com.google.common.collect.Iterables;
import me.supposedly_sam.mymcLite.Commands.*;
import me.supposedly_sam.mymcLite.Events.*;
import me.supposedly_sam.mymcLite.Utils.PluginMessages;
import me.supposedly_sam.mymcLite.Utils.SpawnFile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public final class MymcLite extends JavaPlugin {

    private final PluginMessages pluginMessages = new PluginMessages();

    @Override
    public void onEnable() {

        // Config.yml
        saveDefaultConfig();
        // Spawn.yml
        new SpawnFile(this);

        // Channel Registers
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", pluginMessages);

        // Event Registers
        registerListener(new JoinEvent(this));
        registerListener(new LeaveEvent(this));
        registerListener(new BuildEvents(this));
        registerListener(new Chat(this));
        registerListener(new DisabledCommands(this));
        registerListener(new InventoryClick(this));

        // Command Registers
        registerCommand("mlreload", new Reload(this));
        registerCommand("setspawn", new SetSpawn(this));
        registerCommand("spawn", new Spawn(this));
        registerCommand("build", new Build());
        registerCommand("goto", new Goto(this));
        registerCommand("gameselector", new GameSelector(this));

    }

    @Override
    public void onDisable() {
        // Unregistering Plugin Channels
        Bukkit.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", pluginMessages);
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
