package com.gmail.JyckoSianjaya.LastHolo;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Location;

public class Hologram {
	private ArrayList<HologramData> holograms = new ArrayList<HologramData>(30);
	private Location loc;
	private Hologram(Location loc) {
		this.loc = loc;
	}
	public void delete() {
		for (HologramData hd : holograms) {
			hd.getArmorStand().remove();
		}
		holograms.clear();
		loc = null;
	}
	public Location getLocation() {
		return this.loc;
	}
	public int size() {
		return holograms.size();
	}
	public void clear() {
		for (HologramData hd : holograms) {
			hd.getArmorStand().remove();
		}
		holograms.clear();
	}
	public void insertLine(int line, String data) {
		holograms.add(line, HologramData.createHoloData(this.loc, data));
		teleport(loc);
	}
	public void removeLine(int line) {
		holograms.remove(line);
	}
	public void addLine(String data) {
		this.holograms.add(HologramData.createHoloData(loc, data));
		teleport(loc);
	}
	public static Hologram createHologram(Location loc) {
		Hologram holo = new Hologram(loc);
		holo.teleport(loc);
		return holo;
	}
	public void teleport(Location loc) {
		this.loc = loc;
		int holos = holograms.size();
		Double offset = 0.0; 
		for (HologramData hd : holograms) {
			hd.getArmorStand().teleport(loc.clone().add(0.0, offset, 0.0));
			offset-=0.2;
		}
	}
}
