package com.gmail.JyckoSianjaya.LastHolo;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import com.gmail.JyckoSianjaya.LastHolo.Utility.*;

public class HologramData {
	private ArmorStand armorstand;
	private String data;
	private HologramData(ArmorStand armor, String data) {
		this.armorstand = armor;
		this.data = data;
	}
	public static HologramData createHoloData(Location loc, String name) {
		ArmorStand armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setVisible(false);
		armor.setMarker(true);
		armor.setBasePlate(false);
		armor.setGravity(false);
		armor.setSmall(true);
		armor.setCustomName(Utility.TransColor(name));
		armor.setCustomNameVisible(true);
		return new HologramData(armor, name);
	}
	public String getLine() {
		return data;
	}
	public ArmorStand getArmorStand() {
		return this.armorstand;
	}
}
