package spell;

import pong.Player;
import pong.Pong;

public abstract class Spell {
    Pong pong;
    Player player;

    int cooldownCounter;
    int cost;
    int cooldown;

    public Spell() {
    }

    public void setup(Pong pong, Player player) {
        this.player = player;
        this.pong = pong;
        this.cooldownCounter = 0;
        this.cost = setCost();
        this.cooldown = setCoolDown();
    }

    public abstract int setCost();

    public abstract int setCoolDown();

    public void cast() {
        if (player.mana > cost && cooldownCounter == 0) {
            applyEffects();
            player.mana -= cost;
            cooldownCounter += cooldown;
        }
    }

    /**
     * What does the spell do *
     */
    public abstract void applyEffects();

    public void coolDown() {
        if (cooldownCounter > 0) {
            cooldownCounter -= 1;
        }
    }
}
