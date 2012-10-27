package me.woder.LOL;

import java.sql.DriverManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import me.woder.AI.*;
import net.minecraft.server.Entity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.sql.*;

public class LOL extends JavaPlugin{
	Logger log = Logger.getLogger("Minecraft");
	public final PlayerListner listner = new PlayerListner(this);
	public final Champion ticks = new Champion();
	public final Listners im = new Listners(this);
	public static HashMap<String, Boolean> purple = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> blue = new HashMap<String, Boolean>();
	//has the format of kill/death/building destroyed/exp
	public static HashMap<String, mysqlStats> stats = new HashMap<String, mysqlStats>();
	//has the format of skill~lastusetime/skill~lastusetime
	public static HashMap<String, Champion> skill = new HashMap<String, Champion>();
	public static Location purplespawn;
	public static Location bluespawn;
	public Nexus pnexus = new Nexus(0, this);
	public Nexus bnexus = new Nexus(1, this);
	public static BukkitScheduler timer = Bukkit.getScheduler();
	public static boolean gamestarted = false;
	public boolean welcome = true;
	public boolean thirty = true;
	public boolean gameisover = false;
	public LOLConfig config;
	public String winningTeam = "";
	public Map<Player, Integer> realHealth = new HashMap<Player, Integer>();
	public Map<Player, Integer> initialHealth = new HashMap<Player, Integer>();

	
	@Override
	public void onDisable() {
	    log.info("LOL has been disabled!");	
	}

	@Override
	public void onEnable() {
		log.info("LOL has been enabled!");
		config = new LOLConfig(this, "config.yml");
	    timer.scheduleSyncRepeatingTask(this, listner, 0, 5);
	    timer.scheduleSyncRepeatingTask(this, ticks, 0, 20);
	    purplespawn = config.getPurpleSpawn();
	    bluespawn = config.getBlueSpawn();
	    timer.scheduleSyncRepeatingTask(this, new Runnable(){
			@Override
			public void run() {
				if((blue.size() == purple.size()) && blue.size() != 0 && purple.size() != 0){
				  if(welcome){
					gameStart();
					welcome = false;
				  }
				}			
			}	    	
	    }, 0, 200);
	    getServer().getPluginManager().registerEvents(im, this);
	    addEntityMap();
	}
	
	@SuppressWarnings("rawtypes")
	public static void shootArrow(int x, int y, int z, Player target, boolean t){
		int px = target.getLocation().getBlockX();
		int py = target.getLocation().getBlockY()+1;
		int pz = target.getLocation().getBlockZ();
	if(t){
	  Collection<Zombie> entities = target.getWorld().getEntitiesByClass(Zombie.class);
	  for (Iterator iter = entities.iterator(); iter.hasNext();) {
		   Zombie zombie = (Zombie) iter.next();
		   Location position = zombie.getLocation();
		   if(InRange(position.getBlockX(), position.getBlockY(), position.getBlockZ(), x, y, z, 6)){
			   px = position.getBlockX();
			   py = position.getBlockY()+1;
			   pz = position.getBlockZ();
			   break;
		   }
	  }
	}else{
		Collection<PigZombie> entities = target.getWorld().getEntitiesByClass(PigZombie.class);
		  for (Iterator iter = entities.iterator(); iter.hasNext();) {
			   PigZombie zombie = (PigZombie) iter.next();
			   Location position = zombie.getLocation();
			   if(InRange(position.getBlockX(), position.getBlockY(), position.getBlockZ(), x, y, z, 6)){
				   px = position.getBlockX();
				   py = position.getBlockY()+1;
				   pz = position.getBlockZ();
				   break;
			   }
		  }	
	}
		Vector v = new Vector( px - x, py - y, pz - z);
		Location from = new Location(target.getWorld(), x, y, z);

		v.normalize();
        float speed = 1;
        float variance = .1f;
		v.multiply(speed);
		
		while (from.getBlock().getType() != Material.AIR) {
			from = from.toVector().add(v.clone().normalize().multiply(.2)).toLocation(target.getWorld());
		}
		
         for(int q = 0; q < 4; q++){
			Arrow a = target.getWorld().spawn(from, Arrow.class);
			a.setVelocity(v.clone().add(getVariance(variance)));
         }
			//this.main.cleaner.register(a, 5000);
			//target.getWorld().playEffect(from, org.bukkit.Effect.BOW_FIRE, 0);
	}
	
