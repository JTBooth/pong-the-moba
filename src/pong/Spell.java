package pong;

import server.Player;

public abstract class Spell {
	int cost;
	int cooldownCounter;
    int cooldown;
	String name;
	Player player;
	
	public Spell(String name, int cost, int cooldown, Player player) {
		this.name=name;
		this.setCost(cost);
		this.setCooldown(cooldown);
		this.setCooldownCounter(0);
		this.player=player;
	}
	
	public abstract void cast();

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCooldownCounter() {
		return cooldownCounter;
	}

	public void setCooldownCounter(int cooldownCounter) {
		this.cooldownCounter = cooldownCounter;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public String getName() {
		return name;
	}
}
