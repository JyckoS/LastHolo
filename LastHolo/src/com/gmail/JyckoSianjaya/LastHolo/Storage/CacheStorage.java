package com.gmail.JyckoSianjaya.LastHolo.Storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class CacheStorage {
	private static CacheStorage instance;
	private HashMap<UUID, HoloData> holodatas = new HashMap<UUID, HoloData>();
	private HashMap<UUID, Boolean> toggles = new HashMap<UUID, Boolean>();
	private CacheStorage() {}
	public static CacheStorage getInstance() {
		if (instance == null) instance = new CacheStorage();
		return instance;
	}
	public Collection<HoloData> getHDValues() {
		return holodatas.values();
	}
	public Set<UUID> getHDKeys() {
		return holodatas.keySet();
	}
	public void removeHoloData(UUID uuid) {
		this.holodatas.remove(uuid);
	}
	public Boolean getToggle(UUID uuid) {
		Boolean toggle = toggles.get(uuid);
		if (toggle == null) toggle = false;
		return toggle;
	}
	public void setToggle(UUID uuid, Boolean toggle) {
		toggles.put(uuid, toggle);
	}
	public void setHoloData(UUID uuid, HoloData data) {
		this.holodatas.put(uuid, data);
	}
	public HoloData getHoloData(UUID uuid) {
		return holodatas.get(uuid);
	}
}
