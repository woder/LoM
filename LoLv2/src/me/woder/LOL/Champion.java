package me.woder.LOL;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.entity.Player;


public class Champion implements Runnable {
	
	public void tick(){
		
	}
	
	public Champion getChampion(){
		return null;		
	}
	
	public void setUltion(){
		
	}
	
	public int getUltiCoolDown(){
		return -1;
	}
	
	public void setUlticooldown(int number){
		
	}
	
	public void setUltiused(){
		
	}
	
	public boolean isUltiDone(){
		return false;
	}
	
	public boolean isUltiActivated(){
		return false;
	}
	
	public void addDeath(){
		
	}
	
	public void addkill(){
		
	}
	
	public void addturret(){
		
	}
	
	public int getDeath(){
	    return -1;	
	}
	
	public int getKill(){
		return -1;
	}
	
	public int getTurret(){
		return -1;
	}
	
	public void setAd(int ad){
		
	}
	
	public void setAp(int ap){
		
	}
	
	public int getAd(){
		return 0;
	}
	
	public int getAp(){
		return 0;
	}
	
	public HealthPlayer getHealth(){
		return null;
	}
	
	public void setPlayer(Player p) {
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void run() {
		Iterator it = LOL.skill.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	       // System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        Champion its = (Champion)pairs.getValue();
	        its.tick();
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}
}
