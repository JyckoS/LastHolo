package com.gmail.JyckoSianjaya.LastHolo.Listeners;

import java.io.File;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.JyckoSianjaya.LastHolo.Hologram;
import com.gmail.JyckoSianjaya.LastHolo.LastHolo;
import com.gmail.JyckoSianjaya.LastHolo.LineData;
import com.gmail.JyckoSianjaya.LastHolo.Events.PostHoloChatEvent;
import com.gmail.JyckoSianjaya.LastHolo.Events.PreHoloChatEvent;
import com.gmail.JyckoSianjaya.LastHolo.Storage.CacheStorage;
import com.gmail.JyckoSianjaya.LastHolo.Storage.DataStorage;
import com.gmail.JyckoSianjaya.LastHolo.Storage.DataStorage.WGVersion;
import com.gmail.JyckoSianjaya.LastHolo.Storage.HoloData;
import com.gmail.JyckoSianjaya.LastHolo.Storage.PackedSound;
import com.gmail.JyckoSianjaya.LastHolo.Utility.Utility;
import com.gmail.JyckoSianjaya.LastHolo.Utility.WG_12;
import com.gmail.JyckoSianjaya.LastHolo.Utility.WG_13;

import me.clip.placeholderapi.PlaceholderAPI;

public class SimpleHandler {
	private static SimpleHandler instance;
	private CacheStorage cache = CacheStorage.getInstance();
	private DataStorage data = DataStorage.getInstance();
	private SimpleHandler() {}
	public static SimpleHandler getInstance() {
		if (instance == null) instance = new SimpleHandler();
		return instance;
	}
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		if (!p.hasPlayedBefore()) {
			cache.setToggle(uuid, data.isFirstJoinHoloForced());
		}
		if (cache.getToggle(uuid) == null) {
			cache.setToggle(uuid, false);
	   		File file = new File(LastHolo.getInstance().getDataFolder() + File.separator + "datastorage", uuid + ".yml");
    		if (!file.exists()) return;
    		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
    		Boolean toggled = yml.getBoolean("toggled");
    		cache.setToggle(uuid, toggled);
		}
		if (data.isHoloChatAlwaysEnabled() == true) {
			cache.setToggle(uuid, true);
		}
	}
	public void manageKick(PlayerKickEvent e) {
	   	Player p = e.getPlayer();
    	String thename = p.getName();
		UUID uuid = p.getUniqueId();
    		Boolean en = null;
    		if (cache.getToggle(uuid) != null) en = cache.getToggle(uuid);
    		File file = new File(LastHolo.getInstance().getDataFolder() + File.separator + "datastorage", uuid + ".yml");
    		if (file.exists()) {
    			YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
    			yml.set("toggled", en);
				try {
					yml.save(file);
				} catch (IOException re) {
					// TODO Auto-generated catch block
					}
    			}
    		else {
    			YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
    			yml.set("toggled", en);
    			try {
					file.createNewFile();

						yml.save(file);
				} catch (IOException er) {
					Utility.sendConsole("&3[Lastholo] &bCouldn't create file for " + thename);
				}
    		}
    	if (cache.getHoloData(uuid) != null) {
    		HoloData hda = cache.getHoloData(uuid);
    		Hologram holo = hda.getHologram();
    		holo.delete();
    		cache.removeHoloData(uuid);
    	}
				
	}
	public void manageMove(PlayerMoveEvent e) {
		  if (data.useExtremeFollow()) {
			  if (cache.getHoloData(e.getPlayer().getUniqueId()) != null) {
				  HoloData hd = cache.getHoloData(e.getPlayer().getUniqueId());
				  Hologram holo = hd.getHologram();
				  holo.teleport(e.getTo().clone().add(0.0, hd.getHeight(), 0.0));
			  }
			  }
	}
	public void manageQuit(PlayerQuitEvent e) {
	   	Player p = e.getPlayer();
    	String thename = p.getName();
		UUID uuid = p.getUniqueId();
    		Boolean en = null;
    		if (cache.getToggle(uuid) != null) en = cache.getToggle(uuid);
    		File file = new File(LastHolo.getInstance().getDataFolder() + File.separator + "datastorage", uuid + ".yml");
    		if (file.exists()) {
    			YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
    			yml.set("toggled", en);
				try {
					yml.save(file);
				} catch (IOException es) {
					// TODO Auto-generated catch block
					}
    			}
    		else {
    			try {
					file.createNewFile();
	    			YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
	    			yml.set("toggled", en);
					try {
						yml.save(file);
					} catch (IOException re) {
						// TODO Auto-generated catch block
						}
				} catch (IOException re) {
					Utility.sendConsole("&3[Lastholo] &bCouldn't create file for " + thename);
				}
    		}
    	if (cache.getHoloData(uuid) != null) {
    		HoloData hd = cache.getHoloData(uuid);
    		Hologram holo = hd.getHologram();
    		holo.delete();
    		cache.removeHoloData(uuid);
    	}
	}
	public void manageChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
    String name = p.getName();
    if (data.isSpectatorHoloHidden()) {
    	if (p.getGameMode() == GameMode.SPECTATOR) {
    		return;
    	}
    }
    if (Utility.isVanished(p)) {
    	return;
    }
	if (data.isGlobalSoundUsed()) {
		for (PackedSound sound : data.getGlobalSounds()) {
			Utility.playGlobal(sound.getSound(), sound.getVolume(), sound.getPitch());
		}
	}
	if (data.isHoloChatNearbyOnly()) {
		int range = data.getMinimumNearbyRange();
		ArrayList<Player> players = Utility.near(p, range);
		if (players.isEmpty() || players.size() < 1) {
			return;
		}
	}
    if (data.getBlackLists().contains(name)) {
    	return;
    }
    if (data.getCommandPrompter() != null) {
    	if (data.getCommandPrompter().inCommandProcess(p)) return;
    }
    if (e.isCancelled()) return;
		String msg = e.getMessage();
		String chatf = "If this message exists, means there is an error";
		World w = p.getWorld();
		Location ge = p.getLocation();
		String namexx = w.getName();
		if (!data.isRegionOptionIgnored()) {
		if (data.isRegionAllowed()) {
			if (data.isWorldGuardEnabled()) {
				if (data.getWGVersion() == WGVersion.after) {
					if (WG_13.allowedRegion(p, w, ge, data.getAllowedRegions())) return;
				}
				else if (data.getWGVersion() != WGVersion.before) {
					if (WG_12.allowedRegion(w, ge, data.getAllowedRegions())) return;
				}
			}
		}
		if (data.isRegionDisabled()) {
			if (data.isWorldGuardEnabled()) {
				if (data.getWGVersion() == WGVersion.after) {
					if (WG_13.disabledRegion(p, w, ge, data.getDisabledRegions())) return;
 				}
				else if (data.getWGVersion() != WGVersion.before){
					if (WG_12.disabledRegion(w, ge, data.getDisabledRegions())) return;
				}
				
			}
		}
		}
		if (data.isWorldsDisabled()) {
		for (String we : data.getDisabledWorlds()) {
			if (namexx.equals(we)) {
				return;
						}
				}
			}
		if (p.hasPermission("holo.chat")) {
			if (cache.getToggle(uuid) == null) {
				cache.setToggle(uuid, true);
			}
			if (data.isOriginalChatHidden()) {
				Boolean conte = true;
				if (data.isHidingRequiresToggles()) {
					if (cache.getToggle(uuid) != true) {
						conte = false;
					}
				}
				if (conte) {
					e.setCancelled(true);
				}
			}
			if (cache.getToggle(uuid)) {
			int g = msg.length();
			if (data.isLengthEnabled()) {
				
				if (g >= data.getMaxParagraphLength()) {
					msg = msg.substring(0, data.getMaxParagraphLength()) + "..";
				}
			}
			chatf = data.getChatFormat();
			if (data.checkPAPI()) {
				chatf = PlaceholderAPI.setPlaceholders(p, chatf);
			}
			if (chatf.contains("%CHAT%")) {
			chatf = chatf.replace("%CHAT%", msg);
			}
			if (data.checkPAPI()) {
				if (p.hasPermission("lastholo.superplaceholder")) {
					chatf = PlaceholderAPI.setPlaceholders(p, chatf);
				}
			}
			if (chatf == null) {
				Utility.sendConsole("&b&l[LastHolo] &cAn error occured, is the chat-format null?");
				Utility.sendConsole("&eChat-Format: &f" + chatf);
				return;
			}
			if (data.isLocalSoundUsed()) {
				for (PackedSound sound : data.getLocalSounds())
				Utility.PlaySoundAt(p.getWorld(), p.getLocation(), sound.getSound(), sound.getVolume(), sound.getPitch());
			}
			if (p.hasPermission("holo.color")) {
				chatf = org.bukkit.ChatColor.translateAlternateColorCodes('&', chatf);
			}
				Location locas = p.getLocation().add(0.0, data.getHoloFirstOffset(), 0.0);
				Hologram holo;
				HoloData holodata;
				if (cache.getHoloData(uuid) == null) {
					synchronized(this) {
					holo = Hologram.createHologram(locas);
					}
					holodata = new HoloData(holo, data.getHoloOffset(), data.getHoloLiveTick(), p);
				}
				else {
					holodata = cache.getHoloData(uuid);
					holo = holodata.getHologram();
				}
				PreHoloChatEvent event = new PreHoloChatEvent(holodata);
				Bukkit.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled()) {
					cache.removeHoloData(uuid);
					holo.delete();
					return;
				}
				holo.teleport(p.getLocation().add(0.0, data.getHoloFirstOffset(), 0.0));
				cache.setHoloData(uuid, holodata);
					if (data.isNewLineCreatedIfFull()) {
						if (holo.size() > 0) {
							holo.clear();
						}
							int length = chatf.length();
							int size = Math.max(0, (length % data.getMaxSentenceLength()) > 0 ? (length / data.getMaxSentenceLength()) : (length / data.getMaxSentenceLength() - 1));
							String ccolor = null;
							Collection<LineData> ldaa = null;
							if (!data.isWordSystemUsed()) {
							for (int i = 0; i <= size; i++) {
								String messg = null;
								if (i >= 0 && i < size){
										int gage = i * data.getMaxSentenceLength() + data.getMaxSentenceLength();
										messg = chatf.substring((i * data.getMaxSentenceLength()), gage);
										if (ccolor != null) {
											messg = ccolor + messg;
										}
										ccolor = ChatColor.getLastColors(messg);
										holo.insertLine(i, messg);
								}
								if (i >= 0 && i == size) {
									messg = chatf.substring((i * data.getMaxSentenceLength()), length);
									if (ccolor != null) {
										messg = ccolor + messg;
									}
									holo.insertLine(i, messg);
								}
							}
							}
							else {
								String[] words = chatf.split(" ");
								int wlength = words.length;
								int reallength = wlength;
								if (wlength > data.getMaxWordLength()) {
									wlength = data.getMaxWordLength();
								}
								int wsizes = Math.max(0, (wlength % data.getMaxWordsInALine()) > 0 ? wlength / data.getMaxWordsInALine() : wlength / data.getMaxWordsInALine() - 1);
								HashMap<Integer, LineData> info = new HashMap<Integer, LineData>();
								String cachess = "";
								for (int i = 0; i <= wsizes; i++) {
									int firstlength = i * data.getMaxWordsInALine();
									int maxlength = i * data.getMaxWordsInALine() + data.getMaxWordsInALine();
									if (maxlength > wlength) {
										maxlength = wlength;
									}
									while (firstlength > wlength) {
										firstlength--;
									}
									String wordz = "";
									if (ccolor != null) {
										wordz = ccolor + wordz;
									}
									ArrayList<String> addedwords = new ArrayList<String>();
									for (int c = firstlength; c < maxlength; c++) {
										String wdd = words[c];
										if (wdd.length() > data.getMaxWordLength()) {
											wdd = wdd.substring(0, data.getMaxWordLength());
										}
										addedwords.add(wdd);
										if (c == maxlength) {
											wordz = wordz + wdd;
											continue;
										}
										wordz = wordz + wdd + " ";
									}
									ccolor = ChatColor.getLastColors(wordz);
									cachess = "";
									if (wordz.length() > data.getMaxSentenceLength()) {
										LineData laa = new LineData(i + 1, "", true, "", ccolor);
										String newword = "";
										int attempt = 0;
										ArrayList<String> cacheword = new ArrayList<String>();
										Boolean full = false;
										for (String str : addedwords) {
											if (newword.length() > data.getMaxSentenceLength()) {
												full = true;
											}
											if (full) {
												
												/* if (attempt <= this.full_attempt_length) {
													int ss = max_sentence_length;
													String cuts = newword + " " + str; 
													if (cuts.length() < ss) {
														ss = cuts.length();
														cuts = cuts.substring(0, ss);
														newword = cuts;
													}
													else {
													cuts = cuts.substring(0, max_sentence_length);
													cacheword.add(cuts.substring(max_sentence_length, cuts.length()));
													newword = cuts;
													}
													continue;
												}
												*/
												cacheword.add(str);
												continue;
											}
											if (attempt == addedwords.size()) {
												newword = newword + str;
											}
											attempt++;
											newword = newword + str + " ";
										}
										wordz = newword;
										for (String str : cacheword) {
											if (str.equals(cacheword.get(cacheword.size() - 1))) { cachess = cachess + str; continue;}
											cachess = cachess + str + " ";
										}
										laa.setCache(cachess);
										info.put(i + 1, laa);
									}
									if (i == wsizes) {
										if (wlength < reallength) {
											wordz = wordz + "...";
										}
									}
									LineData ld;
									if (info.containsKey(i)) {
										ld = info.get(i);
										ld.setLine(wordz);
									}
									else {
										ld = new LineData(i, wordz, true, "", ccolor);
										info.put(i, ld);
									}
								}
								holo.clear();
								ldaa = info.values();
								for (LineData ld : info.values()) {
									int line = ld.getCurrentLine();
									String finals = ld.getFinal();
									if (finals.endsWith(" ")) {
										finals = finals.substring(0, finals.length() - 1);
									}
									holo.insertLine(line, finals);
								}
							}
							if (data.isChatPopping() == true) {
								holo.teleport(locas);
							}
							holodata.setLiveTicks(data.getHoloLiveTick() + size * 33);
							holodata.setHeight(holodata.getHeight() + size * 0.2);
							PostHoloChatEvent eventa = new PostHoloChatEvent(holodata, ldaa);
							Bukkit.getServer().getPluginManager().callEvent(eventa);
							if (eventa.isCancelled()) {
								holo.delete();
								cache.removeHoloData(uuid);
								return;
							}
						}
					if (!data.isNewLineCreatedIfFull()) {
						if(holo.size() > 0) {
							holo.clear();
							holo.addLine(chatf);
							if (data.isChatPopping() == true) {
								holo.teleport(locas);
							}
						}
						else {
							holo.addLine(chatf);
						}
					}
			}
		}
	}
}
