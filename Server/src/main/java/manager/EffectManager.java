package manager;

import org.jbox2d.dynamics.BodyType;

import java.util.ArrayList;
import java.util.List;

import pong.DragEffect;
import pong.GlobalEffect;
import shapes.PongShape;

/**
 * Created by rbooth on 7/9/14.
 */
public class EffectManager extends ArrayList<GlobalEffect> {

    public EffectManager(String option) {
        super();
        if ("drag".equals(option)) {
            DragEffect sld = new DragEffect(10f, 0.1f);
            this.add(sld);
        }
    }

    public void applyForces(List<PongShape> pongShapes) {
        for (PongShape pongShape : pongShapes) {
            if (pongShape.getBody() == null) {
                continue; // pongshapes without bodies are only pongshapes for rendering purposes
            }
            if (!pongShape.getBody().getType().equals(BodyType.DYNAMIC)) {
                continue;
            }
            for (GlobalEffect globalEffect : this) {
                globalEffect.applyForce(pongShape);
            }
        }

    }
}
