package com.gmail.JyckoSianjaya.LastHolo.Utility;

import java.util.List;



import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class WG_13 {
	public static boolean allowedRegion(Player p, org.bukkit.World w, Location loc, List<String> aregions) {
		World wrr = BukkitAdapter.adapt(p).getWorld();
		RegionContainer rg = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager manager = rg.get(wrr);
		BlockVector3 locz = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
		ApplicableRegionSet regs = manager.getApplicableRegions(locz);
		if (regs.size() <= 0) {
			return false;
		}	
		for (String g : aregions) {
			for (final ProtectedRegion reg : regs) {
				if (!reg.getId().equalsIgnoreCase(g)) {
					return true;
				}
			}
		}
		return false;
	}
	public static boolean disabledRegion(Player p, org.bukkit.World w, Location loc, List<String> dreg) {
		World wrr = BukkitAdapter.adapt(p).getWorld();
		RegionContainer rg = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager manager = rg.get(wrr);
		BlockVector3 locz = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());

		ApplicableRegionSet regs = manager.getApplicableRegions(locz);
		if (regs.size() <= 0) {
			return false;
		}
		for (String g : dreg) {
			for (final ProtectedRegion reg : regs) {
				if (reg.getId().equalsIgnoreCase(g)) {
					return true;
				}
			}
		}
	return false;
	}
}
