package com.gmail.JyckoSianjaya.LastHolo.Utility;

import java.util.List;


import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WG_12 {
	public static boolean allowedRegion(World w, Location loc, List<String> aregions) {
		ApplicableRegionSet gg = WGBukkit.getRegionManager(w).getApplicableRegions(new Vector(loc.getX(), loc.getY(), loc.getZ()));
		for (String g : aregions) {
			if (gg.size() <= 0) return true;
			for (final ProtectedRegion reg : gg) {
				if (!reg.getId().equalsIgnoreCase(g)) {
					return true;
				}
			}
		}
		return false;
	}
	public static boolean disabledRegion(World w, Location loc, List<String> dreg) {
	for (String g : dreg) {
		for (final ProtectedRegion reg : WGBukkit.getRegionManager(w).getApplicableRegions(new Vector(loc.getX(), loc.getY(), loc.getZ()))) {
			if (reg.getId().equalsIgnoreCase(g)) {
				return true;
			}
		}
	}
	return false;
	}
}
