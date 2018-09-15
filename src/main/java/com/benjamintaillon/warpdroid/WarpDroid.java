package com.benjamintaillon.warpdroid;

import com.benjamintaillon.warpdroid.subclasses.CustomConfig;
import com.benjamintaillon.warpdroid.subclasses.Home;
import com.benjamintaillon.warpdroid.subclasses.Warps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.management.OperationsException;
import java.io.IOException;
import java.util.*;

public final class WarpDroid extends JavaPlugin implements Listener {

    private List<Tameable> getPets(Player player, Class classMatch) {
        List<Tameable> pets = new ArrayList<>();
        Tameable tameable;
        for (World world : getServer().getWorlds()) {
            for (LivingEntity ent : world.getLivingEntities()) {
                if (ent instanceof Tameable && classMatch.isInstance(ent)) {
                    tameable = (Tameable)ent;
                    if (tameable.isTamed() && tameable.getOwner().getUniqueId() == player.getUniqueId()) {
                        pets.add(tameable);
                    }
                }
            }
        }
        return pets;
    }

    private void makePetsPersistent() {
        Tameable tameable;
        for (World world : getServer().getWorlds()) {
            for (Entity ent : world.getEntities()) {
                if (ent instanceof Tameable) {
                    tameable = (Tameable)ent;
                    if (tameable.isTamed()) {
                        ((LivingEntity)ent).setRemoveWhenFarAway(false);
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Globals.plugin = this;
        Globals.homesConfig = new CustomConfig("homes.yml");
        Globals.warpsConfig = new CustomConfig("warps.yml");
        Globals.deathLocations = new HashMap<>();
        makePetsPersistent();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String strCMD = cmd.getName().toUpperCase();

        switch (strCMD) {
            case "BACK":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Location deathLocation = Globals.deathLocations.get(player.getUniqueId());
                    if (deathLocation != null) {
                        player.teleport(deathLocation);
                        sender.sendMessage("Teleported you to your death location.");
                    } else {
                        sender.sendMessage("Your death location could not be found.");
                    }
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "CALL":
                if (sender instanceof Player) {
                    if (args.length < 1) {
                        return false;
                    }
                    Player player = (Player) sender;
                    List<Tameable> pets = getPets(player, Tameable.class);
                    for (int i = pets.size() - 1; i >= 0; i--) {
                        if (pets.get(i).getCustomName() == null || !pets.get(i).getCustomName().toUpperCase().startsWith(args[0].toUpperCase())) {
                            pets.remove(i);
                        }
                    }
                    for (Tameable pet : pets) {
                        pet.teleport(player.getLocation());
                    }
                    sender.sendMessage(String.format("Teleported %s pets to your location.", pets.size()));
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "CALLANIMALS":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    List<Tameable> pets = getPets(player, Tameable.class);
                    for (Tameable pet : pets) {
                        pet.teleport(player.getLocation());
                    }
                    sender.sendMessage(String.format("Teleported %s pets to your location.", pets.size()));
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "CALLHORSES":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    List<Tameable> pets = getPets(player, AbstractHorse.class);
                    for (Tameable pet : pets) {
                        pet.teleport(player.getLocation());
                    }
                    sender.sendMessage(String.format("Teleported %s pets to your location.", pets.size()));
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "CALLDOGS":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    List<Tameable> pets = getPets(player, Wolf.class);
                    for (Tameable pet : pets) {
                        pet.teleport(player.getLocation());
                    }
                    sender.sendMessage(String.format("Teleported %s pets to your location.", pets.size()));
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "CALLCATS":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    List<Tameable> pets = getPets(player, Ocelot.class);
                    for (Tameable pet : pets) {
                        pet.teleport(player.getLocation());
                    }
                    sender.sendMessage(String.format("Teleported %s pets to your location.", pets.size()));
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "CALLBIRDS":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    List<Tameable> pets = getPets(player, Parrot.class);
                    for (Tameable pet : pets) {
                        pet.teleport(player.getLocation());
                    }
                    sender.sendMessage(String.format("Teleported %s pets to your location.", pets.size()));
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "HOME":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Location location = Home.getHomeLocation(player);
                    player.teleport(location);
                    player.sendMessage("Teleported home.");
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "SETHOME":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    try {
                        Home.setHomeLocation(player);
                        player.sendMessage("Moved home to current location.");
                    } catch (IOException e) {
                        player.sendMessage(ChatColor.RED + "An error occurred saving the location.");
                    }
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "WARP":
                if (args.length > 0) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        Location location = Warps.getWarpLocation(args[0]);
                        if (location != null) {
                            player.teleport(location);
                            player.sendMessage("Teleported to \"" + args[0] + "\".");
                        }
                        else {
                            sender.sendMessage("Warp \"" + args[0] + "\" does not exist.");
                        }
                    } else {
                        sender.sendMessage("This command can only be executed by a player.");
                    }
                    return true;
                }
                else
                {
                    strCMD = "WARPLIST";
                }
            case "WARPLIST":
                String[] warps = Warps.getWarps();
                sender.sendMessage(ChatColor.YELLOW + "== Warps list ==");
                for (int i = 0; i < warps.length; i++) {
                    sender.sendMessage(ChatColor.YELLOW + String.format("(%s/%s): %s", i + 1, warps.length, warps[i]));
                }
                return true;
            case "ADDWARP":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length > 0) {
                        try {
                            Warps.addWarp(args[0], player.getLocation());
                            player.sendMessage("Created warp \"" + args[0] + "\" at your location.");
                        } catch (IOException e) {
                            player.sendMessage(ChatColor.RED + "An error occurred saving the location.");
                        } catch (OperationsException e) {
                            sender.sendMessage("Warp \"" + args[0] + "\" already exists.");
                        }
                    }
                    else {
                        return false;
                    }
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "SETWARP":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length > 0) {
                        try {
                            Warps.setWarp(args[0], player.getLocation());
                            player.sendMessage("Moved warp \"" + args[0] + "\" to your location.");
                        } catch (IOException e) {
                            player.sendMessage(ChatColor.RED + "An error occurred saving the location.");
                        } catch (OperationsException e) {
                            sender.sendMessage("Warp \"" + args[0] + "\" does not exist.");
                        }
                    }
                    else {
                        return false;
                    }
                } else {
                    sender.sendMessage("This command can only be executed by a player.");
                }
                return true;
            case "REMOVEWARP":
                if (args.length > 0) {
                    try {
                        Warps.removeWarp(args[0]);
                        sender.sendMessage("Removed warp \"" + args[0] + "\".");
                    } catch (IOException e) {
                        sender.sendMessage(ChatColor.RED + "An error occurred saving the location.");
                    } catch (OperationsException e) {
                        sender.sendMessage("Warp \"" + args[0] + "\" does not exist.");
                    }
                }
                else {
                    return false;
                }
                return true;
        }
        return false;
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Globals.deathLocations.put(player.getUniqueId(), player.getLocation());
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onPetTamed(EntityTameEvent event) {
        event.getEntity().setRemoveWhenFarAway(false); // Make the pet persistent.
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Tameable pet;
        for (Entity ent : event.getChunk().getEntities()) {
            if (ent instanceof Tameable) {
                pet = (Tameable)ent;
                if (pet.isTamed()) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
