package pong.contact;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import pong.Pong;
import shapes.PongShape;
import sounds.BounceSound;
import utils.Debugger;

/**
 * Created by sihrc on 7/9/14.
 */
public class PaddleBallPair extends ContactPair {
    /**
     * Debbie *
     */
    Debugger debbie = new Debugger(PaddleBallPair.class.getSimpleName());

    /**
     * Super Constructor
     *
     * @param shapeA - Contact Shape A
     * @param shapeB - Contact SHape B
     * @param pong*
     */
    public PaddleBallPair(PongShape shapeA, PongShape shapeB, Pong pong) {
        super(shapeA, shapeB, pong);
    }

    @Override
    public void beginContactI(Contact contact) {
        debbie.d("Begin Contact 1");
    }

    @Override
    public void preSolveII(Contact contact, Manifold oldManifold) {
        debbie.d("Presolve 2");
    }

    @Override
    public void postSolveIII(Contact contact, ContactImpulse impulse) {
        debbie.d("postSolve 3");

        float impulse0 = impulse.normalImpulses[0];
        float impulse1 = impulse.normalImpulses[1];
        float imp = impulse0 > impulse1 ? impulse0 : impulse1;
        imp = imp > 1 ? 1 : imp;
        pong.addNonPersistent(new BounceSound(imp));
    }

    @Override
    public void endContactIV(Contact contact) {
        debbie.d("endContact IV");

    }
}