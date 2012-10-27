package me.woder.AI;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.util.Vector;

import me.woder.LOL.LOL;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.Item;
import net.minecraft.server.ItemFood;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathPoint;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomTargetNonTamed;
import net.minecraft.server.World;

@SuppressWarnings("unused")
public class EntityMinion extends EntityZombie{

    private boolean b = false;
    private float c;
    private float g;
    private boolean h;
    private boolean i;
    private float j;
    private float k;
    private Navigation nav;
    private Timer timer = new Timer();
    private Location end;
    private int maxIter;
    boolean trueOrFalse = false;

    public EntityMinion(World world, Location loc, float l, boolean can) {
        super(world);
        this.texture = "/mob/zombie.png";
        this.b(0.6F, 0.8F);
        //this.goalSelector.a(0, new PathfinderMinion(this, loc, l, can));
    }
    
    public void goToLocation(Entity entity, int x, int y, int z, LOL plugin){
    	//Entity is the pig entity
    	//You'll also need to divide the path's goal, because the AI can't reach further then certain distances.
    	while(trueOrFalse = true){
    	            if(entity instanceof Wolf){
    	             
    	            EntityCreature creature = ((CraftCreature)((Zombie)entity)).getHandle();
    	 
    	            Navigation navigation = creature.getNavigation();
    	            navigation.a(/*The location's X*/x, /*The location's Y*/y, /*The location's Z*/z, /*The speed, 0.3F looks good*/0.3F);
    	}
    	Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){
    	@Override
		public void run(){
    	trueOrFalse = true;
    	}}, 2000L);
    	}
    }
       
}