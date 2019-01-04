package com.gmail.JyckoSianjaya.LastHolo.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gmail.JyckoSianjaya.LastHolo.Storage.HoloData;

public class PreHoloChatEvent extends Event implements Cancellable {
	private Boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();
	private HoloData holodata;
	public PreHoloChatEvent(HoloData holodata) {
		this.holodata = holodata;
	}
	public HoloData getHoloData() { return this.holodata; }
	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		// TODO Auto-generated method stub
		this.cancelled = arg0;
	}
	public Player getPlayer() {
		return this.holodata.getOwner();
	}
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}

}
