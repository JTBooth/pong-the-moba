package spell;

// Note that when ticksRemaining is zero, the object will no longer be ticked.
public abstract class DelayedSpell {
    public int ticksRemaining;

    public DelayedSpell(int timeout) {
        this.ticksRemaining = timeout;
    }

    public void tick() {
        if (ticksRemaining > 0) {
            ticksRemaining -= 1;
        }
        if (timeToAct()) {
            takeAction();
        }
    }

    public abstract boolean timeToAct();

    public abstract void takeAction();
}
