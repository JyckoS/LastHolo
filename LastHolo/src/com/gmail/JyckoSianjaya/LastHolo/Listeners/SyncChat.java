package com.gmail.JyckoSianjaya.LastHolo.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SyncChat implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void chat(AsyncPlayerChatEvent e) {
		SimpleHandler.getInstance().manageChat(e);
	}
}
