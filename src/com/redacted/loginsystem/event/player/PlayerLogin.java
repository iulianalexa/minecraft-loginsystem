package com.redacted.loginsystem.event.player;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.redacted.loginsystem.InventorySerialization;
import com.redacted.loginsystem.Main;

import net.md_5.bungee.api.ChatColor;

public class PlayerLogin implements Listener {
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		String playerName = player.getName();
		player.sendMessage(ChatColor.RED + "Use /token [token] to log in. No account? Enter https://www.[redacted].com/minecraft");
		ItemStack[] inv = player.getInventory().getContents();
		String str = "";
		for (int i=0;i<inv.length;i++) {
			if (inv[i] == null || inv[i].getType().equals(Material.AIR)) {
				str = str + "{},";
			} else {
				String ser = InventorySerialization.serialize(inv[i]);
				str = str + ser + ",";
			}
		}
		str = str.substring(0, str.length() - 1);
		
		ItemStack[] inv2 = player.getInventory().getArmorContents();
		String str2 = "";
		for (int i=0;i<inv2.length;i++) {
			if (inv2[i] == null || inv2[i].getType().equals(Material.AIR)) {
				str2 = str2 + "{},";
			} else {
				
				String ser = InventorySerialization.serialize(inv2[i]);
				str2 = str2 + ser + ",";
			}
		}
		str2 = str2.substring(0, str2.length() - 1);
		
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		String sqlurl = "jdbc:sqlite:LoginSystem/inventories.db";
		try {
			java.sql.Connection conn = DriverManager.getConnection(sqlurl);
			Statement sqlstmt = conn.createStatement();
			
			sqlstmt.execute("CREATE TABLE IF NOT EXISTS inventory (username TEXT, inv TEXT)");
			sqlstmt.execute("CREATE TABLE IF NOT EXISTS armor (username TEXT, inv TEXT)");
			sqlstmt.execute("CREATE TABLE IF NOT EXISTS xp (username TEXT, val INT)");
			sqlstmt.execute("CREATE TABLE IF NOT EXISTS coords (username TEXT, worldName TEXT, valx INT, valy INT, valz INT)");
			ResultSet sqlrs = sqlstmt.executeQuery("SELECT * FROM inventory WHERE username = '" + playerName + "'");
			if (!sqlrs.next()) {
				sqlstmt.execute("INSERT INTO inventory (username, inv) VALUES ('" + playerName + "', '" + str + "')");
				
				
			}
			
			ResultSet sqlrs2 = sqlstmt.executeQuery("SELECT * FROM armor WHERE username = '" + playerName + "'");
			if (!sqlrs2.next()) {
				sqlstmt.execute("INSERT INTO armor (username, inv) VALUES ('" + playerName + "', '" + str2 + "')");
			
			}
			
			int xp = player.getTotalExperience();
			ResultSet sqlrs3 = sqlstmt.executeQuery("SELECT * FROM xp WHERE username = '" + playerName + "'"); 
			if (!sqlrs3.next()) {
				sqlstmt.execute("INSERT INTO xp (username, val) VALUES ('" + playerName + "', '" + xp + "')");
			}
			
			int valx = player.getLocation().getBlockX();
			int valy = player.getLocation().getBlockY() + 3;
			int valz = player.getLocation().getBlockZ();
			String worldname = player.getLocation().getWorld().getName();
			
			int valx2 = Bukkit.getServer().getWorld(worldname).getSpawnLocation().getBlockX();
			int valy2 = Bukkit.getServer().getWorld(worldname).getSpawnLocation().getBlockY() + 3;
			int valz2 = Bukkit.getServer().getWorld(worldname).getSpawnLocation().getBlockZ();
			
			
			
			ResultSet sqlrs4 = sqlstmt.executeQuery("SELECT * FROM coords WHERE username = '" + playerName + "'");
			if (!sqlrs4.next()) {
				sqlstmt.execute("INSERT INTO coords (username, worldName, valx, valy, valz) VALUES ('" + playerName + "', '" + worldname + "', '" + valx + "', '" + valy + "', '" + valz + "')");
			}
			
			Location loc = new Location(Bukkit.getServer().getWorld(worldname), (double) valx2, (double) valy2, (double) valz2);
			
			player.teleport(loc);
			
			player.setTotalExperience(0);
			
			sqlrs4.close();
			sqlrs3.close();
			sqlrs2.close();
			sqlrs.close();
			sqlstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		List<String> notLoggedIn = Main.getLoggedP();
		if (!notLoggedIn.contains(playerName)) {
			notLoggedIn.add(playerName);
			Main.setLoggedP(notLoggedIn);
		}
	}
}
