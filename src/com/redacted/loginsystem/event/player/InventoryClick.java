package com.redacted.loginsystem.event.player;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.redacted.loginsystem.Main;

public class InventoryClick implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		List<String> notLoggedIn = Main.getLoggedP();
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			String playerName = player.getName();
			
			if (notLoggedIn.contains(playerName)) {
				event.setCancelled(true);
			}
		}
	}
}
