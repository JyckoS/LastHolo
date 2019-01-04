package com.gmail.JyckoSianjaya.LastHolo.Storage;

import java.util.UUID;


import org.bukkit.entity.Player;

import com.gmail.JyckoSianjaya.LastHolo.Hologram;


public class HoloData {
	private Hologram holo = null;
	private Double height = 0.0;
	private int liveticks = 100;
	private Player owner = null;
	private UUID owneruuid = null;
	public HoloData(Hologram holo, Double height, int liveticks, Player owner) {
		this.holo = holo;
		this.height = height;
		this.liveticks = liveticks;
		this.owner = owner;
		this.owneruuid = owner.getUniqueId();
	}
	public void setHeight(Double newheight) {
		this.height = newheight;
	}
	public void setLiveTicks(int newliveticks) {
		this.liveticks = newliveticks;
	}
	public UUID getUUID() { return this.owneruuid; }
	public boolean isOwnerOnline() {
		if (owner == null || !owner.isOnline()) return false;
		return true;
	}
	public Player getOwner() { return this.owner; }
	public Hologram getHologram() { return this.holo; }
	public Double getHeight() { return this.height; }
	public int getLiveTicks() { return this.liveticks; }
	public void reduceLiveTicks() {
		liveticks--;
	}
}
