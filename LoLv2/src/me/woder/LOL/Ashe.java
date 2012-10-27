package me.woder.LOL;


public class Ashe extends Champion{
	
	public org.bukkit.entity.Player p;
	public int ulticooldown = 0;
	public boolean ultiactivated = false;
	public int deathCount = 0;
	public int killCount = 0;
	public int towerkill = 0;
	public int bonusad = 80;
	public int bonusap = 0;
	public HealthPlayer hp;
	
	@Override
	public Champion getChampion(){
		return this;
	}
	
	@Override
	public void setPlayer(org.bukkit.entity.Player p){
		this.p = p;
		hp = new HealthPlayer(this.p, 200, 200, 10);
	}
	
	@Override
	public int getUltiCoolDown(){
		return ulticooldown;
	}
	
	@Override
	public void setUltion(){
		ultiactivated = true;
	}
	
	@Override
    public void setUlticooldown(int number){
		ulticooldown = number;
	}
	
	@Override
	public boolean isUltiActivated(){
		return ultiactivated;
	}

	@Override
	public boolean isUltiDone(){
		if(ulticooldown == 0){
			return true;
		}
		return false;
	}
	
	@Override
    public void setUltiused(){
		ultiactivated = false;
	}
	
	@Override
    public void addDeath(){
		this.deathCount++;
	}
	
	@Override
	public void addkill(){
		this.killCount++;
	}
	
	@Override
	public void addturret(){
		this.towerkill++;
	}
	
	@Override
	public int getDeath(){
	    return this.deathCount;	
	}
	
	@Override
	public int getKill(){
		return this.killCount;
	}
	
	@Override
	public int getTurret(){
		return this.towerkill;
	}
	
	@Override
	public void tick(){
		if(ulticooldown > 0){
			ulticooldown = ulticooldown - 1;
		}
	}
	
	@Override
	public void setAd(int ad){
	  bonusad = ad;
	}
	
	@Override
	public void setAp(int ap){
	  bonusap = ap;
	}
	
	public HealthPlayer getHealth(){
		return hp;
	}
	
}
