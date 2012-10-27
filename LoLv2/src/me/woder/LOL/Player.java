package me.woder.LOL;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Player {
	LOL lol;
	
	public Player(LOL lol){
		this.lol = lol;
	}
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	public void sendAll(int order, String message, String message2){
		if(order == 1){
			Iterator it = lol.blue.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        String its = (String)pairs.getKey();
		        org.bukkit.entity.Player player = Bukkit.getPlayer(its);
		        player.sendMessage(ChatColor.GREEN + message);
		    }
		    Iterator is = lol.purple.entrySet().iterator();
		    while (is.hasNext()) {
		        Map.Entry pairs = (Map.Entry)is.next();
		       // System.out.println(pairs.getKey() + " = " + pairs.getValue());
		        String its = (String)pairs.getKey();
		        org.bukkit.entity.Player player = Bukkit.getPlayer(its);
		        player.sendMessage(ChatColor.RED + message2);
		    }
		}else{
			Iterator it = lol.blue.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        String its = (String)pairs.getKey();
		        org.bukkit.entity.Player player = Bukkit.getPlayer(its);
		        player.sendMessage(ChatColor.GREEN + message);
		    }
		    Iterator is = lol.purple.entrySet().iterator();
		    while (is.hasNext()) {
		        Map.Entry pairs = (Map.Entry)is.next();
		       // System.out.println(pairs.getKey() + " = " + pairs.getValue());
		        String its = (String)pairs.getKey();
		        org.bukkit.entity.Player player = Bukkit.getPlayer(its);
		        player.sendMessage(ChatColor.RED + message2);
		    }
		}
	}

}
