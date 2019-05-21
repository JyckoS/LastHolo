package com.gmail.JyckoSianjaya.LastHolo;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Location;

import com.gmail.JyckoSianjaya.LastHolo.Runnables.SimpleRunnable;
import com.gmail.JyckoSianjaya.LastHolo.Runnables.SimpleTask;
import com.gmail.JyckoSianjaya.LastHolo.Storage.DataStorage;

public class Hologram {
	private ArrayList<HologramData> holograms = new ArrayList<HologramData>(30);
	private Location loc;
	private Hologram(Location loc) {
		this.loc = loc;
	}
	public void delete() {
		if (DataStorage.getInstance().isHoloRemovalAsync()) {
		for (HologramData hd : holograms) {
			hd.getArmorStand().remove();
		}
		holograms.clear();
		loc = null;
		}
		else {
			SimpleRunnable.getInstance().addTask(new SimpleTask() {
				@Override
				public void run() {
					for (HologramData hd : holograms) {
						hd.getArmorStand().remove();
					}
					holograms.clear();
					loc = null;
				}

				@Override
				public int getHealth() {
					// TODO Auto-generated method stub
					return health;
				}

				@Override
				public void reduceHealth() {
					// TODO Auto-generated method stub
					health--;
				}
				int health = 1;
			});
		}
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
		if (data == null) return;
		if (data.length() <= 0) return;
		SimpleRunnable.getInstance().addTask(new SimpleTask() {
			int health = 1;
		@Override
		public void run() {
			holograms.add(line, HologramData.createHoloData(loc, data));
		teleport(loc);
		}

		@Override
		public int getHealth() {
			// TODO Auto-generated method stub
			return health;
		}

		@Override
		public void reduceHealth() {
			// TODO Auto-generated method stub
			health--;
		}
		});
	}
	public void removeLine(int line) {
		holograms.remove(line);
	}
	public void addLine(String data) {
		if (data == null) return;
		if (data.length() <= 1) return;
		SimpleRunnable.getInstance().addTask(new SimpleTask() {
			int health = 1;
		@Override
		public void run() {
			holograms.add(HologramData.createHoloData(loc, data));
			teleport(loc);
		}

		@Override
		public int getHealth() {
			// TODO Auto-generated method stub
			return health;
		}

		@Override
		public void reduceHealth() {
			// TODO Auto-generated method stub
			health--;
		}
		});
	}
	public static Hologram createHologram(Location loc) {
		Hologram holo = new Hologram(loc);
		holo.teleport(loc);
		return holo;
	}
	public void teleport(Location locat) {
		this.loc = locat.clone();
		Double offset = 0.0;
		for (HologramData hd : holograms) {
			hd.getArmorStand().teleport(loc.clone().add(0.0, offset, 0.0));
			offset = offset - 0.2;
		}
	}
}
