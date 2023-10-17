package com.redacted.loginsystem.event.player;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.redacted.loginsystem.Main;

public class PlayerMove implements Listener {
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		String playerName = player.getName();
		List<String> notLoggedIn = Main.getLoggedP();
		
		if (notLoggedIn.contains(playerName)) {
			int cx1 = event.getFrom().getBlockX();
			int cy1 = event.getFrom().getBlockY();
			int cz1 = event.getFrom().getBlockZ();
			int cx2 = event.getTo().getBlockX();
			int cz2 = event.getTo().getBlockZ();
			if (cx1 != cx2 || cz1 != cz2) {
				Location loc = new Location(player.getWorld(), cx1, cy1, cz1);
				player.teleport(loc);
			}
		}
	}
}
