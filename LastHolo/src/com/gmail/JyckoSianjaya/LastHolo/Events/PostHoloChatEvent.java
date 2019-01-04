package com.gmail.JyckoSianjaya.LastHolo.Events;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gmail.JyckoSianjaya.LastHolo.LineData;
import com.gmail.JyckoSianjaya.LastHolo.Storage.HoloData;

public class PostHoloChatEvent extends Event implements Cancellable {
	private HoloData holodata;
	private Boolean iscancelled = false;
	private Collection<LineData> linedatas;
	private static final HandlerList handlers = new HandlerList();
	public PostHoloChatEvent(HoloData data, Collection<LineData> ld) {
		this.holodata = data;
		this.linedatas = ld;
	}
	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return this.iscancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		// TODO Auto-generated method stub
		this.iscancelled = arg0;
	}

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}
	public Collection<LineData> getLineDatas() { return this.linedatas; }
	public HoloData getHoloData() { return this.holodata; }
	public Player getPlayer() { return this.holodata.getOwner(); }

}
