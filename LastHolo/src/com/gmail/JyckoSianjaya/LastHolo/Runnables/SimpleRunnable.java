package com.gmail.JyckoSianjaya.LastHolo.Runnables;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.JyckoSianjaya.LastHolo.LastHolo;

public final class SimpleRunnable {
	private static SimpleRunnable instance;
	private ArrayList<SimpleTask> tasks = new ArrayList<SimpleTask>();
	private SimpleRunnable() {
		new BukkitRunnable() {
			@Override
			public void run() {
			for (SimpleTask task : new ArrayList<SimpleTask>(tasks)) {
				if (task.getHealth() <= 0) {
					tasks.remove(task);
					continue;
				}
				task.reduceHealth();
				task.run();
			}
			}
		}.runTaskTimer(LastHolo.getInstance(), 1L, 1L);
	}
	public static SimpleRunnable getInstance() {
		if (instance == null) {
			instance = new SimpleRunnable();
		}
		return instance;
	}
	public void addTask(SimpleTask task) {
		this.tasks.add(task);
	}
}
