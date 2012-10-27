package me.woder.LOL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class Listners implements Listener {
	public static HashMap<String, EntityDamageEvent> deathCause = new HashMap<String, EntityDamageEvent>();
	public LOL lol;
	
	public Listners(LOL lol) {
		this.lol = lol;
	}


	@EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player))return;

        Player player = (Player)event.getEntity();
        if (LOL.skill.containsKey(player.getName())){
        double extra = 0;
        if (event instanceof EntityDamageByEntityEvent){
          EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent)event;
          Entity damager = subEvent.getDamager();
         if ((damager instanceof Player)) {
            Player attacker = (Player)damager;
            Champion champ = LOL.skill.get(attacker.getName());
            extra = champ.getAd();
         }
        }
        player.sendMessage("So this is normal damage: " + event.getDamage() + " and this is extra: " + extra);
        LOL.skill.get(player.getName()).getHealth().applyDamage((int)(event.getDamage()+extra));
       }
        event.setCancelled(true);
    }   
	
	@EventHandler
	  public void onEntityHeal(EntityRegainHealthEvent event) {
	    if ((event.getEntity() instanceof Player))
	    {
	      Player player = (Player)event.getEntity();
	      if (LOL.skill.containsKey(player.getName())){
	       LOL.skill.get(player.getName()).getHealth().applyDamage(-1 * event.getAmount());
	      }
	      event.setCancelled(true);
	    }
	  }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamageEntity(EntityDamageByEntityEvent event){
	    Entity en = event.getDamager();
	    if((en instanceof Player)) {
            //Player attacker = (Player)en;
            //Champion champ = LOL.skill.get(attacker.getName());
            /*if(champ.isUltiActivated() && (champ.getChampion() instanceof Yarvy)){
            	if((event.getEntity() instanceof Player)) {
                    Player ene = (Player)event.getEntity();
                    Champion enemy = LOL.skill.get(ene.getName());
            	}
            }*/
	    }
	}
        
	public void deathEvent(Player p, EntityDamageEvent event){
        EntityDamageEvent cause = event;
        if ((cause instanceof EntityDamageByEntityEvent)) {
            EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent)cause;
            Entity damager = subEvent.getDamager();
            if ((damager instanceof Player)) {
                Player attacker = (Player)damager;
                Bukkit.broadcastMessage(attacker.getDisplayName() + ChatColor.DARK_RED + " has slain " + p.getDisplayName());
                //this is for the killer
                Champion attacked = LOL.skill.get(attacker.getPlayer().getName());
                attacked.addkill();
                LOL.updateStats(attacker, attacked.getKill() + "/" + attacked.getDeath() + "/" + attacked.getTurret());
                //this is for the victim
                Champion champion = LOL.skill.get(p.getPlayer().getName());
                champion.addDeath();
                LOL.updateStats(p, champion.getKill() + "/" + champion.getDeath() + "/" + champion.getTurret());
                p.teleport(p.getWorld().getSpawnLocation());
                lol.respawn(p);
            }else if (subEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){
                damager = ((Projectile)damager).getShooter();
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "A turret has slain " + p.getDisplayName());
                Champion champion = LOL.skill.get(p.getPlayer().getName());
                champion.addDeath();
                LOL.updateStats(p, champion.getKill() + "/" + champion.getDeath() + "/" + champion.getTurret());
                p.teleport(p.getWorld().getSpawnLocation());
                lol.respawn(p);
            }else{
            	Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.DARK_RED + " died!");
                Champion champion = LOL.skill.get(p.getPlayer().getName());
                champion.addDeath();
                LOL.updateStats(p, champion.getKill() + "/" + champion.getDeath() + "/" + champion.getTurret());
                p.teleport(p.getWorld().getSpawnLocation());
                lol.respawn(p);
            }
           } 
	}
        
	/*@EventHandler(priority = EventPriority.HIGH)
    public void onDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player p = (Player)event.getEntity();
        String name = p.getName();
        EntityDamageEvent cause = deathCause.get(name);
        if ((cause instanceof EntityDamageByEntityEvent)) {
            EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent)cause;
            Entity damager = subEvent.getDamager();
            if ((damager instanceof Player)) {
                Player attacker = (Player)damager;
                Bukkit.broadcastMessage(attacker.getDisplayName() + ChatColor.DARK_RED + " has slain " + p.getDisplayName());
                //this is for the killer
                Champion attacked = LOL.skill.get(attacker.getPlayer().getName());
                attacked.addkill();
                LOL.updateStats(attacker, attacked.getKill() + "/" + attacked.getDeath() + "/" + attacked.getTurret());
                //this is for the victim
                Champion champion = LOL.skill.get(p.getPlayer().getName());
                champion.addDeath();
                LOL.updateStats(p, champion.getKill() + "/" + champion.getDeath() + "/" + champion.getTurret());
                p.teleport(p.getWorld().getSpawnLocation());
                lol.respawn(p);
            }else if (subEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){
                damager = ((Projectile)damager).getShooter();
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "A turret has slain " + p.getDisplayName());
                Champion champion = LOL.skill.get(p.getPlayer().getName());
                champion.addDeath();
                LOL.updateStats(p, champion.getKill() + "/" + champion.getDeath() + "/" + champion.getTurret());
                p.teleport(p.getWorld().getSpawnLocation());
                lol.respawn(p);
            }else{
            	Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.DARK_RED + " died!");
                Champion champion = LOL.skill.get(p.getPlayer().getName());
                champion.addDeath();
                LOL.updateStats(p, champion.getKill() + "/" + champion.getDeath() + "/" + champion.getTurret());
                p.teleport(p.getWorld().getSpawnLocation());
                lol.respawn(p);
            }
           } 
    }*/
	
	@EventHandler(priority = EventPriority.HIGH)
	public void breakBlock(BlockBreakEvent event){
		if(event.getBlock().getTypeId() == 49){
		   Location place = event.getBlock().getLocation();
		   Block block = event.getBlock();
		   int id = event.getPlayer().getWorld().getBlockTypeIdAt(place.getBlockX(), place.getBlockY()+3, place.getBlockZ());
		   if(id == 21 || id == 73 || id == 74 || id == 22 || id == 45){
			   event.getPlayer().getWorld().getBlockAt(place.getBlockX(), place.getBlockY()+3, place.getBlockZ()).setTypeId(0);  
			   event.getPlayer().getWorld().createExplosion(place, 0);
			   Bukkit.broadcastMessage(ChatColor.DARK_RED + event.getPlayer().getDisplayName() + " has destroyed a turret!");
			   if(LOL.skill.containsKey(event.getPlayer().getName())){
				 Champion champion = LOL.skill.get(event.getPlayer().getName());
				 LOL.updateStats(event.getPlayer(), champion.getKill() + "/" + champion.getDeath() + "/" + champion.getTurret());
				 LOL.skill.get(event.getPlayer().getName()).addturret();
			   }else{
			     LOL.updateStats(event.getPlayer(), "0/0/1/");
			     LOL.skill.get(event.getPlayer().getName()).addturret();
			   }
		   }else if(lol.config.getBnexusbot().getBlockX() == place.getBlockX() && lol.config.getBnexusbot().getBlockY() == place.getBlockY() && lol.config.getBnexusbot().getBlockZ() == place.getBlockZ()){
			   lol.bnexus.destroyPart(block);
		   }else if(lol.config.getBnexusmid().getBlockX() == place.getBlockX() && lol.config.getBnexusmid().getBlockY() == place.getBlockY() && lol.config.getBnexusmid().getBlockZ() == place.getBlockZ()){
			   lol.bnexus.destroyPart(block);
		   }else if(lol.config.getBnexustop().getBlockX() == place.getBlockX() && lol.config.getBnexustop().getBlockY() == place.getBlockY() && lol.config.getBnexustop().getBlockZ() == place.getBlockZ()){
			   lol.bnexus.destroyPart(block);
		   }else if(lol.config.getPnexusbot().getBlockX() == place.getBlockX() && lol.config.getPnexusbot().getBlockY() == place.getBlockY() && lol.config.getPnexusbot().getBlockZ() == place.getBlockZ()){
			   lol.pnexus.destroyPart(block);
		   }else if(lol.config.getPnexusmid().getBlockX() == place.getBlockX() && lol.config.getPnexusmid().getBlockY() == place.getBlockY() && lol.config.getPnexusmid().getBlockZ() == place.getBlockZ()){
			   lol.pnexus.destroyPart(block);
		   }else if(lol.config.getPnexustop().getBlockX() == place.getBlockX() && lol.config.getPnexustop().getBlockY() == place.getBlockY() && lol.config.getPnexustop().getBlockZ() == place.getBlockZ()){
			   lol.pnexus.destroyPart(block);
		   }
		}
	}
        // Some code here
	
	@EventHandler
	  public void onProjectileHit(ProjectileHitEvent event) {
	    if ((event.getEntity() instanceof Arrow)) {
	      Arrow arrow = (Arrow)event.getEntity();
	      if ((arrow.getShooter() instanceof Player)) {
	        Player player = (Player)arrow.getShooter();
	        if (!LOL.skill.containsKey(player.getName())) {
	          return;
	        }
	       if(LOL.skill.get(player.getName()).isUltiActivated() && (LOL.skill.get(player.getName()).getChampion() instanceof Ashe)){
	        TNTPrimed tnt = event.getEntity().getWorld().spawn(arrow.getLocation(), TNTPrimed.class);
			tnt.setFuseTicks(2);
	        event.getEntity().remove();
	        LOL.skill.get(player.getName()).setUlticooldown(100);
	        LOL.skill.get(player.getName()).setUltiused();
	       }
	      }
	    }
	  }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int blockId = player.getItemInHand().getType().getId();
        if(!LOL.skill.containsKey(player.getName())){
        	return;
        }
        if(blockId == 276 && LOL.skill.get(player.getName()).isUltiActivated() && (LOL.skill.get(player.getName()).getChampion() instanceof Lux)){
        	//float dir = (float)Math.toDegrees(Math.atan2(player.getLocation().getBlockX() - player.getTargetBlock(HSet, 100).getX(), b.getZ() - player.getLocation().getBlockZ()));
        	Vector v = player.getLocation().getDirection();
        	Vector pos = player.getLocation().toVector();
        	pos.setY(pos.getY()+1);
        	final HashSet blocks = new HashSet();
        	for(int e = 1; e < 30; e++){
        	 Vector d = v.multiply(1);
             Vector done = pos.add(d);
             pos = done;
        	 int x = done.getBlockX();
        	 int y = done.getBlockY();
        	 int z = done.getBlockZ();
        	 Block block = player.getWorld().getBlockAt(x, y, z);
        	 if(block.getTypeId() == 0){
        	  List entity = player.getWorld().getEntities();
        	  for (Iterator iter = entity.iterator(); iter.hasNext();) {
       		   Entity thingy = (Entity) iter.next();
       		   Location position = thingy.getLocation();
       		   if(LOL.InRange(position.getBlockX(), position.getBlockY(), position.getBlockZ(), x, y, z, 4)){
       			   if(thingy instanceof Player){
       				 Player guy = (Player)thingy;
       				 int ratio = LOL.skill.get(player.getName()).getAp();
       				 guy.damage(ratio, player);
       			     thingy.setFireTicks(100);
       			   }else{
       				 thingy.setFireTicks(1000); 
       			   }
       		   }
       	      }
        	  block.setTypeId(35);
        	  block.setData((byte) 14);
        	  blocks.add(block);
        	 }
        	}
        	BukkitScheduler timer = Bukkit.getScheduler();
    	    timer.scheduleSyncDelayedTask(lol, new Runnable(){
    	     @Override
			public void run(){
    	       for (Iterator<Block> i = blocks.iterator(); i.hasNext(); ) {		 
           	      Block block = i.next();
           	      if(block.getType() == Material.WOOL){
           	    	  block.setTypeId(0);
           	      }     	 
               }
    	     }
    	    }, 20);
        }
    }

}
