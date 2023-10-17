package com.redacted.loginsystem.event.player;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.redacted.loginsystem.Main;

public class PlayerDamage implements Listener {
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		if (damager instanceof Player) {
			Player damagingPlayer = (Player) damager;
			String damagingPlayerName = damagingPlayer.getName();
			List<String> notLoggedIn = Main.getLoggedP();
			if (notLoggedIn.contains(damagingPlayerName)) {
				event.setCancelled(true);
			}
		}
	}
}
