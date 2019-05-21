package com.gmail.JyckoSianjaya.LastHolo.Storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitRunnable;

import com.cyr1en.cp.CommandPrompter;
import com.gmail.JyckoSianjaya.LastHolo.Hologram;
import com.gmail.JyckoSianjaya.LastHolo.LastHolo;
import com.gmail.JyckoSianjaya.LastHolo.Utility.Utility;
import com.gmail.JyckoSianjaya.LastHolo.Utility.XSound;

public class DataStorage {
	private static DataStorage instance;
	private ArrayList<String> blacklists = new ArrayList<String>();
	private WGVersion wgversion;
	private ServerVersion srvversion;
	private CommandPrompter cmdprompter;
	private Boolean wgenabled;
	private Boolean asyncprocess = true;
	private Boolean usespecialarmorstand;
	private Boolean usesasyncremoval = true;
	private Boolean ispapi;
	private CacheStorage cache = CacheStorage.getInstance();

	private DataStorage() {
		this.srvversion = checkVersion();
		Utility.sendConsole("Detected Server Version: " + this.srvversion);
		loadConfig();
		checkPAPIS();
		if (ispapi) {
			Utility.sendConsole("&9LastHolo >> &7Hooked with PAPI");
		}
		new BukkitRunnable() {
			@Override
			public void run() {

				synchronized(this) {
				for (HoloData name  : new ArrayList<>(cache.getHDValues())) {
					Player p = name.getOwner();
					Hologram holo = name.getHologram();
					Double height = name.getHeight();
					int liveticks = name.getLiveTicks();
					
					if (!name.isOwnerOnline()) {
						holo.delete();
						cache.removeHoloData(name.getUUID());
						continue;
					}
					if (liveticks < 1) {
						cache.removeHoloData(name.getUUID());
						holo.delete();
					}
					else {
						name.reduceLiveTicks();
						if (holo == null) return;
						holo.teleport(p.getLocation().add(0.0, height, 0.0));
					}
				}
			}
			}
		}.runTaskTimerAsynchronously(LastHolo.getInstance(), 1L, 1L);
	}
	public void setSpecialArmor(Boolean use) {
		this.usespecialarmorstand = use;
	}
	public static DataStorage getInstance() {
		if (instance == null) instance = new DataStorage();
		return instance;
	}
	public boolean isSpectatorHoloHidden() {
		return this.spectator_hide;
	}
	private ServerVersion checkVersion() {
		String ver = LastHolo.getInstance().getServer().getVersion();
		if (ver.contains("1.8")) {
			return ServerVersion.V1_8;
		}
		if (ver.contains("1.9")) {
			return ServerVersion.V1_9;
		}
		if (ver.contains("1.10")) {
			return ServerVersion.V1_10;
		}
		if (ver.contains("1.11")) {
			return ServerVersion.V1_11;
		}
		if (ver.contains("1.12")) {
			return ServerVersion.V1_12;
		}
		if (ver.contains("1.13")) {
			return ServerVersion.V1_13;
		}
		return null;
	}
	public ServerVersion getServerVersion() {
		return this.srvversion;
	}
	public enum ServerVersion {
		V1_8,
		V1_9,
		V1_10,
		V1_11,
		V1_12,
		V1_13, V1_14;
	}
	public boolean checkPAPI() {
		return this.ispapi;
	}
	private void checkPAPIS() {
		ispapi = Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
	public final void loadBlacklist() {
		File blacklist = new File(LastHolo.getInstance().getDataFolder(), "datastorage" + File.separator + "blacklisted-players.yml");
		YamlConfiguration yml;
		if (!blacklist.exists()) {
			try {
				blacklist.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			yml = YamlConfiguration.loadConfiguration(blacklist);
			ArrayList<String> zz = new ArrayList<String>();
			zz.add("aguywhospeaksalot");
			yml.set("blacklisted-players", zz);
			try {
				yml.save(blacklist);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		yml = YamlConfiguration.loadConfiguration(blacklist);
		for (String str : yml.getStringList("blacklisted-players")) {
			this.addBlacklist(str);
		}
	}
	public final void saveBlacklist() {
		File blacklist = new File(LastHolo.getInstance().getDataFolder(), "datastorage" + File.separator + "blacklisted-players.yml");
		blacklist.delete();
		if (!blacklist.exists()) {
			try {
				blacklist.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrayList<String> toblck = new ArrayList<String>(this.getBlackLists());
		File blacklist2 = new File(LastHolo.getInstance().getDataFolder(), "datastorage" + File.separator + "blacklisted-players.yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(blacklist2);
		yml.set("blacklisted-players", toblck);
		try {
			yml.save(blacklist2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean useExtremeFollow(){
		return this.use_extreme_follow;
	}
	public void removeBlackList(String toremove) {
		blacklists.remove(toremove);
	}
	public void addBlacklist(String toadd) {
		blacklists.add(toadd);
	}
	public boolean isBlacklisted(String name) {
		return blacklists.contains(name);
	}
	public ArrayList<String> getBlackLists() { return this.blacklists; }
	public WGVersion getWGVersion() { return this.wgversion; }
	public double getHoloOffset() { return this.holo_offset; }
	public int getHoloLiveTick() { return this.holo_liveticks; }
	public double getHoloFirstOffset() { return this.first_holo_offset; }
	public String getChatFormat() { return this.chatformat; }
	public boolean isFirstJoinHoloForced() { return this.force_firstjoin_enable; }
	public boolean isChatPopping() { return this.holo_chat_pops; }
	public boolean isHoloChatAlwaysEnabled() { return this.holo_chat_always_enable; }
	public boolean isHoloChatNearbyOnly() { return this.use_nearby_only; }
	public int getMinimumNearbyRange() { return this.nearby_only_range; }
	public boolean isOriginalChatHidden() { return this.hide_original_chat; }
	public boolean isHidingRequiresToggles() { return this.hide_requires_toggle; }
	public boolean isWordSystemUsed() { return this.use_word_system; }
	public int getMaxWords() { return this.max_words; }
	public int getMaxWordsInALine() { return this.max_words_line; }
	public int getMaxWordLength() { return this.max_word_length; }
	public int getMaxSentenceLength() { return this.max_sentence_length; }
	public int getFullAttemptCheck() { return this.full_attempt_check; }
	public boolean isLengthEnabled() { return this.use_max_length; }
	public int getMaxParagraphLength() { return this.max_length; }
	public boolean isNewLineCreatedIfFull() { return this.newline_full; }
	public int getLengthEachLine() { return this.linelength; }
	public String getMessage_ChatOn() { return this.chaton; }
	public String getMessage_ChatOff() { return this.chatoff; }
	public String getMessage_noPerm() { return this.noperm; }
	public String getMessage_noCommand() { return this.nocommand; }
	public List<String> getMessages_help() { return this.helpmsgs; }
	public ArrayList<PackedSound> getLocalSounds() { return this.localsounds; }
	public Boolean isLocalSoundUsed() { return this.uselocalsound; }
	public ArrayList<PackedSound> getGlobalSounds() { return this.globalsounds; }
	public Boolean isGlobalSoundUsed() { return this.useglobalsound; }
	public Boolean isWorldsDisabled() { return this.disableworlds; }
	public List<String> getDisabledWorlds() { return this.disabledworld; }
	public Boolean isRegionOptionIgnored() { return this.ignore_region_option; }
	public List<String> getDisabledRegions() { return this.disabled_regions; }
	public boolean isRegionDisabled() { return this.disableregions; }
	public boolean isRegionAllowed() { return this.allowedregions_only; }
	public List<String> getAllowedRegions() { return this.allowed_regions; }
	public boolean isWorldGuardEnabled() { return this.isWGenabled; }
	public CommandPrompter getCommandPrompter() { return this.cmdprompter; }
	public Boolean isSpecialArmorStandUsed() {
		return this.usespecialarmorstand;
	}
	private String color(String color) {
		return Utility.TransColor(color);
	}
	public void loadConfig() {
		this.isWGenabled = this.WGEnable();
		this.isWEenabled = Bukkit.getPluginManager().isPluginEnabled("WorldEdit");
		if (isWGenabled) this.getVersion(); 
		this.iscmdprompter = Bukkit.getPluginManager().isPluginEnabled("CommandPrompter");
		if (iscmdprompter) {
			cmdprompter = (CommandPrompter) Bukkit.getPluginManager().getPlugin("CommandPrompter");
		}
		LastHolo.getInstance().reloadConfig();
		  FileConfiguration config = LastHolo.getInstance().getConfig();
		  try {
			  this.usesasyncremoval = config.getBoolean("async_holo_removal");
			  this.usespecialarmorstand = config.getBoolean("special_armorstand");
			  this.allowed_regions = config.getStringList("allowed_regions");
			  this.allowedregions_only = config.getBoolean("allow_chat_certain_region_only");
			  this.chatformat = color(config.getString("chat_format"));
			  this.chatoff = color(config.getString("chat_off"));
			  this.chaton = color(config.getString("chat_on"));
			  this.noperm = color(config.getString("no_permission"));
			  this.nocommand = color(config.getString("command_doesnt_exist"));
			  this.helpmsgs = Utility.TransColor(config.getStringList("help_messages"));
			  this.disabled_regions = config.getStringList("disabled_regions");
			  this.disableregions = config.getBoolean("disable_certain_regions");
			  this.disabledworld = config.getStringList("disabled_worlds");
			  this.disableworlds = config.getBoolean("disable_certain_worlds");
			  this.first_holo_offset = config.getDouble("holo_firstoffset");
			  this.force_firstjoin_enable = config.getBoolean("force_holo_chat_first_join");
			  this.full_attempt_check = config.getInt("words_system.advancced_only.full_attempt_check");
			  List<String> globalsounds = config.getStringList("global_sounds");
			  this.globalsounds.clear();
			  for (String str : globalsounds) {
				  String[] sond = str.split("-");
				  Sound target = null;
				  try {
					  target = XSound.requestXSound(sond[0]);
				  } catch (IllegalArgumentException e) {
					  Utility.sendConsole("The sound: " + sond[0] + "doesn't exist!");
					  continue;
				  }
				  Float vol = Float.valueOf(sond[1]);
				  Float pitch = Float.valueOf(sond[2]);
				  PackedSound sound = new PackedSound(target, vol, pitch);
				  this.globalsounds.add(sound);
			  }
			  this.useglobalsound = config.getBoolean("enable_global_sound_effect");
			  List<String> localsounds = config.getStringList("local_sounds");
			  Boolean enablesound = config.getBoolean("enable_local_sound");
			  this.localsounds.clear();
			  if (enablesound) {
				  for (String str: localsounds) {
					  String[] sond = str.split("-");
					  Sound target = null;
					  try {
						  target = XSound.requestXSound(sond[0]);
					  } catch (IllegalArgumentException e) {
						  Utility.sendConsole("The sound: " + sond[0] + "doesn't exist!");
						  continue;
					  }
					  Float vol = Float.valueOf(sond[1]);
					  Float pitch = Float.valueOf(sond[2]);
					  PackedSound sound = new PackedSound(target, vol, pitch);
					  this.localsounds.add(sound);
				  }
			  }
			  this.asyncprocess = config.getBoolean("async_chat_processing");
			  this.hide_original_chat = config.getBoolean("original_chat.hidden");
			  this.hide_requires_toggle = config.getBoolean("original_chat.requires_toggle");
			  this.holo_chat_always_enable = config.getBoolean("always_enable_holo_chat_join");
			  this.holo_chat_pops = config.getBoolean("chat_pops_during_chat");
			  this.holo_liveticks = config.getInt("holo_livetick");
			  this.holo_offset = config.getDouble("holo_offset");
			  this.use_word_system = config.getBoolean("words_system.enable_system");
			  this.max_words = config.getInt("words_system.max_words");
			  this.max_word_length = config.getInt("words_system.max_word_length");
			  this.max_sentence_length = config.getInt("words_system.max_sentence_length_line");
			  this.max_words_line = config.getInt("words_system.max_words_line");
			  this.ignore_region_option = config.getBoolean("ignore_region_option");
			  this.use_nearby_only = config.getBoolean("nearby_only.use_system");
			  this.nearby_only_range = config.getInt("nearby_only.min_range");
			  this.use_extreme_follow = config.getBoolean("use_extreme_follow_method");
			  this.spectator_hide = config.getBoolean("spectator_holo_hidden");
			  this.use_max_length = config.getBoolean("enable_max_length");
			  this.max_sentence_length = config.getInt("max_length");
			  this.newline_full = config.getBoolean("enable_new_line_if_full");
			  this.linelength = config.getInt("each_line_length");
		  } catch (Exception e) {
			  e.printStackTrace();
			  Utility.sendConsole("&cAn error has occured! You put something wrong in the config.yml");
			  Utility.sendConsole("&aFeel free to PM me on Spigot: Gober, if you're confused where you are wrong at.");
			  if (Bukkit.getServer().getVersion().contains("1.13")) {
				  Utility.sendConsole("&b[LastHolo] &7Server version detected: 1.13.X, using our 'Robot', to fix it for you..");
				  Utility.sendConsole("&9Robot > &fTrying to change &bglobal-sound&f to &eBLOCK_NOTE_BLOCK_HARP&f....");
				  config.set("global-sound", "BLOCK_NOTE_BLOCK_HARP");
				  LastHolo.getInstance().saveConfig();
				  Utility.sendConsole("&9Robot > &fChanged, trying to reload config..");
				  try {
					  Sound.valueOf(config.getString("global-sound"));
				  } catch (Exception rere) {
					  Utility.sendConsole("&9Robot > &cOh no! &fLooks like the trouble still occur, I think this is out of my limit for this one, please contact me on Spigot : Gober.");
					  return;
				  }
				  Utility.sendConsole("&9Robot > &a(^-^) &fSuccess! Try to chat now!");
			  }
		  }
	      	}
	  private boolean WGEnable() {
		  return LastHolo.getInstance().getServer().getPluginManager().isPluginEnabled("WorldGuard");
	  }
	  private void getVersion() {
		  if (!WGEnable()) {
			  return;
		  }
		  Plugin pl = Bukkit.getPluginManager().getPlugin("WorldGuard");
		  PluginDescriptionFile pdf = pl.getDescription();
		  String ver = pdf.getVersion();
		  Boolean isnew = ver.startsWith("7");
		  switch (isnew.toString().toLowerCase()) {
		  case "true":
			  wgversion = WGVersion.after;
			  Utility.sendConsole("[LH] WorldGuard Version - 7.0+");
			  return;
		  case "false":
			  wgversion = WGVersion.before;
			  Utility.sendConsole("[LH] WorldGuard Version - 6.0 +");
			  return;
		  }
	  }
	  public Boolean shouldEnable(){
	  	return Bukkit.getServer().getPluginManager().isPluginEnabled("HolographicDisplays");
	  }
	  public enum WGVersion {
		  before,
		  after;
	  }
	  public boolean isHoloRemovalAsync() {
		  return this.usesasyncremoval;
	  }
	  public boolean isProcessingAsync() {
		  return this.asyncprocess;
	  }
	private Boolean isWEenabled = false;
	private Boolean isWGenabled = false;
	private Boolean iscmdprompter = false;
	
	private Boolean spectator_hide = true;
	private Boolean use_extreme_follow = false;
	private Boolean force_firstjoin_enable = false;
	private Boolean holo_chat_pops = true;
	private Boolean holo_chat_always_enable = false;
	private Boolean use_nearby_only = true;
	private Boolean hide_original_chat = false;
	private Boolean hide_requires_toggle = false;
	
	/* WORDS SYSTEM */
	
	private Boolean use_word_system = true;
	private int max_words = 30;
	private int max_words_line = 5;
	private int max_word_length = 17;
	private int max_sentence_length = 24;
	private int full_attempt_check = 1;
	
	/*LENGTH*/
	private Boolean use_max_length = false;
	private int max_length = 35;
	private boolean newline_full = true;
	private int linelength = 15;
	
	/* MESSAGES */
	private String chaton = "";
	private String chatoff = "";
	private String noperm = "";
	private String nocommand = "";
	private List<String> helpmsgs;
	
	/* SOUNDS */
	private ArrayList<PackedSound> localsounds = new ArrayList<PackedSound>();
	private ArrayList<PackedSound> globalsounds = new ArrayList<PackedSound>();
	private boolean uselocalsound = true;
	private boolean useglobalsound = false;
	
	/* DISABLES */
	private List<String> disabledworld;
	private Boolean disableworlds = false;
	private Boolean ignore_region_option = false;
	
	private List<String> disabled_regions;
	private Boolean disableregions = false;
	
	private List<String> allowed_regions;
	private Boolean allowedregions_only = false;
	
	private int nearby_only_range = 13;
	
	private double holo_offset = 1.0;
	private int holo_liveticks = 100;
	private double first_holo_offset = 0.0;
	
	private String chatformat = "";
	
}
