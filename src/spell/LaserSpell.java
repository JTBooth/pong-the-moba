package spell;

import pong.DestroyBall;
import pong.Pong;
import server.Player;
import shapes.Ball;

public class LaserSpell extends Spell {
    public LaserSpell(Pong pong, Player player) {
        super("Laser", 0, 30, player);
    }

    public void cast() {
        System.out.println("LaserSpell Called Cast \n\n");
        Ball laser = Pong.pong.makeLaser(player);
        Pong.pong.addShape(laser);
        DestroyBall destroyBall = new DestroyBall(laser, 90);
        Pong.delayedEffects.add(destroyBall);
    }
}
