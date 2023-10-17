package com.redacted.loginsystem.commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.redacted.loginsystem.InventorySerialization;
import com.redacted.loginsystem.Main;

public class Token implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			List<String> notLoggedIn = Main.getLoggedP();
			if (args.length == 1 && notLoggedIn.contains(player.getName())) {
				
				String playerName = player.getName();
				String token = args[0];
				String url = "jdbc:mysql://localhost:3306/loginsystem";
				String username = "[redacted]";
				String password = "[redacted]";
				try {
					Connection connection = DriverManager.getConnection(url, username, password);
					Statement stmt = connection.createStatement();
					
					ResultSet rs = stmt.executeQuery("SELECT requestIP, availableFrom, availableUntil FROM tokens WHERE username = '" + playerName + "' AND token = '" + token + "'");
					if (rs.next()) {
						long unixTime = System.currentTimeMillis() / 1000L;
						long availableFrom = rs.getLong(2);
						long availableUntil = rs.getLong(3);
						String ipAddress = player.getAddress().getAddress().toString().substring(1);
						String requestIP = rs.getString(1);
						if (ipAddress.equals(requestIP)) {
							if (availableFrom <= unixTime && availableUntil >= unixTime) {
								player.sendMessage("You have logged in!");
								
								if (notLoggedIn.contains(playerName)) {
									notLoggedIn.remove(playerName);
									Main.setLoggedP(notLoggedIn);
								}
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
									ResultSet sqlrs = sqlstmt.executeQuery("SELECT inv FROM inventory WHERE username = '" + player.getName() + "'");
									if (sqlrs.next()) {
										String inv = sqlrs.getString(1);
										List<String> jsons = InventorySerialization.splitJson(inv);
										List<ItemStack> items = new ArrayList<ItemStack>();
										for (int i=0;i<jsons.size();i++) {
											if (jsons.get(i).equals("{}")) {
												items.add(null);
											} else {
												ItemStack item = InventorySerialization.deserialize(jsons.get(i));
												items.add(item);
												
											}
										}
										ItemStack[] itemsArray = new ItemStack[items.size()];
										itemsArray = items.toArray(itemsArray);
										player.getInventory().setContents(itemsArray);
										
										sqlstmt.execute("DELETE FROM inventory WHERE username = '" + playerName + "'");
									}
									
									ResultSet sqlrs2 = sqlstmt.executeQuery("SELECT inv FROM armor WHERE username = '" + player.getName() + "'");
									if (sqlrs2.next()) {
										String inv = sqlrs2.getString(1);
										List<String> jsons = InventorySerialization.splitJson(inv);
										List<ItemStack> items = new ArrayList<ItemStack>();
										for (int i=0;i<jsons.size();i++) {
											if (jsons.get(i).equals("{}")) {
												items.add(null);
											} else {
												ItemStack item = InventorySerialization.deserialize(jsons.get(i));
												items.add(item);
												
											}
										}
										ItemStack[] itemsArray = new ItemStack[items.size()];
										itemsArray = items.toArray(itemsArray);
										player.getInventory().setArmorContents(itemsArray);
										
										sqlstmt.execute("DELETE FROM armor WHERE username = '" + playerName + "'");
									}
									
									
									
									ResultSet sqlrs3 = sqlstmt.executeQuery("SELECT val FROM xp WHERE username = '" + player.getName() + "'");
									if (sqlrs3.next()) {
										int xp = sqlrs3.getInt(1);
										player.setTotalExperience(xp);
										sqlstmt.execute("DELETE FROM xp WHERE username = '" + playerName + "'");
									}
									
									ResultSet sqlrs4 = sqlstmt.executeQuery("SELECT worldName, valx, valy, valz FROM coords WHERE username = '" + player.getName() + "'");
									if (sqlrs4.next()) {
										String worldName = sqlrs4.getString(1);
										int valx = sqlrs4.getInt(2);
										int valy = sqlrs4.getInt(3);
										int valz = sqlrs4.getInt(4);
										Location loc = new Location(Bukkit.getServer().getWorld(worldName), (double) valx, (double) valy, (double) valz);
										player.teleport(loc);
										sqlstmt.execute("DELETE FROM coords WHERE username = '" + playerName + "'");
									}
									
									sqlrs4.close();
									sqlrs3.close();
									sqlrs2.close();
									sqlrs.close();
									sqlstmt.close();
									conn.close();
								} catch (SQLException e) {
									
									e.printStackTrace();
								}
								
								rs.close();
								stmt.close();
								Statement stmt2 = connection.createStatement();
								stmt2.execute("DELETE FROM tokens WHERE username = '" + playerName + "'");
								stmt2.close();
								connection.close();
							} else {
								player.kickPlayer("Expired token.");
							}
						} else {
							Bukkit.getBanList(Type.NAME).addBan(playerName, "Compromised Account", null, "Console");
							Bukkit.getBanList(Type.IP).addBan(ipAddress, "Compromised IP Address", null, "Console");
							Bukkit.getBanList(Type.IP).addBan(requestIP, "Compromised IP Address", null, "Console");
							player.kickPlayer("Compromised Account! Enter www.[redacted].com/minecraft/ban for more info.");
							
							
						}
					} else {
						player.kickPlayer("Incorrect token. No account? Enter www.[redacted].com/minecraft and sign up!");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					player.kickPlayer("Cannot connect to database. Please try again later.");
				}
			}
		} else {
			sender.sendMessage("This command cannot be used in the Console.");
		}
		return true;
	}

}
