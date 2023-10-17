package com.redacted.loginsystem.event.player;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.redacted.loginsystem.Main;

public class PlayerBreak implements Listener {
	@EventHandler
	public void onPlayerBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		List<String> notLoggedIn = Main.getLoggedP();
		if (notLoggedIn.contains(playerName)) {
			event.setCancelled(true);
		}
	}
}
