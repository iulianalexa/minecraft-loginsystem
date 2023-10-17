package com.redacted.loginsystem;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.redacted.loginsystem.commands.Token;
import com.redacted.loginsystem.event.player.CraftItem;
import com.redacted.loginsystem.event.player.InventoryClick;
import com.redacted.loginsystem.event.player.InventoryCreative;
import com.redacted.loginsystem.event.player.InventoryMove;
import com.redacted.loginsystem.event.player.PlayerBreak;
import com.redacted.loginsystem.event.player.PlayerConnect;
import com.redacted.loginsystem.event.player.PlayerDamage;
import com.redacted.loginsystem.event.player.PlayerDropItem;
import com.redacted.loginsystem.event.player.PlayerGetDamage;
import com.redacted.loginsystem.event.player.PlayerInteract;
import com.redacted.loginsystem.event.player.PlayerInteractEntity;
import com.redacted.loginsystem.event.player.PlayerLogin;
import com.redacted.loginsystem.event.player.PlayerMove;
import com.redacted.loginsystem.event.player.PlayerPickup;
import com.redacted.loginsystem.event.player.PlayerPlace;

public class Main extends JavaPlugin {
	static List<String> notLoggedIn = new ArrayList<String>();
	
	public void onEnable() {
		File f = new File("LoginSystem");
		if (f.isDirectory()) {
			File f2 = new File("LoginSystem" + File.separator + "inventories.db");
			if (!f.isFile()) {
				try {
					f2.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			f.mkdir();
			File f2 = new File("LoginSystem" + File.separator + "inventories.db");
			try {
				f2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Loading LoginSystem...");
		registerCommands();
		registerEvents();
	}
	
	public void onDisable() {
		System.out.println("Unloading LoginSystem...");
	}
	

	public void registerCommands() {
		getCommand("token").setExecutor(new Token());
	}
	
	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerConnect(), this);
		pm.registerEvents(new PlayerLogin(), this);
		pm.registerEvents(new PlayerMove(), this);
		pm.registerEvents(new PlayerPlace(), this);
		pm.registerEvents(new PlayerPickup(), this);
		pm.registerEvents(new InventoryMove(), this);
		pm.registerEvents(new PlayerBreak(), this);
		pm.registerEvents(new PlayerDamage(), this);
		pm.registerEvents(new PlayerGetDamage(), this);
		pm.registerEvents(new PlayerInteract(), this);
		pm.registerEvents(new PlayerInteractEntity(), this);
		pm.registerEvents(new InventoryClick(), this);
		pm.registerEvents(new InventoryCreative(), this);
		pm.registerEvents(new CraftItem(), this);
		pm.registerEvents(new PlayerDropItem(), this);
	}
	
	public static List<String> getLoggedP() {
		return notLoggedIn;
	}
	
	public static void setLoggedP(List<String> nL) {
		notLoggedIn = nL;
	}
	
}
