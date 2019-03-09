package com.gmail.JyckoSianjaya.LastHolo.NMS;

import org.bukkit.Bukkit;


import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import com.gmail.JyckoSianjaya.LastHolo.HologramData;
import com.gmail.JyckoSianjaya.LastHolo.LastHolo;
import com.gmail.JyckoSianjaya.LastHolo.Utility.Utility;

import java.lang.Double;

public class ArmorStand_13 {
/*	private HologramData old(Location loc, String name) {
		World w = ((CraftWorld) loc.getWorld()).getHandle();
		EntityArmorStand armors = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
	// net.minecraft.server.v1_13_R2.Entity.setPosition(java.lang.Double, java.lang.Double, java.lang.Double)
		armors.setPosition(loc.getX(), loc.getY(), loc.getZ());
		w.addEntity(armors);
		ArmorStand armor = (ArmorStand) armors.getBukkitEntity();
		armor.setVisible(false);
		armor.setBasePlate(false);
		armor.setCustomName(Utility.TransColor(name));
		armor.setCustomNameVisible(true);
		if (name == null || name.length() <= 0) {
			armor.setCustomNameVisible(false);
		}
		armor.setInvulnerable(true);
		armor.setMarker(true);
		armor.setSmall(true);
		armor.setGravity(false);
		return HologramData.createHoloData(armor, name);
	}*/
	public static HologramData createHoloData(Location loc, String name) {
		ArmorStand armor = LastHolo.getInstance().getNMS().getArmorStand(loc);
		armor.setVisible(false);
		armor.setBasePlate(false);
		armor.setCustomName(Utility.TransColor(name));
		armor.setCustomNameVisible(true);
		armor.setMarker(true);
		armor.setSmall(true);
		armor.setGravity(false);
		return HologramData.createHoloData(armor, name);
		/*
		ArmorStand armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setVisible(false);
		armor.setMarker(true);
		armor.setBasePlate(false);
		armor.setGravity(false);
		armor.setSmall(true);
		armor.setCustomName(Utility.TransColor(name));
		armor.setCustomNameVisible(true);
		return new HologramData(armor, name);*/
	}
}
