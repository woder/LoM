package me.woder.LOL;

import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerListner extends TimerTask{
	   public static LOL plugin;
		
		public PlayerListner(LOL instance) {
			plugin = instance;
		}

		@Override
		public void run() {
			Player[] players = Bukkit.getServer().getOnlinePlayers();
			for(int w = 0; w < players.length; w++){
				final Player player = players[w];
				final Location location = player.getLocation();
				int X = location.getBlockX();
				int Y = location.getBlockY();
				int Z = location.getBlockZ();
			
				for(int ix = -10; ix < 10; ix++){
					for(int iy = -10; iy < 10; iy++){
						for(int iz = -10; iz < 10; iz++){
							if(player.getWorld().getBlockTypeIdAt(X+ix, Y+iy, Z+iz) == 21 && !LOL.blue.containsKey(player.getName())){
							  if(LOL.InRange(X+ix, Y+iy, Z+iz, X, Y, Z, 7)){
								LOL.shootArrow(X+ix, Y+iy, Z+iz, player, false);
							  }
							}else if(((player.getWorld().getBlockTypeIdAt(X+ix, Y+iy, Z+iz) == 73 || player.getWorld().getBlockTypeIdAt(X+ix, Y+iy, Z+iz) == 74)) && !LOL.purple.containsKey(player.getName())){
								if(LOL.InRange(X+ix, Y+iy, Z+iz, X, Y, Z, 7)){
									LOL.shootArrow(X+ix, Y+iy, Z+iz, player, true);
								}
							}else if(player.getWorld().getBlockTypeIdAt(X+ix, Y+iy, Z+iz) == 22 && !LOL.blue.containsKey(player.getName())){
								LOL.shootArrow(X+ix, Y+iy, Z+iz, player, false);
							}else if(player.getWorld().getBlockTypeIdAt(X+ix, Y+iy, Z+iz) == 45 && !LOL.purple.containsKey(player.getName())){
								LOL.shootArrow(X+ix, Y+iy, Z+iz, player, true);
							}
						}
					}
				}	
			}
			
		}

}
