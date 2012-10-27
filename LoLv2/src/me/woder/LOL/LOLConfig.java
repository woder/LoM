package me.woder.LOL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LOLConfig {

	private LOL plugin;
	String configName;
	File configFile;
    FileConfiguration config;
    
    public boolean getDebug(){
    	return config.getBoolean("lol.debug");
    }
    
    public void setDebug(String d){
    	config.set("lol.debug", d);
    	saveYamls();
    }

    public void setBnexustop(Location loc){
    	config.set("arena.locations.bnexustop.X", loc.getBlockX());
    	config.set("arena.locations.bnexustop.Y", loc.getBlockY());
    	config.set("arena.locations.bnexustop.Z", loc.getBlockZ());
    	saveYamls();
    }

    public Location getBnexustop(){
    	int X = config.getInt("arena.locations.bnexustop.X");
    	int Y = config.getInt("arena.locations.bnexustop.Y");
    	int Z = config.getInt("arena.locations.bnexustop.Z");
    	return new Location(plugin.getServer().getWorld("world"),X,Y,Z);
    }

    public void setBnexusmid(Location loc){
    	config.set("arena.locations.bnexusmid.X", loc.getBlockX());
    	config.set("arena.locations.bnexusmid.Y", loc.getBlockY());
    	config.set("arena.locations.bnexusmid.Z", loc.getBlockZ());
    	saveYamls();
    }

    public void setBnexusbot(Location loc){
    	config.set("arena.locations.bnexusbot.X", loc.getBlockX());
    	config.set("arena.locations.bnexusbot.Y", loc.getBlockY());
    	config.set("arena.locations.bnexusbot.Z", loc.getBlockZ());
    	saveYamls();
    }

    public Location getBnexusmid(){
    	int X = config.getInt("arena.locations.bnexusmid.X");
    	int Y = config.getInt("arena.locations.bnexusmid.Y");
    	int Z = config.getInt("arena.locations.bnexusmid.Z");
    	return new Location(plugin.getServer().getWorld("world"),X,Y,Z);
    }

    public Location getBnexusbot(){
    	int X = config.getInt("arena.locations.bnexusbot.X");
    	int Y = config.getInt("arena.locations.bnexusbot.Y");
    	int Z = config.getInt("arena.locations.bnexusbot.Z");
    	return new Location(plugin.getServer().getWorld("world"),X,Y,Z);
    }

    public void setPurpleSpawn(Location loc){
    	config.set("arena.locations.purpleSpawn.X", loc.getBlockX());
    	config.set("arena.locations.purpleSpawn.Y", loc.getBlockY());
    	config.set("arena.locations.purpleSpawn.Z", loc.getBlockZ());
    	saveYamls();
    }

    public void setBlueSpawn(Location loc){
    	config.set("arena.locations.blueSpawn.X", loc.getBlockX());
    	config.set("arena.locations.blueSpawn.Y", loc.getBlockY());
    	config.set("arena.locations.blueSpawn.Z", loc.getBlockZ());
    	saveYamls();
    }

    public Location getPurpleSpawn(){
    	int X = config.getInt("arena.locations.purpleSpawn.X");
    	int Y = config.getInt("arena.locations.purpleSpawn.Y");
    	int Z = config.getInt("arena.locations.purpleSpawn.Z");
    	return new Location(plugin.getServer().getWorld("world"),X,Y,Z);
    }

    public Location getBlueSpawn(){
    	int X = config.getInt("arena.locations.blueSpawn.X");
    	int Y = config.getInt("arena.locations.blueSpawn.Y");
    	int Z = config.getInt("arena.locations.blueSpawn.Z");
    	return new Location(plugin.getServer().getWorld("world"),X,Y,Z);
    }

    public void setPnexustop(Location loc){
    	config.set("arena.locations.pnexustop.X", loc.getBlockX());
    	config.set("arena.locations.pnexustop.Y", loc.getBlockY());
    	config.set("arena.locations.pnexustop.Z", loc.getBlockZ());
    	saveYamls();
    }

    public Location getPnexustop(){
    	int X = config.getInt("arena.locations.pnexustop.X");
    	int Y = config.getInt("arena.locations.pnexustop.Y");
    	int Z = config.getInt("arena.locations.pnexustop.Z");
    	return new Location(plugin.getServer().getWorld("world"),X,Y,Z);
    }

    public void setPnexusmid(Location loc){
    	config.set("arena.locations.pnexusmid.X", loc.getBlockX());
    	config.set("arena.locations.pnexusmid.Y", loc.getBlockY());
    	config.set("arena.locations.pnexusmid.Z", loc.getBlockZ());
    	saveYamls();
    }

    public void setPnexusbot(Location loc){
    	config.set("arena.locations.pnexusbot.X", loc.getBlockX());
    	config.set("arena.locations.pnexusbot.Y", loc.getBlockY());
    	config.set("arena.locations.pnexusbot.Z", loc.getBlockZ());
    	saveYamls();
    }

    public Location getPnexusmid(){
    	int X = config.getInt("arena.locations.pnexusmid.X");
    	int Y = config.getInt("arena.locations.pnexusmid.Y");
    	int Z = config.getInt("arena.locations.pnexusmid.Z");
    	return new Location(plugin.getServer().getWorld("world"),X,Y,Z);
    }

    public Location getPnexusbot(){
    	int X = config.getInt("arena.locations.pnexusbot.X");
    	int Y = config.getInt("arena.locations.pnexusbot.Y");
    	int Z = config.getInt("arena.locations.pnexusbot.Z");
    	return new Location(plugin.getServer().getWorld("world"),X,Y,Z);
    }

    public void setRespawnbox(Location loc){
    	config.set("arena.locations.respawn.X", loc.getBlockX());
    	config.set("arena.locations.respawn.Y", loc.getBlockY());
    	config.set("arena.locations.respawn.Z", loc.getBlockZ());
    	saveYamls();
    }
    
    public Location getRespawnbox(){
    	int X = config.getInt("arena.locations.respawn.X");
    	int Y = config.getInt("arena.locations.respawn.Y");
    	int Z = config.getInt("arena.locations.respawn.Z");
    	return new Location(plugin.getServer().getWorld("world"),X,Y,Z);
    }
    
    public String getTwoKill(){
    	return config.getString("lol.general.combat.TwoKill");
    }
    
    public String getThreeKill(){
    	return config.getString("lol.general.combat.ThreeKill");
    }
    
    public String getFourKill(){
    	return config.getString("lol.general.combat.FourKill");
    }
    
    public String getFiveKill(){
    	return config.getString("lol.general.combat.FiveKill");
    }
    
    public String getSixKillPlus(){
    	return config.getString("lol.general.combat.SixKillPlus");
    }
    
    public void setTwoKill(String m){
    	config.set("lol.general.combat.TwoKill", m);
    	saveYamls();
    }
    
    public void setThreeKill(String m){
    	config.set("lol.general.combat.ThreeKill", m);
    	saveYamls();
    }
    
    public void setFourKill(String m){
    	config.set("lol.general.combat.FourKill", m);
    	saveYamls();
    }
    
    public void setFiveKill(String m){
    	config.set("lol.general.combat.FiveKill", m);
    	saveYamls();
    }
    
    public void setSixKillPlus(String m){
    	config.set("lol.general.combat.SixKillPlus", m);
    }
    
    public boolean getFriendlyFire(){
    	return config.getBoolean("lol.general.combat.friendlyFire");
    }
    
    public void setFriendlyFire(String f){
    	config.set("lol.general.combat.friendlyFire", f);
    	saveYamls();
    }
       

    //==============================================================================================

	public LOLConfig(LOL plugin, String configName){
		this.plugin = plugin;
		this.configName = configName;

		configFile = new File(plugin.getDataFolder(), configName);
		try{
			firstRun();
		} catch (Exception e){
			e.printStackTrace();
		}
		config = new YamlConfiguration();
		loadYamls();
	}

	public void saveYamls() {
	    try {
	        config.save(configFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public void loadYamls() {
	    try {
	        config.load(configFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void firstRun() throws Exception {
	    if(!configFile.exists()){
	        configFile.getParentFile().mkdirs();
	        copy(plugin.getResource(configName), configFile);
	    }
	}
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
