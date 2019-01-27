package com.gmail.JyckoSianjaya.LastHolo.NMS;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

import com.gmail.JyckoSianjaya.LastHolo.HologramData;
import com.gmail.JyckoSianjaya.LastHolo.LastHolo;
import com.gmail.JyckoSianjaya.LastHolo.Storage.DataStorage;
import com.gmail.JyckoSianjaya.LastHolo.Utility.Utility;

public class NMSWrapper {
	private Class<?> craftworld;
	private Class<?> nmsworld;
	private Class<?> entityarmorstand;
	private Class<?> entity;
	private Class<?> entityliving;
	private Method method_getbukkitentity;
	private Method method_setposition;
	private Method method_addEntity;
	private Constructor<?> entityarmorstand_constructor;
	private String nmspackage;
	private String craftbukkitpackage;
	private LastHolo maiinstance;
	public NMSWrapper(LastHolo instance) {
		this.maiinstance = instance;
		checkNMS();
		loadClasses();
	}
	public Class<?> getCraftWorld() {
		return this.craftworld;
	}
	/*
	 * 		World w = ((CraftWorld) loc.getWorld()).getHandle();
		EntityArmorStand armors = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
		armors.setPosition(loc.getX(), loc.getY(), loc.getZ());
		w.addEntity(armors);
		ArmorStand armor = (ArmorStand) armors.getBukkitLivingEntity();
		armor.setVisible(false);
		armor.setBasePlate(false);
		armor.setCustomName(Utility.TransColor(name));
		armor.setCustomNameVisible(true);
		armor.setInvulnerable(true);
		armor.setMarker(true);
		armor.setSmall(true);
		armor.setGravity(false);
		return HologramData.createHoloData(armor, name);
	 */
	public ArmorStand getArmorStand(Location loc) {
		Object armor = null;
		Object world = this.getObjectNMSWorld(loc.getWorld());
		ArmorStand armorstand = null;
		try {
			armor = this.entityarmorstand_constructor.newInstance(world, loc.getX(), loc.getY(), loc.getZ());
			this.method_setposition.invoke(armor, loc.getX(), loc.getY(), loc.getZ());
			this.method_addEntity.invoke(world, armor);
			armorstand = (ArmorStand) this.method_getbukkitentity.invoke(armor);
		} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
			return null;
		}
		return armorstand;

	}
	public Object getObjectNMSWorld(World w) {
		Object craftworld = this.craftworld.cast(w);
		Method m = null;
		try {
			m = this.craftworld.getMethod("getHandle");
			return m.invoke(craftworld);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		
		}
		
	}
	private enum Type {
		CraftWorld,
		NMSWorld,
		EntityArmorStand;
	}
	public Class<?> getNMSWorld() {
		return this.nmsworld;
	}
	public Class<?> getEntityArmorStand() {
		return this.entityarmorstand;
	}
	private void loadMethods() {
		try {
		this.method_addEntity = this.nmsworld.getMethod("addEntity", this.entity);
		} catch (NoSuchMethodException e) {
			Utility.sendConsole("&cLastHolo > &7Reflection found error! Will be using Bukkit's Normal Method. &e(Error Code: Missing_Method-addEntity)");
			DataStorage.getInstance().setSpecialArmor(false);
			return;
		}
		
		try {
			this.method_getbukkitentity = this.entity.getMethod("getBukkitLivingEntity");

		} catch (NoSuchMethodException e) {
			try {
				this.method_getbukkitentity = this.entity.getMethod("getBukkitEntity");
			} catch (NoSuchMethodException e1) {
				Utility.sendConsole("&cLastHolo > &7Reflection error! &7Will be using Bukkit's! &e(Error Code: Missing_Method-getBukkitEntity-getBukkitLivingEntity)");
				DataStorage.getInstance().setSpecialArmor(false);
				return;
			}
		}
		try {
			this.method_setposition = this.entity.getMethod("setPosition", double.class, double.class, double.class);
		} catch (NoSuchMethodException e) {
			try {
				this.method_setposition = this.entityarmorstand.getMethod("setPosition", double.class, double.class, double.class);
			} catch (NoSuchMethodException ef) {
			Utility.sendConsole("&cLastHolo > &7Reflection error! &7Will be using Bukkit's! &e(Error Code: Missing_Method-setPosition)");
			DataStorage.getInstance().setSpecialArmor(false);
			return;
			}
		}
		try {
			this.entityarmorstand_constructor = this.entityarmorstand.getConstructor(this.nmsworld, double.class, double.class, double.class);
		} catch (NoSuchMethodException | SecurityException e) {
			Utility.sendConsole("&cLastHolo > &7Reflection error! &7Will be using Bukkit's! &e(Error Code: Constructor_Missing)");
			DataStorage.getInstance().setSpecialArmor(false);
			return;
		}
	}
	private void loadClasses() {
		this.craftworld = this.getCBClass("CraftWorld");
		this.nmsworld = this.getNMSClass("World");
		this.entityarmorstand = this.getNMSClass("EntityArmorStand");
		this.entity = this.getNMSClass("Entity");
		this.entityliving = this.getNMSClass("EntityLiving");
		this.loadMethods();
	}
	private void checkNMS() {
		this.craftbukkitpackage = "org.bukkit.craftbukkit." + Utility.getNMSVersion() + ".";
		this.nmspackage = "net.minecraft.server." + Utility.getNMSVersion() + ".";
	}
	public Class<?> getCBClass(String name) {
		try {
			return Class.forName(this.craftbukkitpackage + name);
		} catch (ClassNotFoundException ex) {
			Utility.sendConsole("&c[LastHolo] &7We found an error! The class name " + name + " doesnt exist! Plugin will not work!");
			return null;
		}
	}
	public Class<?> getNMSClass(String name) {
		try {
			return Class.forName(this.nmspackage + name);
		} catch (ClassNotFoundException e) {
			Utility.sendConsole("&c[LastHolo] &7We found an error! The class name " + name + " doesnt exist! Plugin will not work!");
			return null;
		}
	}
	
}
