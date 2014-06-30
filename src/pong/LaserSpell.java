package pong;

import server.Player;

public class LaserSpell extends Spell {
	public LaserSpell(Pong pong, Player player) {
		super("Basic Laser", 0, 30, pong, player);
	}
	
	public void cast() {
        System.out.println("============= 1234 ============= CAST CAST CAST i am here");
        pong.makeLaser(player);
	}
}
