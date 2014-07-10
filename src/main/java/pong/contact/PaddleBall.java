package pong.contact;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import pong.Pong;
import shapes.Ball;
import shapes.Paddle;
import utils.Debugger;

/**
 * Created by sihrc on 7/9/14.
 */
public class PaddleBall implements ContactListener {
    private final Pong pong;
    /** Debbie **/
    Debugger debbie = new Debugger(PaddleBall.class.getSimpleName() + " contact", Debugger.INFO);

    /** Contact Body **/
    Paddle paddle;
    Ball ball;

    public PaddleBall(Paddle paddle, Ball ball, Pong pong){
        super();
        this.paddle = paddle;
        this.ball = ball;
        this.pong=pong;
    }

    @Override
    public void beginContact(Contact contact) {

        debbie.i("Contact");
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        float impulse0 = impulse.normalImpulses[0];
        float impulse1 = impulse.normalImpulses[1];
        float imp = impulse0 > impulse1 ? impulse0 : impulse1;
        imp = imp > 1 ? 1 : imp;
        pong.getSoundmaster().playBounce(imp);
    }
}
