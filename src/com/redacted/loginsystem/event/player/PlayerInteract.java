package com.redacted.loginsystem.event.player;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.redacted.loginsystem.Main;

public class PlayerInteract implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		List<String> notLoggedIn = Main.getLoggedP();
		if (notLoggedIn.contains(playerName) && !event.isCancelled()) {
			event.setCancelled(true);
		}
	}
}
