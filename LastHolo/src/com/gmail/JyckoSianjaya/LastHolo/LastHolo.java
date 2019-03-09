package com.gmail.JyckoSianjaya.LastHolo;

import java.io.File;









import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;

import com.cyr1en.cp.CommandPrompter;
import com.gmail.JyckoSianjaya.LastHolo.Commands.LHCommand;
import com.gmail.JyckoSianjaya.LastHolo.Listeners.LHListener;
import com.gmail.JyckoSianjaya.LastHolo.NMS.NMSWrapper;
import com.gmail.JyckoSianjaya.LastHolo.Runnables.SimpleRunnable;
import com.gmail.JyckoSianjaya.LastHolo.Storage.CacheStorage;
import com.gmail.JyckoSianjaya.LastHolo.Storage.DataStorage;
import com.gmail.JyckoSianjaya.LastHolo.Storage.DataStorage.WGVersion;
import com.gmail.JyckoSianjaya.LastHolo.Utility.Utility;
import com.gmail.JyckoSianjaya.LastHolo.Utility.WG_12;
import com.gmail.JyckoSianjaya.LastHolo.Utility.WG_13;
import com.gmail.JyckoSianjaya.LastHolo.Utility.XSound;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplLoader;
public class LastHolo extends JavaPlugin {
  // LATEST VERSION : https://pastebin.com/8xGMfKZn (modify this when update plugin)
	HashMap<UUID,Boolean> toggle = new HashMap<>();
	private static LastHolo instance;
	private DataStorage storage;
	private SimpleRunnable run;
	private NMSWrapper nmswrapper;
	  HashMap<Integer, String> pages = new HashMap<>();
	  File blist;
  public void PlaySound(Sound sound, Float volu, Float pitu, World w, Location g) {
	  w.playSound(g, sound, volu, pitu);
  }

  public static LastHolo getInstance() { return instance; }
  private boolean isCmdPromptEnable() {
	  return this.getServer().getPluginManager().isPluginEnabled("CommandPrompter");
  }
  public NMSWrapper getNMS() {
	  return this.nmswrapper;
  }
	@Override
	public void onEnable() {
		instance = this;
		Metrics metrics = new Metrics(this);

		this.getConfig().options().copyDefaults(true);
		saveConfig();
		storage = DataStorage.getInstance();
		URL url = null;
		String str;
		String ver;
		Scanner scanner = null;
		try {
		url = new URL("https://pastebin.com/raw/8xGMfKZn");
		scanner = new Scanner(url.openStream());
		str = scanner.nextLine();
		PluginDescriptionFile lastholo = this.getDescription();
		ver = lastholo.getVersion();
		scanner.close();
		if (!str.equals(ver)) {
			Utility.sendConsole("&6&l< UPDATE > &eNew update for LastHolo is available. Check them out in '&fhttps://bit.ly/2IY9hDb&a'");
			
		}
		} catch (Exception e) {
			Utility.sendConsole("Couldn't get the latest version for LastHolo.");
		}
		blist = new File(this.getDataFolder(), "datastorage" + File.separator);
		if (!blist.exists()) {
			blist.mkdir();
		}
		File datafolder = new File(this.getDataFolder(), "datastorage"  + File.separator);
		if (!datafolder.exists()) {
			datafolder.mkdir();
		}
		File blacklist = new File(this.getDataFolder(), "datastorage" + File.separator + "blacklisted-players.yml");
		if (!blacklist.exists()) {
			try {
				blacklist.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (DataStorage.getInstance().isWorldGuardEnabled()) {
			Utility.sendConsole("&9[LastHolo] &7Hooked into &eWorldGuard.");
		}
		else {
			Utility.sendConsole("&9[LastHolo] &7WorldGuard hook &cfailed&7.");
		}
			Utility.sendConsole("&9[LastHolo] &7Plugin &asuccessfully enabled&7, have a fancy chat there, enjoy ;)");
			getServer().getPluginManager().registerEvents(new LHListener(), this);
			this.getCommand("lastholo").setExecutor(new LHCommand());
			run = SimpleRunnable.getInstance();
			this.nmswrapper = new NMSWrapper(this);
	}
	public String getDataStorage() {
		return (this.getDataFolder() + File.separator + "datastorage" + File.separator);
	
	}
	public boolean shouldEnable() {
		return this.getServer().getPluginManager().isPluginEnabled("HolographicDisplays");
	}
	@Override
	public void onDisable() {
    // TODO : Make more things
		Utility.sendConsole("LastHolo disabled, saving up all player's data.");
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		CacheStorage.getInstance().removeAllHolo();
		for (Player p : players) {
			UUID uuid = p.getUniqueId();
			File f = new File(this.getDataFolder(), "datastorage" + File.separator + uuid + ".yml");
			YamlConfiguration yaml = YamlConfiguration.loadConfiguration(f);
			Boolean bool = null;
			if (toggle.get(uuid) == null) return;
			yaml.set("toggled", bool);
			try {
				yaml.save(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				}
		}
		DataStorage.getInstance().saveBlacklist();
	}
}
        	  
/*
Heloo! I'm sure you had just decompile this plugin!
be sure not to abuse!
*/