	public void respawn(final Player p){
		double seconds = (skill.get(p.getName()).getDeath()*1.7+10);
		p.sendMessage(ChatColor.DARK_RED + "Respawning in:" + seconds + " seconds!");
		timer.scheduleSyncDelayedTask(this, new Runnable(){
			@Override
			public void run() {
			  if(blue.containsKey(p.getName())){
				p.teleport(bluespawn);		
			  }else{
			    p.teleport(purplespawn);
			  }
			}			
		}, (long) (20*seconds));
	}
		
	private static Vector getVariance(float variance) {
		Vector v = new Vector((Math.random() * 2 - 1), (Math.random() * 2 - 1), (Math.random() * 2 - 1)).normalize().multiply(Math.random() * variance);
		return (v);

	}
	
	//Mysql stats! :D
	@SuppressWarnings("rawtypes")
	public void mysqlSave(){
		log.info("Called!");
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "stats";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "java"; 
		String password = "by+79669b3rr2";
		int updateQuery = 0;
		Statement statement = null;
		ResultSet rs = null;
		String sql = "SELECT * from player";
		try {
		  Class.forName(driver).newInstance();
		  conn = DriverManager.getConnection(url+dbName,userName,password);
		  statement = conn.createStatement();
		  log.info("Connected to the database");
		  rs = statement.executeQuery(sql);
			while (rs.next()) {
			  Bukkit.broadcastMessage(rs.getString(1) + "  " + rs.getInt(2) + "  "+rs.getInt(4)+ " " + rs.getInt(5) + "\n");
			  stats.put(rs.getString(1), new mysqlStats(rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(0)));
		    }
		  Iterator it = LOL.skill.entrySet().iterator();
		  String QueryString = "";
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		       // System.out.println(pairs.getKey() + " = " + pairs.getValue());
		        Champion its = (Champion)pairs.getValue();
		        String name = (String)pairs.getKey();
		        int won = 0;
		        if(getWinningTeam() == "blue"){
		          if(LOL.blue.containsKey(name)){
		        	won = 1;
		          }
		        }else if(getWinningTeam() == "purple"){
		          if(LOL.blue.containsKey(name)){
			        	won = 1;
			       }	
		        }
		        if(stats.containsKey(name)){
		        int kill = stats.get(name).kill + its.getKill();
		        int death = stats.get(name).death + its.getDeath();
		        int takedown = stats.get(name).takedown + its.getTurret();
		        int win = stats.get(name).win + won;
		         QueryString = "UPDATE  `stats`.`player` SET  `kill` =  '"+kill+"',`death` = '"+death+"',`takedown` =  '"+takedown+"', `wins` = '"+ win +"' WHERE  `player`.`name` =  '"+name+"'";
		        }else{
		         QueryString = "INSERT INTO `stats`.`player` (`name`, `kill`, `death`, `takedown`, `wins`) VALUES ('"+name+"', '"+its.getKill()+"', '"+its.getDeath()+"', '"+its.getTurret()+"', '"+won+"');";
		        }
		         updateQuery = statement.executeUpdate(QueryString);
		        //it.remove(); // avoids a ConcurrentModificationException
		  }
		  if (updateQuery != 0) {
			  System.out.println("Data was added without error!");
		  }
		  rs.close();
		  conn.close();
		  System.out.println("Disconnected from database");
	    } catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	public void startGame(){
		Bukkit.broadcastMessage(ChatColor.DARK_RED + "Minions have spawned!");
		timer.scheduleSyncRepeatingTask(this, new Runnable(){
			@Override
			public void run() {
				World world = config.getBnexusbot().getWorld();
				net.minecraft.server.World mcWorld = ((CraftWorld) world).getHandle();
				final EntityMinion minion = new EntityMinion(mcWorld, config.getPnexusbot(), 0.23F, false);
				 
	            minion.setPosition(config.getBnexusbot().getX(), config.getBnexusmid().getY(), config.getBnexusbot().getZ());
	            mcWorld.addEntity(minion, SpawnReason.CUSTOM);
	            for(int i = 0; i < 6; i++){
					mcWorld.addEntity(minion, SpawnReason.CUSTOM);
				}
	            
	            
	            final EntityMinionPurp minion2 = new EntityMinionPurp(mcWorld, config.getBnexusbot(), 0.23F, false);
	            minion2.setPosition(config.getBnexusbot().getX(), config.getPnexusmid().getY(), config.getBnexusbot().getZ());
	            mcWorld.addEntity(minion2, SpawnReason.CUSTOM);
	            for(int i = 0; i < 6; i++){
					mcWorld.addEntity(minion2, SpawnReason.CUSTOM);
				}
	            /*EntityMinion minion2 = new EntityMinion(mcWorld);
	            minion2.setPosition(config.getBnexusmid().getX(), config.getBnexusmid().getY(), config.getBnexusmid().getZ());
	            spawn(mcWorld, minion2, SpawnReason.CUSTOM);
	            EntityMinion minion3 = new EntityMinion(mcWorld);
	            minion3.setPosition(config.getBnexustop().getX(), config.getBnexustop().getY(), config.getBnexustop().getZ());
	            spawn(mcWorld, minion3, SpawnReason.CUSTOM);
	            EntityMinion minion4 = new EntityMinion(mcWorld);
	            minion4.setPosition(config.getPnexusbot().getX(), config.getPnexusbot().getY(), config.getPnexusbot().getZ());
	            spawn(mcWorld, minion4, SpawnReason.CUSTOM);
	            EntityMinion minion5 = new EntityMinion(mcWorld);
	            minion5.setPosition(config.getPnexusmid().getX(), config.getPnexusmid().getY(), config.getPnexusmid().getZ());
	            spawn(mcWorld, minion5, SpawnReason.CUSTOM);
	            EntityMinion minion6 = new EntityMinion(mcWorld);
	            minion6.setPosition(config.getPnexustop().getX(), config.getPnexustop().getY(), config.getPnexustop().getZ());
	            spawn(mcWorld, minion6, SpawnReason.CUSTOM);*/
			}
		}, 0, 20*30);
		
		/*World world = config.getBnexusbot().getWorld();
		net.minecraft.server.World mcWorld = ((CraftWorld) world).getHandle();
		final EntityMinion minion = new EntityMinion(mcWorld);	 
        minion.setPosition(config.getBnexusmid().getX(), config.getBnexusmid().getY()+2, config.getBnexusmid().getZ());
        mcWorld.addEntity(minion, SpawnReason.CUSTOM);    
        //minion.move(new Location(world, config.getBnexusmid().getX(), config.getBnexusmid().getY()+2, config.getBnexusmid().getZ()), new Vector(config.getPnexusmid().getX(), config.getPnexusmid().getY()+2, config.getPnexusmid().getZ()));
        gamestarted = true;*/
	}
	
