package pong.contact;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import shapes.Ball;
import shapes.Paddle;
import utils.Debugger;

/**
 * Created by sihrc on 7/9/14.
 */
public class PaddleBall implements ContactListener {
    /** Debbie **/
    Debugger debbie = new Debugger(PaddleBall.class.getSimpleName() + " contact", Debugger.INFO);

    /** Contact Body **/
    Paddle paddle;
    Ball ball;

    public PaddleBall(Paddle paddle, Ball ball){
        super();
        this.paddle = paddle;
        this.ball = ball;
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

    }
}
