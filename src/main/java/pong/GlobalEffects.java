package pong;

import org.jbox2d.dynamics.BodyType;
import shapes.PongShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rbooth on 7/9/14.
 */
public class GlobalEffects extends ArrayList<GlobalEffect> {

    public GlobalEffects(String option) {
        super();
        if ("drag".equals(option)) {
            SpeedLimitDrag sld = new SpeedLimitDrag(10, 0.1f);
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