	public void spawn(net.minecraft.server.World mcWorld, Entity minion, SpawnReason reason){
		for(int i = 0; i < 6; i++){
			mcWorld.addEntity(minion, reason);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void gameStart(){
		gameisover = false;
		Iterator it = blue.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	       // System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        String its = (String)pairs.getKey();
	        Player player = Bukkit.getPlayer(its);
	        player.teleport(bluespawn);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	    Iterator is = purple.entrySet().iterator();
	    while (is.hasNext()) {
	        Map.Entry pairs = (Map.Entry)is.next();
	       // System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        String its = (String)pairs.getKey();
	        Player player = Bukkit.getPlayer(its);
	        player.teleport(purplespawn);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
		Bukkit.broadcastMessage(ChatColor.DARK_RED + "Welcome to summoner's rift");
		timer.scheduleAsyncDelayedTask(this, new Runnable(){
			@Override
			public void run() {
				thirtySecond();
			}		
		}, 20*30);
	}
	
	public void thirtySecond(){
		Bukkit.broadcastMessage(ChatColor.DARK_RED + "Thirty seconds til minion spawn!");		
		timer.scheduleAsyncDelayedTask(this, new Runnable(){
			@Override
			public void run() {
				startGame();
			}		
		}, 20*30);
	}
	
	public void endGame(){
		mysqlSave();
		gamestarted = false;
		blue.clear();
		purple.clear();
		skill.clear();
		Player[] players = Bukkit.getOnlinePlayers();
		for(int i = 0; i < players.length; i++){
			players[i].setPlayerListName(players[i].getName());
			players[i].setDisplayName(players[i].getName());
		}
	}
	
	public static boolean InRange(int X1, int Y1, int Z1, int X2, int Y2, int Z2, int Range){
		boolean Output = false;
		double part1 = (Math.pow((X1 - X2), 2) + Math.pow((Z1 - Z2), 2));
		int distence = (int) Math.sqrt(part1);
   
		if(distence <= Range){
			Output = true;
		}	
		return Output;		
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void addEntityMap() {
		try{
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;
 
            Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", args);
            a.setAccessible(true);
 
            a.invoke(a, EntityMinion.class, "Zombie", 54);
            a.invoke(a, EntityMinionPurp.class, "PigZombie", 57);
        }catch (Exception e){
            e.printStackTrace();
            this.setEnabled(false);
        }
	}
	
	public void gameEnd(int winning){
		me.woder.LOL.Player player = new me.woder.LOL.Player(this);
		player.sendAll(winning, "Victory!", "Defeat!");
		gameisover = true;
		if(winning == 1){
			winningTeam = "blue";
		}else{
			winningTeam = "purple";
		}
		endGame();
	}
	
	public static void updateStats(Player player, String data){
	String together = player.getDisplayName() + " " + data;
	String name = player.getDisplayName();
	int keep = 0;
	  while(together.length()-keep > 16){
		  keep++;
	  }
	  //Bukkit.broadcastMessage("So, keep = " + keep + " and output.length() = " + output.length());
	    name = name.substring(0, name.length() - keep);
		player.setPlayerListName(name+" "+data);
	}
	
	public void joinPurple(Player p, CommandSender sender){
      if(purple.size() < 6){
		     if(!blue.containsKey(sender.getName()) && !purple.containsKey(sender.getName())){
		    	 if(blue.size() < purple.size()){
		    	    joinBlue(p, sender);
		    	 }else{
		    	   purple.put(sender.getName(), true);
				   //player.teleport(purplespawn);
				   p.setDisplayName(ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.WHITE);
				   Bukkit.broadcastMessage(ChatColor.YELLOW + sender.getName() + " has joined the " + ChatColor.LIGHT_PURPLE + "purple " + ChatColor.YELLOW + "team!");
				   sender.sendMessage(ChatColor.YELLOW + "You have joined a team!");
				   updateStats(p, "0/0/0");
		    	 }
		     }else{
		    	  sender.sendMessage(ChatColor.YELLOW + "You are already on a team!");
		     }
	  }else{
	    if(blue.size() < 6){
	    	if(!blue.containsKey(sender.getName()) && !purple.containsKey(sender.getName())){
			      joinBlue(p, sender); 
		    }else{
		    	  sender.sendMessage(ChatColor.YELLOW + "You are already on a team!");
		    }
	    }else{
	     sender.sendMessage(ChatColor.YELLOW + "Sorry, both teams are full!");
	    }
	  }
	}
	
	public void joinBlue(Player p, CommandSender sender){
		if(blue.size() < 6){
		     if(!blue.containsKey(sender.getName()) && !purple.containsKey(sender.getName())){
		    	 if(blue.size() > purple.size()){
		    	    joinPurple(p, sender);
		    	 }else{
		    	   blue.put(sender.getName(), true);
				   //player.teleport(purplespawn);
				   p.setDisplayName(ChatColor.BLUE + sender.getName() + ChatColor.WHITE);
				   Bukkit.broadcastMessage(ChatColor.YELLOW + sender.getName() + " has joined the " + ChatColor.BLUE + "blue " + ChatColor.YELLOW + "team!");
				   sender.sendMessage(ChatColor.YELLOW + "You have joined a team!");
				   updateStats(p, "0/0/0");
		    	 }
		     }else{
		    	  sender.sendMessage(ChatColor.YELLOW + "You are already on a team!");
		     }
	  }else{
	    if(purple.size() < 6){
	    	if(!blue.containsKey(sender.getName()) && !purple.containsKey(sender.getName())){
			      joinBlue(p, sender); 
		    }else{
		    	  sender.sendMessage(ChatColor.YELLOW + "You are already on a team!");
		    }
	    }else{
	     sender.sendMessage(ChatColor.YELLOW + "Sorry, both teams are full!");
	    }
	  }
	}
	
	public String getWinningTeam(){
		if(gameisover){
			return winningTeam;
		}
		return "no";
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("join")){ // If the player typed /basic then do the following...
		  if(LOL.skill.containsKey(sender.getName())){
			Random randomGenerator = new Random();
			int num = randomGenerator.nextInt(2);
			if(num == 1){
			  joinBlue(player, sender);
			}else{
			  joinPurple(player, sender);
			}
		  }else{
		    sender.sendMessage(ChatColor.RED + "You have not chosen a champion, do that now with /iam <champion name>");
		  }
			return true;
		}else if(cmd.getName().equalsIgnoreCase("setblue")){
		  if(player.isOp()){
			config.setBlueSpawn(player.getLocation());
			bluespawn = player.getLocation();
			sender.sendMessage(ChatColor.RED + "You have set the blue spawn!");
		  }else{
			  sender.sendMessage(ChatColor.RED + "You have no permission to use this."); 
		  }
		}else if(cmd.getName().equalsIgnoreCase("setpurple")){
		  if(player.isOp()){
			config.setPurpleSpawn(player.getLocation());
			purplespawn = player.getLocation();
			sender.sendMessage(ChatColor.RED + "You have set the purple spawn!");
            
		  }
		}else if(cmd.getName().equalsIgnoreCase("e")){
			if(LOL.skill.containsKey(sender.getName())){
		      if(LOL.skill.get(sender.getName()).isUltiDone()){
		    	  Champion champion = LOL.skill.get(sender.getName()).getChampion();
		    	  champion.setUltion();
		    	 sender.sendMessage(ChatColor.RED + "You have activated your ultimate!");
		      }else{
		    	 Champion champion = LOL.skill.get(sender.getName()).getChampion();
		    	 sender.sendMessage(ChatColor.RED + "You need to wait " + champion.getUltiCoolDown() + " seconds to use that!");
		      }
			}else{
				//why should we ever end up here??
		    	sender.sendMessage(ChatColor.RED + "You have not chosen a champion, do that now with /iam <champion name>");
		    }
		    return true;
		}else if(cmd.getName().equalsIgnoreCase("iam")){
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("ashe")){
				   Ashe ashe = new Ashe();
				   ashe.setPlayer(player);
				   LOL.skill.put(sender.getName(), ashe);
		           player.setHealth(20);
		           sender.sendMessage(ChatColor.YELLOW + "You now have 200 HP");
				   sender.sendMessage(ChatColor.RED + "You have chosen the champion \"Ashe\"");
				   Bukkit.broadcastMessage(player.getDisplayName() + ChatColor.YELLOW + " has chosen Ashe as their champion!");
				}else if(args[0].equalsIgnoreCase("lux")){
				   Lux lux = new Lux();
				   lux.setPlayer(player);
				   LOL.skill.put(sender.getName(), lux);
		           player.setHealth(20);
		           sender.sendMessage(ChatColor.YELLOW + "You now have 210 HP");
				   sender.sendMessage(ChatColor.RED + "You have chosen the champion \"Lux\"");
				   Bukkit.broadcastMessage(player.getDisplayName() + ChatColor.YELLOW + " has chosen Lux as their champion!");
				}else{
				   sender.sendMessage(ChatColor.RED + "I do not know of this champion!");
				}
			}else{
				sender.sendMessage(ChatColor.RED + "Champions are: Lux, Ashe");
			}
			return true;
		}else if(cmd.getName().equalsIgnoreCase("gameend")){
		  if(player.isOp()){
			endGame();
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "The game has been ended by force!");
		  }else{
			sender.sendMessage(ChatColor.DARK_RED + "You have no permission to do this!");
		  }
		}else if(cmd.getName().equalsIgnoreCase("lol")){
			if(args.length > 1){
				if(args[0].equalsIgnoreCase("set")){
				   if(args[1].equalsIgnoreCase("pbot")){
					   Block b = player.getTargetBlock(null, 5);
					   Location loc = b.getLocation();	
					   config.setPnexusbot(loc);
					   sender.sendMessage(ChatColor.YELLOW + "Point set!");
				   }else if(args[1].equalsIgnoreCase("pmid")){
					  Block b = player.getTargetBlock(null, 5);
					  Location loc = b.getLocation();	
					  config.setPnexusmid(loc);
					  sender.sendMessage(ChatColor.YELLOW + "Point set!");
				   }else if(args[1].equalsIgnoreCase("ptop")){
					   Block b = player.getTargetBlock(null, 5);
					   Location loc = b.getLocation();	
					   config.setPnexustop(loc);
					   sender.sendMessage(ChatColor.YELLOW + "Point set!");
				   }else if(args[1].equalsIgnoreCase("bbot")){
					   Block b = player.getTargetBlock(null, 5);
					   Location loc = b.getLocation();	
					   config.setBnexusbot(loc);		   
					   sender.sendMessage(ChatColor.YELLOW + "Point set!");
				   }else if(args[1].equalsIgnoreCase("bmid")){
					   Block b = player.getTargetBlock(null, 5);
					   Location loc = b.getLocation();	
					   config.setBnexusmid(loc);
					   sender.sendMessage(ChatColor.YELLOW + "Point set!");
				   }else if(args[1].equalsIgnoreCase("btop")){
					   Block b = player.getTargetBlock(null, 5);
					   Location loc = b.getLocation();	
					   config.setBnexustop(loc);
					   sender.sendMessage(ChatColor.YELLOW + "Point set!");
				   }
				}
			}
		  return true;
		}
		
		return false; 
	}
	
	
	
}
