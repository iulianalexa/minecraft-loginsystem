package com.redacted.loginsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InventorySerialization {
	
	@SuppressWarnings("unchecked")
	public static String serialize(ItemStack is) {
		JSONObject obj = new JSONObject();
		obj.put("name", is.getType().toString());
		obj.put("amount", Integer.toString(is.getAmount()));
		obj.put("durability", Short.toString(is.getDurability()));
		JSONArray array = new JSONArray();
		Map<Enchantment, Integer> enchs = is.getEnchantments();
		for (Map.Entry<Enchantment, Integer> entry : enchs.entrySet()) {
			JSONObject newobj = new JSONObject();
			newobj.put("id", entry.getKey().getName());
			newobj.put("lvl", entry.getValue());
			array.add(newobj);
		}
		if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
			obj.put("displayName", is.getItemMeta().getDisplayName());
		} else {
			obj.put("displayName", "");
		}
		
		JSONArray arrayLore = new JSONArray();
		if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
			List<String> lores = is.getItemMeta().getLore();
			for (int i=0;i<lores.size();i++) {
				arrayLore.add(lores.get(i));
				
			}
		}

		obj.put("ench", array);
		obj.put("lore", arrayLore);
		return obj.toJSONString();
	}
	
	public static ItemStack deserialize(String str) {
		JsonObject obj = new JsonParser().parse(str).getAsJsonObject();
		String name = obj.get("name").getAsString();
		Material mat = Material.getMaterial(name);
		String displayName = obj.get("displayName").getAsString();
		int amount = Integer.parseInt(obj.get("amount").getAsString());
		short durability = Short.parseShort(obj.get("durability").getAsString());
		ItemStack is = new ItemStack(mat, amount, durability);
		JsonArray enchArray = obj.get("ench").getAsJsonArray();
		Map<Enchantment, Integer> enchs = new HashMap<Enchantment, Integer>();
		for (int i=0;i<enchArray.size();i++) {
			Enchantment ench = Enchantment.getByName(enchArray.get(i).getAsJsonObject().get("id").getAsString());
			int lvl = enchArray.get(i).getAsJsonObject().get("lvl").getAsInt();
			enchs.put(ench, lvl);
		}
		
		JsonArray loreArray = obj.get("lore").getAsJsonArray();
		List<String> lores = new ArrayList<String>();
		for (int i=0;i<loreArray.size();i++) {
			String lore = loreArray.get(i).getAsString();
			lores.add(lore);
		}
		is.addEnchantments(enchs);
		ItemMeta itemMeta = is.getItemMeta();
		if (!lores.isEmpty()) {
			itemMeta.setLore(lores);
		}
		if (!displayName.equals("")) {
			itemMeta.setDisplayName(displayName);
		}
		is.setItemMeta(itemMeta);
		return is;
		
	}
	
	public static List<String> splitJson(String bigJson) {
		List<String> list = new ArrayList<String>();
		boolean cons = false;
		int lev = 0;
		String str = "";
		for (int i=0;i<bigJson.length();i++) {
			char chr = bigJson.charAt(i);
			if (chr == '{' && !cons) {
				cons = true;
				str = str + chr;
				lev++;
			} else if (chr == '{' && cons) {
				lev++;
				str = str + chr;
			} else if (chr == '}' && cons) {
				lev--;
				str = str + chr;
				if (lev == 0) {
					cons = false;
					list.add(str);
					str = "";
				}
			} else if (cons) {
				str = str + chr;
			}
		}
		
		return list;
	}
}
