package com.gmail.JyckoSianjaya.LastHolo;

import org.bukkit.Location;

import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import com.gmail.JyckoSianjaya.LastHolo.NMS.ArmorStand_10;
import com.gmail.JyckoSianjaya.LastHolo.NMS.ArmorStand_11;
import com.gmail.JyckoSianjaya.LastHolo.NMS.ArmorStand_12;
import com.gmail.JyckoSianjaya.LastHolo.NMS.ArmorStand_13;
import com.gmail.JyckoSianjaya.LastHolo.NMS.ArmorStand_8;
import com.gmail.JyckoSianjaya.LastHolo.NMS.ArmorStand_9;
import com.gmail.JyckoSianjaya.LastHolo.Storage.DataStorage;
import com.gmail.JyckoSianjaya.LastHolo.Storage.DataStorage.ServerVersion;
import com.gmail.JyckoSianjaya.LastHolo.Utility.*;

public class HologramData {
	private ArmorStand armorstand;
	private String data;
	private HologramData(ArmorStand armor, String data) {
		this.armorstand = armor;
		this.data = data;
	}
	public static HologramData createHoloData(Location loc, String name) {
		DataStorage data = DataStorage.getInstance();
		if (data.isSpecialArmorStandUsed()) {
		switch (data.getServerVersion()) {
		case V1_8:
			return ArmorStand_8.createHoloData(loc, name);
		case V1_9:
			return ArmorStand_9.createHoloData(loc, name);
		case V1_10:
			return ArmorStand_10.createHoloData(loc, name);
		case V1_11:
			return ArmorStand_11.createHoloData(loc, name);
		case V1_12:
			return ArmorStand_12.createHoloData(loc, name);
		case V1_13:
			return ArmorStand_13.createHoloData(loc, name);
		default:
			return null;
		}
		}
		ArmorStand armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setMarker(true);
		armor.setVisible(false);
		armor.setInvulnerable(true);
		armor.setCustomName(Utility.TransColor(name));
		armor.setCustomNameVisible(true);
		armor.setBasePlate(false);
		armor.setSmall(true);
		armor.setGravity(false);
		return new HologramData(armor, name);
		}
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
	public static HologramData createHoloData(ArmorStand armor, String name) {
		return new HologramData(armor, name);
	}
	public String getLine() {
		return data;
	}
	public ArmorStand getArmorStand() {
		return this.armorstand;
	}
}
