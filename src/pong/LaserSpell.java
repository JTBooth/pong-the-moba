package pong;

import server.Player;

public class LaserSpell extends Spell {
	public LaserSpell(Pong pong, Player player) {
		super("Laser", 0, 30, pong, player);
	}
	
	public void cast() {
        System.out.println("LaserSpell Called Cast \n\n");
        Ball laser = pong.makeLaser(player);
        DestroyBall destroyBall = new DestroyBall(laser, 90);
        Pong.delayedEffects.add(destroyBall);
	}
}
