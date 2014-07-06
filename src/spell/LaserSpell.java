package spell;

import pong.Pong;
import server.Player;
import shapes.Ball;

public class LaserSpell extends Spell {
    public LaserSpell(Pong pong, Player player) {
        super("Laser", 5, 60, player);
    }

    public void cast() {
        System.out.println("LaserSpell Called Cast \n\n");
        Ball laser = Pong.pong.makeLaser(player);
        Pong.pong.addShape(laser);
        DestroyBall destroyBall = new DestroyBall(laser, 30);
        Pong.delayedEffects.add(destroyBall);
    }
}
