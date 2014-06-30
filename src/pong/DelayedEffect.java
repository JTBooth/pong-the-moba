package pong;
// Note that when ticksRemaining is zero, the object will no longer be ticked.
public abstract class DelayedEffect {
	public int ticksRemaining;
	public void tick() {
		if (ticksRemaining > 0) {
			ticksRemaining -= 1;
		}
		if (timeToAct()) {
			takeAction();
		}
	}
	abstract boolean timeToAct();
	
	abstract void takeAction();
}
