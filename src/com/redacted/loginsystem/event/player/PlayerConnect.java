package com.redacted.loginsystem.event.player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;


public class PlayerConnect implements Listener {
		
	@EventHandler
	public void onPlayerConnect(PlayerLoginEvent event) {
		String url = "jdbc:mysql://localhost:3306/loginsystem";
		String username = "[redacted]";
		String password = "[redacted]";
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Statement stmt = connection.createStatement();
			Player player = event.getPlayer();
			String playerName = player.getName();
			String query = "SELECT * FROM users WHERE username = '" + playerName + "'";
			ResultSet rs = stmt.executeQuery(query);
			if (!rs.next()) {
				event.disallow(Result.KICK_OTHER, "You have not registered! Enter www.[redacted].com/minecraft and sign up!");
				
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			event.disallow(Result.KICK_OTHER, "Database error.");
		}
		
		
	}
}
