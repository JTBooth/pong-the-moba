package spell;

import pong.Pong;
import server.Player;

public abstract class Spell {
    int cost;
    int cooldownCounter;
    int cooldown;
    String name;
    Player player;
    Pong pong;

    public Spell(String name, int cost, int cooldown, Player player, Pong pong) {
        this.name = name;
        this.setCost(cost);
        this.setCooldown(cooldown);
        this.setCooldownCounter(0);
        this.player = player;
        this.pong = pong;
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
