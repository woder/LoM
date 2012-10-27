package me.woder.LOL;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HealthPlayer
{
  private int maxHealth;
  private int currentHealth;
  private Player p;
  private boolean isInvuln;
  private int healthUpdateFrequency;

  public HealthPlayer(Player p, int maxHealth, int currentHealth, int healthUpdateFrequency)
  {
    this.p = p;
    this.maxHealth = maxHealth;
    this.currentHealth = currentHealth;
    this.healthUpdateFrequency = healthUpdateFrequency;
  }

  public void heal() {
    this.currentHealth = this.maxHealth;
    updatePhysicalHealth();
  }

  public boolean applyDamage(int damage) {
    if (this.currentHealth <= 0)
    {
      return false;
    }
    if (this.isInvuln) {
      return false;
    }
    this.currentHealth -= damage;
    if (this.currentHealth <= 0) {
      this.p.setHealth(0);
      this.p.sendMessage("Im applying: " + this.currentHealth);
      return true;
    }if (damage > 0) {
      updatePlayerMessage(false);
    }

    if (this.currentHealth > this.maxHealth) {
      this.p.setHealth(20);
      this.currentHealth = this.maxHealth;
      return true;
    }
    double percentHealth = this.currentHealth / this.maxHealth;
    this.p.sendMessage("Percent: " + percentHealth);
    double vanillaHealth = ((this.currentHealth * 20) / this.maxHealth);//Math.ceil(percentHealth * 20.0D);
    this.p.sendMessage("You should have: " + vanillaHealth);
    if(this.currentHealth < 1 && this.currentHealth != 0){
      vanillaHealth = 1;
    }
    this.p.setHealth((int)Math.ceil(vanillaHealth));
    updatePlayerMessage(true);
    return true;
  }

  public void updatePlayerMessage(boolean useFreq) {
    if (((useFreq) && (this.currentHealth % this.healthUpdateFrequency == 0)) || (!useFreq))
      this.p.sendMessage(ChatColor.GREEN + "Current Health: " + this.currentHealth + " of " + this.maxHealth);
  }

  public void updatePhysicalHealth()
  {
    if (this.currentHealth > this.maxHealth) {
      this.p.setHealth(20);
      this.currentHealth = this.maxHealth;
      return;
    }
    double percentHealth = this.currentHealth / this.maxHealth;
    double vanillaHealth = Math.ceil(percentHealth * 20.0D);
    this.p.setHealth((int)vanillaHealth);
  }

  public int getMaxHealth() {
    return this.maxHealth;
  }
  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
  }
  public int getCurrentHealth() {
    return this.currentHealth;
  }
  public Player getP() {
    return this.p;
  }
  public boolean isInvuln() {
    return this.isInvuln;
  }
  public void setInvuln(boolean isInvuln) {
    this.isInvuln = isInvuln;
  }

  public void setHealthUpdateFrequency(int healthUpdateFrequency) {
    this.healthUpdateFrequency = healthUpdateFrequency;
  }

  public int getHealthUpdateFrequency() {
    return this.healthUpdateFrequency;
  }
}

