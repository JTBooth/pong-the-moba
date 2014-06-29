package pong;

import server.Player;

public class LaserSpell extends Spell {
	public LaserSpell(Pong pong, Player player) {
		super("Basic Laser", 1, 30, pong, player);
	}
	
	public void cast() {
		pong.shootLaser(player);
	}

	
}
