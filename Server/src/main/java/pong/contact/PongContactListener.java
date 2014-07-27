package pong.contact;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.ArrayList;
import java.util.List;

import utils.Debugger;

/**
 * Created by sihrc on 7/10/14.
 */

public class PongContactListener implements ContactListener {
    /**
     * Debugger *
     */
    Debugger debbie = new Debugger(PongContactListener.class.getSimpleName());

    /**
     * Private variables *
     */
    List<ContactPair> pairs = new ArrayList<ContactPair>();
    ContactPair currentPair;
    boolean matched = false;


    /**
     * Modifying Contact Pairs in Contact List *
     */
    public void registerPair(ContactPair pair) {
        pairs.add(pair);
    }

    public void removePair(ContactPair pair) {
        pairs.remove(pair);
    }

    @Override
    public void beginContact(Contact contact) {
        checking(contact);
        if (matched)
            currentPair.beginContactI(contact);
    }

    /**
     * Check for specific contact pair to use *
     */
    private void checking(Contact contact) {
        if (!matched) {
            for (ContactPair pair : pairs) {
                if (pair.check(contact)) {
                    matched = true;
                    currentPair = pair;
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (matched) {
            currentPair.endContactIV(contact);
            matched = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (matched)
            currentPair.preSolveII(contact, oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if (matched)
            currentPair.postSolveIII(contact, impulse);
    }
}