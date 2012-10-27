package me.woder.LOL;

import org.bukkit.block.Block;

public class Nexus {
	public int team = 0;
	public int health = 3;
	public LOL lol;
	
	public Nexus(int team, LOL lol){
		this.team = team;
		this.lol = lol;
	}
	
	public void destroyPart(Block block){
	  if(health-1 < 1){
		int other = 0;
		if(team == 0){
		   other = 1;
		}
		lol.gameEnd(other);
		block.getWorld().createExplosion(block.getLocation(), 0);
	  }else{
		health = health - 1;
		me.woder.LOL.Player player = new me.woder.LOL.Player(lol);
		player.sendAll(team, "Nexus health at " + health + "/3!", "Nexus health at " + health + "/3!");
	  }
	}

}
