package me.woder.AI;

import java.util.Timer;

import net.minecraft.server.EntityPigZombie;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.Navigation;
import net.minecraft.server.World;

import org.bukkit.Location;

@SuppressWarnings("unused")
public class EntityMinionPurp extends EntityPigZombie{
    
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

	public EntityMinionPurp(World world, Location loc, float l, boolean can) {
		super(world);
        this.texture = "/mob/pigzombie.png";
        this.b(0.6F, 0.8F);
	}
    
       
}
