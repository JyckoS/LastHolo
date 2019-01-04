package com.gmail.JyckoSianjaya.LastHolo.Commands;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.gmail.JyckoSianjaya.LastHolo.LastHolo;
import com.gmail.JyckoSianjaya.LastHolo.Storage.CacheStorage;
import com.gmail.JyckoSianjaya.LastHolo.Storage.DataStorage;
import com.gmail.JyckoSianjaya.LastHolo.Utility.Utility;

public class LHCommand implements TabExecutor {
	private DataStorage data = DataStorage.getInstance();
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		   if (cmd.getName().equalsIgnoreCase("lastholo")) {
		  		Player player = null;
		    if(sender instanceof Player){
		    	player = (Player) sender;
		    }
		    	int length = args.length;
		        if (length == 0) {
		        	if (!(sender instanceof Player)) {
		        		for (String b : data.getMessages_help()) {
		            	  Utility.sendConsole(b);
		        	}
		        		return true;
		        	}
		        	if (sender instanceof Player) {
		        		for (String c : data.getMessages_help()) {
		        			Utility.sendMsg(sender, c);
		        		}
		        		return true;
		        	}
		        }
		      
		      if(length > 0){
		    	
		    	  if(args[0].equalsIgnoreCase("reload")){
		    		  if (sender instanceof Player) {
		    			  if(!player.hasPermission("holo.reload")){
		              		Utility.sendMsg(player,"No Permission, too handsome.");
		             			return true;
		    			  }
		    			  else{
		    				  Utility.sendMsg(player,"Config Reloaded");
		              }
		            }else{
		              Utility.sendConsole("Config Reloaded");
		            }
		                 // keatas
		                 // 
		        		data.loadConfig();
		        		return true;
		        }
		    	  if (args[0].equalsIgnoreCase("info")) {
		    		  if (!sender.hasPermission("lastholo.update")) {
		    			  Utility.sendMsg(sender, "&cYou have no permission.");
		    			  return true;
		    		  }
		    			URL url = null;
		    			Scanner scanner = null;
		    			String str = "";
		    			String ver;
		    			try {
		    			url = new URL("https://pastebin.com/raw/8xGMfKZn");
		    			scanner = new Scanner(url.openStream());
		    			} catch (Exception e) {
		    				Utility.sendConsole("Couldn't get the latest version for LastHolo.");
		    			}
		    			str = scanner.nextLine();
		    			PluginDescriptionFile lastholo = LastHolo.getInstance().getDescription();
		    			ver = lastholo.getVersion();
		    		  Utility.sendMsg(sender, "&7&m-------------");
		    		  Utility.sendMsg(sender, " ");
		    		  Utility.sendMsg(sender, "&aYou are using LastHolo version:" + ver);
		    		  if (str.equals(ver)) {
		    			  Utility.sendMsg(sender, "&a&l** You are using the latest version **");
		    		  }
		    		  else if (!str.equals(ver)) {
		    			  Utility.sendMsg(sender, "&c&l** You are not using the latest version: &e" + str + "&c&l**");
		    			  Utility.sendMsg(sender, "&bDownload link: '&fhttps://bit.ly/2IY9hDb&b'");
		    		  }
		    		  Utility.sendMsg(sender, " ");
		    		  Utility.sendMsg(sender, "&7&m-------------");
		    	  }
		    	  if(args[0].equalsIgnoreCase("toggle")) {
		    		  if (!(sender instanceof Player)) {
		    			  Utility.sendMsg(sender, "&cOops! Remember that only players can chat :).");
		    			  return true;
		    		  }
		    		  UUID uuid = player.getUniqueId();
		    	  if (!player.hasPermission("holo.toggle")) {
		    		  Utility.sendMsg(player, data.getMessage_noPerm());
		    		  return true;
		    	  }
		    	  CacheStorage cache = CacheStorage.getInstance();
				  if (cache.getToggle(uuid) == null) {
					  cache.setToggle(uuid, true);
					  Utility.sendMsg(player, data.getMessage_ChatOn());
					  return true;
			  }
		    		  if (cache.getToggle(uuid) == false) {
		    			cache.setToggle(uuid, true);
		    			  Utility.sendMsg(player, data.getMessage_ChatOn());
		    			  return true;
		    		  }
		    		  if (cache.getToggle(uuid) == true) {
		    			  cache.setToggle(uuid,  false);
		    			  Utility.sendMsg(player, data.getMessage_ChatOff());
		    			  return true;
		    		  }
		    	  }
		    	  if (args[0].equalsIgnoreCase("blacklist")) {
		    		  if (!sender.hasPermission("lastholo.openblacklist")) {
		    			  Utility.sendMsg(sender, "&c&lOops, &7Can't do that!");
		    			  return true;
		    		  }
		    		  if (length < 2) {
		    			  Utility.sendMsg(sender, "&m-----------------");
		    			  Utility.sendMsg(sender, "    &b&lLastHolo");
		    			  Utility.sendMsg(sender, "&6/lh blacklist add <Player>");
		    			  Utility.sendMsg(sender, "&6/lh blacklist remove <Player>");
		    			  Utility.sendMsg(sender, "&6/lh blacklist list &e<Page>");
		    			  Utility.sendMsg(sender, "&m-----------------");
		    			  return true;
		    		  }
		    		  if (args[1].equalsIgnoreCase("add")) {
		    			  if (length < 3) {
		    				  Utility.sendMsg(sender, "&c&lOops! &7Use /lh blacklist add <Player>");
		    				  return true;
		    			  }
		    		  if (!sender.hasPermission("lastholo.blacklist.add")) {
		    			  Utility.sendMsg(player, "&cOops, &7I don't think you're supposed to do that.");
		    			  return true;
		    		  }
		    		  Player p = (Player) Bukkit.getPlayer(args[2]);
		    		  String name = args[2];
		    		  if (p == null) {
		    			  Utility.sendMsg(sender, "&cThat person does not exist! Just gonna go straight ahead");
		    		  } else { name = p.getName(); }
		    		  data.removeBlackList(name);
		    		  data.addBlacklist(name);
		    		  Utility.sendMsg(sender, "&c&l[&f&l!&c&l] &aThe player &4" + name + " &ahas been &ladded&a from LastHolo - BlackList");
		    		
		    		  return true;
		    		  }
		    	  if (args[1].equalsIgnoreCase("remove")) {
		    		  if (!sender.hasPermission("lastholo.blacklist.remove")) {
		    			  Utility.sendMsg(sender, "&cOops, &7I don't think you're supposed to do that.");
		    			  return true;
		    		  }
					  if (length < 3) {
						  Utility.sendMsg(sender, "&c&lOops! &7Use /lh blacklist remove <Player>");
						  return true;
					  }
		    		  Player p = (Player) Bukkit.getPlayer(args[2]);
		    		  String name = args[2];
		    		  if (p == null) {
		    			  Utility.sendMsg(sender, "&cThat person does not exist!");
		    		  } else { name = p.getName(); }
		    		  Utility.sendMsg(sender, "&c&l[&f&l!&c&l] &aThe player &2" + name + " &ahas been &lremoved &afrom LastHolo - BlackList");
		    		  data.removeBlackList(name);
		    		  return true;
		    	  }
		    	  if (args[1].equalsIgnoreCase("list")) {
		    		  if (!(sender instanceof Player)) {
		    			  Utility.sendConsole("Player only!");
		    			  return true;
		    		  }
		    		  if (!player.hasPermission("lastholo.blacklist.list")) {
		    			  Utility.sendMsg(player, "&cCan't see!");
		    			  return true;
		    		  }
		    		  int size = data.getBlackLists().size();
		    		  int pagescount = Math.max(1, (size % 5 > 0 ? (size / 5) + 1 : (size / 5)));
		    		  ArrayList<String> blacklists = data.getBlackLists();
		    		  HashMap<Integer, String> pages = new HashMap<Integer, String>();
		    		  for (int i = 0; i < data.getBlackLists().size(); i++) {
		    			  pages.put(i, blacklists.get(i));
		    		  }
		    		  if (length > 2) {
		    			  int intnormal = 0;
		    			  try {
		    			  intnormal = Integer.parseInt(args[2]);
		    			  } catch (NumberFormatException e) {
		    				  Utility.sendMsg(player, "&c&lOops! &7Please put a valid number.");
		    				  return true;
		    			  }
		    			  if (intnormal > pagescount) {
		    				  Utility.sendMsg(player, "&c&lOops! &7That is a invalid page, &amax page: &f" + pagescount + "&7!");
		    				  return true;
		    			  }
		    			  int intreg = Integer.parseInt(args[2]) * 5;
		    			  int intreg5 = intreg - 5;
		    			  if (intnormal == pagescount) {
		    				  Utility.sendMsg(player, "&4&m----------------------------");
		    				  Utility.sendMsg(player, "&c&l     Blacklisted Holo-Chats");
		            		  for (int i = intreg5; i < 5; i++) {
		            			  String get = pages.get(i);
		            			  if (get != null) {
		            			   Utility.sendMsg(player, "&7&m-&f " + pages.get(i));
		            		  }            		  }
		            		  Utility.sendMsg(player, "&f&l  Page: &7" + args[2] + "&l/&8" + pagescount);
		            		  Utility.sendMsg(player, "&4&m----------------------------");
		            		  return true;
		    			  }
		    			  Utility.sendMsg(player, "&4&m----------------------------");
		    			  Utility.sendMsg(player, "&c&l     Blacklisted Holo-Chats");
		        		  for (int i = intreg5; i < intreg; i++) {
		        			  String get = pages.get(i);
		        			  if (get != null) {
		        			   Utility.sendMsg(player, "&7&m-&f " + pages.get(i));
		        		  }        		  } 
		        		  Utility.sendMsg(player, "&f&l  Page: &7" + args[2] + "&l/&8" + pagescount);
		        		  Utility.sendMsg(player, "&4&m----------------------------");
		        		  return true;
		    		  }
		    		  Utility.sendMsg(player, "&4&m----------------------------");
		    		  Utility.sendMsg(player, "&c&l     Blacklisted Holo-Chats");
		    		  for (int i = 0; i < 5; i++) {
		    			  String get = pages.get(i);
		    			  if (get != null) {
		    				  Utility.sendMsg(player, "&7&m-&f " + pages.get(i));
		    		  }
		    		  }
		    		  Utility.sendMsg(player, "&f&l  Page: &71&l/&8" + pagescount);
		    		  Utility.sendMsg(player, "&4&m----------------------------");
		    		  return true;
		    	  }
		    	  if (!args[0].equalsIgnoreCase("toggle") || !args[0].equalsIgnoreCase("reload") || !args[0].equalsIgnoreCase("blacklist")) {
		    		  Utility.sendMsg(player, data.getMessage_noCommand());
		        		  return true;
		    	  }
		    	  }
		      }
		    }
			return true;
	}

}
