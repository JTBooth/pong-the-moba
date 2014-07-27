package pong.contact;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import pong.Pong;
import shapes.PongShape;
import utils.Registry;

/**
 * Created by sihrc on 7/10/14.
 */
public abstract class ContactPair {
    char[] id;
    Pong pong;

    /**
     * Super Constructor *
     */
    public ContactPair(PongShape shapeA, PongShape shapeB, Pong pong) {
        this.pong = pong;
        this.id = new char[2];
        setContactPair(shapeA, shapeB);
    }

    /**
     * Sets the contact pair ID - this should be called in the constructor *
     */
    public void setContactPair(PongShape shapeA, PongShape shapeB) {
        char a = Registry.getPacketId(shapeA.getClass());
        char b = Registry.getPacketId(shapeB.getClass());

        if (a < b) {
            id[0] = a;
            id[1] = b;
        } else {
            id[0] = b;
            id[1] = a;
        }
    }

    /**
     * Check if the fixtures match accordingly *
     */
    public boolean check(Contact contact) {
        char a = (Character) contact.getFixtureA().getUserData();
        char b = (Character) contact.getFixtureB().getUserData();
        return ((a < b) && (id[0] == a) && (id[1] == b)) || ((a >= b) && (id[0] == b) && (id[1] == a));
    }

    /**
     * Abstract Methods to fill in *
     */
    public abstract void beginContactI(Contact contact);

    public abstract void preSolveII(Contact contact, Manifold oldManifold);

    public abstract void postSolveIII(Contact contact, ContactImpulse impulse);

    public abstract void endContactIV(Contact contact);
}