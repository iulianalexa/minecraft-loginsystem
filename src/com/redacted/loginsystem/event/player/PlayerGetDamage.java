package com.redacted.loginsystem.event.player;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.redacted.loginsystem.Main;

public class PlayerGetDamage implements Listener {
	@EventHandler
	public void onPlayerGetDamage(EntityDamageEvent event) {
		Entity damaged = event.getEntity();
		if (damaged instanceof Player) {
			Player player = (Player) damaged;
			String playerName = player.getName();
			List<String> notLoggedIn = Main.getLoggedP();
			if (notLoggedIn.contains(playerName)) {
				event.setCancelled(true);
			}
		}
	}
}
