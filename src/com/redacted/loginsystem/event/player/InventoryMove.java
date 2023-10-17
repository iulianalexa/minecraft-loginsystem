package com.redacted.loginsystem.event.player;

import java.util.List;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

import com.redacted.loginsystem.Main;

public class InventoryMove implements Listener {
	@EventHandler
	public void onPlayerMoveInventoryItem(InventoryInteractEvent event) {
		HumanEntity player = event.getWhoClicked();
		String playerName = player.getName();
		List<String> notLoggedIn = Main.getLoggedP();
		if (notLoggedIn.contains(playerName)) {
			event.setCancelled(true);
		}
	}
}
