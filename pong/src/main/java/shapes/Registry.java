package shapes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import serialize.PongPacket;
import utils.Debugger;

/**
 * Created by sihrc on 7/5/14.
 */
public class Registry {
        static Debugger debbie = new Debugger(Registry.class.getSimpleName());

        private static final List<Class<? extends PongPacket>> classes = new ArrayList<Class<? extends PongPacket>>(
                Arrays.asList(
                        Ball.class,
                        Laser.class,
                        Paddle.class,
                        Wall.class,
                        InfoBoard.class
                )
        );

        public static final Map<Character, Class<? extends PongPacket>> registry = new HashMap<Character, Class<? extends PongPacket>>(){{
            /** Char Limit **/
            assert  classes.size() < 255;

            for (int i = 0; i < classes.size(); i++) {
                put((char) i, classes.get(i));
            }
        }};

        /** Get ID for PongPacket **/
        public static char getId(Class<? extends PongPacket> packet) {
            return (char) classes.indexOf(packet);
        }

        /** Get PongPacket **/
        public static PongPacket getPacket(char key) {
            try {
                return registry.get(key).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
}
