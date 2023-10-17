package com.redacted.loginsystem.event.player;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import com.redacted.loginsystem.Main;

public class CraftItem implements Listener {
	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			String playerName = player.getName();
			List<String> notLoggedIn = Main.getLoggedP();
			if (notLoggedIn.contains(playerName)) {
				event.setCancelled(true);
			}
		}
	}
}
