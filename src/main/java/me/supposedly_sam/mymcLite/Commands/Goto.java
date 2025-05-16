package me.supposedly_sam.mymcLite.Commands;

import me.supposedly_sam.mymcLite.Events.JoinEvent;
import me.supposedly_sam.mymcLite.MymcLite;
import me.supposedly_sam.mymcLite.Utils.HelperFunctions;
import me.supposedly_sam.mymcLite.Utils.Responses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Goto implements CommandExecutor, TabCompleter {

    private final Plugin plugin;
    public Goto(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
//        ArrayList<String> serverList = JoinEvent.getServers();
// TODO: Rectify the getServers race condition issue
        if (sender instanceof Player p) {
            if (args.length == 1) {
//                if (serverList == null || serverList.isEmpty()) {
//                    JoinEvent.sendGetServers(p);
//                    serverList = JoinEvent.getServers();
//
//                    if (serverList == null) {
//                        plugin.getLogger().warning("Server list is null");
//                        return true;
//                    }
//                }
//
                String server = args[0];
//                if (!serverList.contains(server)) {
//                    p.sendMessage(ChatColor.RED + "Server (" + server +  ") does not exist");
//                    return true;
//                }
                connectToServer(p, server);
            } else if (args.length == 2) {
                if (!p.hasPermission("mymclite.goto.others")) {
                    Responses.sendNotAuthorized(p);
                    return true;
                }

                String server = args[0];
                String targetName = args[1];

//                if (!serverList.contains(server)) {
//                    p.sendMessage(ChatColor.RED + "Server (" + server +  ") does not exist");
//                    return true;
//                }

                if (!HelperFunctions.isPlayerOnline(p, targetName)) {
                    return true;
                }

                Player target = Bukkit.getPlayerExact(targetName);
                connectToServer(target, server);
            } else {
                Responses.sendCommandUsage(p, "/goto <server> [player]");
            }
        }
        return true;
    }

    private void connectToServer(Player player, String server){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);

            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());

            out.close();
            b.close();
        } catch (IOException e) {
            plugin.getLogger().info(e.getMessage());
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
//        if (args.length == 1) {
//            ArrayList<String> servers = JoinEvent.getServers();
//            if (servers != null) {
//                return servers.stream()
//                        .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
//                        .collect(Collectors.toList());
//            }
//        } else if (args.length == 2) {
//            return Bukkit.getOnlinePlayers().stream()
//                    .map(Player::getName)
//                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
//                    .collect(Collectors.toList());
//        }

        return Collections.emptyList();
    }
}
